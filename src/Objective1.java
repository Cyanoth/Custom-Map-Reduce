import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Seperate common functions for each object into its own file.

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    private ArrayList<PassengerEntry> parsedPassengerDataFile;

    private Mapper[] mMappers;
    private ArrayList<Reducer> mReducers = new ArrayList<>();

    //String - Output String.
    private ArrayList<KeyValuePair> totalMappedEntites = new ArrayList<>();

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
        System.out.println("I'm here.");

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
        LOGGER.log(Level.INFO, "Executing Mappers... Wait..."); //TODO: Limit to MAX_RUNNING_MAPPERS at anyone time.
        for (Mapper singleMapper : mMappers) { //Execute Each Mapper
            try {
                totalMappedEntites.addAll(singleMapper.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shuffleSortMappedData()
    {
        LOGGER.log(Level.INFO, "Shuffle & Sorting Mapped Data...");
        Collections.sort(totalMappedEntites); //Sort & Shuffle the Mapped Entries //TODO: Add a time which tracks how long the sorting algorithm took.
        LOGGER.log(Level.INFO, "Shuffle Sort Completed!");
    }

    private void setupReducerObjects() //TODO: Pass in as parameter rather than global variable?
    {
        //IMPORTANT: THE DATA MUST BE SHUFFLED/SORTED FOR THIS FUNCTION TO COMPLETE CORRECTLY...
        //Get key, if unique (not in Unique Key array) then start sending thse to the reducer until a new key pops up.

        String currentKeyValue = "";
        int currentReducerNumber = -1;

        for (int i = 0; i < totalMappedEntites.size(); i++)
        {
            if (!currentKeyValue.equals(totalMappedEntites.get(i).getKey1())) { //TODO: Check this comparsion statement, rather confusing?
                currentReducerNumber++;
                mReducers.add(new Reducer("fromAirport", currentReducerNumber));
                mReducers.get(currentReducerNumber).addKeyValuePair(totalMappedEntites.get(i));
                currentKeyValue = totalMappedEntites.get(i).getKey1();
            }
            else
                mReducers.get(currentReducerNumber).addKeyValuePair(totalMappedEntites.get(i));
        }
    }

    private static void outputResults() {
        //Call each reducer and output its result.
        System.out.println();
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
