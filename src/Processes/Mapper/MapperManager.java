import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapperManager {
    private static final Logger LOGGER = Logger.getLogger(MapperManager.class.getName());
    private Mapper[] mMappers;

    public void createMappers(ArrayList<AbstractDetails> data, Keys mapKeyName, Keys mapKeyValue) //Set mapKeyValue to null to run mapper as a counter.
    {
        int amountOfEntries = data.size();

        int mappersToCreate = (int) Math.ceil(amountOfEntries / Configuration.MAX_MAPPER_DATAENTRIES); //Create this many mapper objects.
        mMappers = new Mapper[mappersToCreate];
        LOGGER.log(Level.INFO, "Allocated memory for: " + mappersToCreate + " mapper objects.");

        int tmp_MapperCounter = 0;
        int current_DataEntry = 0;
        for (int i = 0; i < mappersToCreate; i++) { //For each mapper, initialise it and pass some data to it.
            if (mapKeyValue == null)
                mMappers[i] = new Mapper(mapKeyName, tmp_MapperCounter);
            else
                mMappers[i] = new Mapper(mapKeyName, mapKeyValue, tmp_MapperCounter);

            tmp_MapperCounter++;
            for (int j = 0; j < Configuration.MAX_MAPPER_DATAENTRIES; j++)
            {
                if (j < amountOfEntries) // //TODO: This has recently changed, will need to check this part works... Ensures we don't read past the end of the file.
                    mMappers[i].addEntry(data.get(current_DataEntry++)); //Add a single entry to the mapper
            }
        }
    }

    public ArrayList<KeyValuePair> executeAllMapperThreads() //todo: create custom exception   //TODO: Need a way of returning an mapper error/stopping all threads.
    {
        LOGGER.log(Level.INFO, "Executing Mappers" + " (Max Simultaneous: " + Configuration.MAX_RUNNING_MAPPERS +  ") Please Wait...");
        ThreadPoolExecutor mapperThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.MAX_RUNNING_MAPPERS);
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (Mapper singleMapper : mMappers) { //Execute Each Mapper
            try {
                Future<ArrayList<KeyValuePair>> mappedChunk = mapperThreads.submit(singleMapper);
                mappedEntries.addAll(mappedChunk.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        mapperThreads.shutdown();
        LOGGER.log(Level.INFO, "All Mapper Thread Executions Completed!");
        return shuffleSortMappedData(mappedEntries);
    }


    private ArrayList<KeyValuePair> shuffleSortMappedData(ArrayList<KeyValuePair> sortPairs)
    {
        LOGGER.log(Level.INFO, "Shuffle & Sorting Mapped Data...");
        Collections.sort(sortPairs); //Sort & Shuffle the Mapped Entries //TODO: Add a time which tracks how long the sorting algorithm took.
        LOGGER.log(Level.INFO, "Shuffle Sort Completed!");

        return sortPairs;
    }

}
