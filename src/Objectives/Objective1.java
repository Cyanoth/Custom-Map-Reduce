import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Seperate common functions for each object into its own file.

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    private ArrayList<PassengerEntry> parsedPassengerDataFile;

    private Mapper[] mMappers;
    private ArrayList<Reducer> mReducers = new ArrayList<>();

    private ArrayList<KeyValuePair> totalMappedEntities = new ArrayList<>();
    private ArrayList<KeyValuePair> totalReducedEntities = new ArrayList<>();

    public int startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        String passengerDataFile = ("/home/charlie/Documents/MapReduce-AdvComp/Data/AComp_Passenger_data_no_error.csv");

        parsedPassengerDataFile = parseDataFile(passengerDataFile); //io errors are handled within the function itself
        int amountOfEntries = parsedPassengerDataFile.size(); //Amount of entries buffered in.
        LOGGER.log(Level.FINE, "Amount of entries parsed from the data file:" + amountOfEntries);

        setupMapperObjects(amountOfEntries); //Initalize & Pass data chunks to each mapper.
        testing_PauseProgram();
        executeMappers();
        testing_PauseProgram();
        shuffleSortMappedData();
        testing_PauseProgram();
        setupReducerObjects();
        testing_PauseProgram();
        executeReducers();
        testing_PauseProgram();
        outputResults();

        LOGGER.log(Level.INFO, "Program Completed!");

        return 0;
    }

    private static ArrayList<PassengerEntry> parseDataFile(String path) {
        LOGGER.log(Level.FINE, "Parsing Data File: " + path);
        ArrayList<PassengerEntry> parsedEntries = new ArrayList<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            String currentLine;

            while ((currentLine = csvReader.readLine()) != null) {
                String[] sl = currentLine.split(",");
                if (sl.length < 6)
                    System.out.println("Invalid amount of arguments!"); //TODO: Handle This Correctly.
                else
                    parsedEntries.add(new PassengerEntry(sl[0], sl[1], sl[2], sl[3], sl[4], sl[5]));
            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Error has occurred with the data file");//TODO: Handle this efficently.
            e.printStackTrace();
        }

        LOGGER.log(Level.FINE, "Parsed Data File Successfully!");
        return parsedEntries;
    }

    private void setupMapperObjects(int amountOfEntries)
    {
        int mappersToCreate = (int) Math.ceil(amountOfEntries / Configuration.MAX_MAPPER_DATAENTRIES); //Create this many mapper objects.
        mMappers = new Mapper[mappersToCreate];
        LOGGER.log(Level.INFO, "Allocated memory for: " + mappersToCreate + " mapper objects.");

        int tmp_MapperCounter = 0;
        int current_DataEntry = 0;
        for (int i = 0; i < mappersToCreate; i++) { //For each mapper, initialise it & pass data to it.
            mMappers[i] = new Mapper("fromAirport", tmp_MapperCounter);
            tmp_MapperCounter++;

            for (int j = 0; j < Configuration.MAX_MAPPER_DATAENTRIES; j++)
            {
                if (j > amountOfEntries) //If reached end of file, break immediately.. Check this...
                    break;
                else
                    mMappers[i].addPassengerEntry(parsedPassengerDataFile.get(current_DataEntry++)); //Add the entry to the mapper
            }
        }
    }

    private void executeMappers()
    {
        //TODO: Need a way of returning an mapper error/stopping all theads.
        LOGGER.log(Level.INFO, "Executing Mappers" + " (Max Simultaneous: " + Configuration.MAX_RUNNING_MAPPERS +  ") Please Wait...");
        ThreadPoolExecutor mapperThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.MAX_RUNNING_MAPPERS);

        for (Mapper singleMapper : mMappers) { //Execute Each Mapper
            try {
                Future<ArrayList<KeyValuePair>> mappedChunk = mapperThreads.submit(singleMapper);
                totalMappedEntities.addAll(mappedChunk.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        mapperThreads.shutdown();
        LOGGER.log(Level.INFO, "All Mapper Thread Executions Completed!");
    }

    private void shuffleSortMappedData()
    {
        LOGGER.log(Level.INFO, "Shuffle & Sorting Mapped Data...");
        Collections.sort(totalMappedEntities); //Sort & Shuffle the Mapped Entries //TODO: Add a time which tracks how long the sorting algorithm took.
        LOGGER.log(Level.INFO, "Shuffle Sort Completed!");
    }

    private void setupReducerObjects() //TODO: Pass in as parameter rather than global variable?
    {
        //IMPORTANT: THE DATA MUST BE SHUFFLED/SORTED FOR THIS FUNCTION TO COMPLETE CORRECTLY...
        //Get key, if unique (not in Unique Key array) then start sending these to the reducer until a new key pops up.
        String currentKeyValue = "";
        int currentReducerNumber = -1;

        for (int i = 0; i < totalMappedEntities.size(); i++)
        {
            if (!currentKeyValue.equals(totalMappedEntities.get(i).getKey1())) { //If not the same as previous value, we need a new reducer object.
                mReducers.add(new Reducer("fromAirport", ++currentReducerNumber)); //Create new reducer.
                currentKeyValue = totalMappedEntities.get(i).getKey1(); //Set current-key value.
            }

            mReducers.get(currentReducerNumber).addKeyValuePair(totalMappedEntities.get(i));
        }
    }

    //TODO: Move the execute functions into a seperate file, change them to have parameters.
    private void executeReducers()
    {
        LOGGER.log(Level.INFO, "Executing Reducers" + " (Max Simultaneous: " + Configuration.MAX_RUNNING_REDUCERS +  ") Please Wait...");
        ThreadPoolExecutor reducerThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.MAX_RUNNING_REDUCERS);

        for (Reducer singleReducer : mReducers) { //Execute Each Mapper
            try {
                Future<KeyValuePair> reducerResult = reducerThreads.submit(singleReducer);
                totalReducedEntities.add(reducerResult.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        reducerThreads.shutdown();
        LOGGER.log(Level.INFO, "All Reducer Thread Executions Completed!");

    }
    private void outputResults() {
        System.out.println("----------------------------\nResults----------------------------");
        int lineCount = 0;

        for (KeyValuePair result: totalReducedEntities) {
                System.out.print(result.asFormattedOutputString() + "  |  ");
                if (lineCount++ == 5) {
                    System.out.print("\n");
                    lineCount = 0;
                }
        }
    }

    private void testing_PauseProgram()
    {
        System.out.println("Press Enter key to continue..."); //Simple Pause Script.
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
