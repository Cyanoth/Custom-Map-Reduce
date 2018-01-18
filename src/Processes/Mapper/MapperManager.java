import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread Management for the Mappers. Setups a number of Mappers and passes each one a
 * 'chunk' of data. Once all have been setup. Uses ThreadPoolExecutor to simultaneously execute a number of Mappers
 *  and pass back all results when all Mappers objects have been completed.
 *  */
public class MapperManager {
    private static final Logger LOGGER = Logger.getLogger(MapperManager.class.getName());
    private Mapper[] mMappers; //Store each Mapper object in a limited Array.

    /**
     * Initial function for setting up the Mapper objects. Limited by hardcoded configuration values.
     * This function needs to be run before executing the mappers.
     * @param data Pass in all Parsed Data. This function will separate it into chunks and pass to each mapper.
     * @param mapKeyName Field name for map key.
     * @param mapKeyValue Field name for value to map to key.
     */
    public void createMappers(ArrayList<AbstractDetails> data, Keys mapKeyName, Keys mapKeyValue)
    {
        int amountOfEntries = data.size();
        int mappersToCreate = (int) Math.ceil((double) amountOfEntries / Configuration.MAX_MAPPER_DATAENTRIES); //Create this amount of Mapper objects (Data Size divided by Max Chunk size rounded up)
        mMappers = new Mapper[mappersToCreate]; //Initialise the array with the previously calculated amount.
        LOGGER.log(Level.INFO, "Allocated memory for: " + mappersToCreate + " mapper objects.");

        int tmp_MapperCounter = 0;
        int current_DataEntry = 0;
        for (int i = 0; i < mappersToCreate; i++) { //For each mapper, initialise it and pass a chunk of data to it.
            mMappers[i] = new Mapper(mapKeyName, mapKeyValue, tmp_MapperCounter);
            tmp_MapperCounter++;

            for (int j = 0; j < Configuration.MAX_MAPPER_DATAENTRIES; j++)
            {
                if (current_DataEntry < amountOfEntries) //Ensure we don't go past the end of the data
                    mMappers[i].addEntry(data.get(current_DataEntry)); //Add a single entry to the mapper

                current_DataEntry++;
            }
        }
    }

    /**
     * Executes each Mapper. Queue-like function so will only run x amount of Mappers and run the next when
     * one has finished. Ends when all Mappers threads have completed.
     * @return A list of Mapped KeyValue pairs from each completed Mappers.
     */
    public ArrayList<KeyValuePair> executeAllMapperThreads()
    {
        LOGGER.log(Level.INFO, "Executing Mappers" + " (Max Simultaneous: " + Configuration.MAX_RUNNING_MAPPERS +  ") Please Wait...");
        ThreadPoolExecutor mapperThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.MAX_RUNNING_MAPPERS); //Simultaneous Mapper Limit
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (Mapper singleMapper : mMappers) { //Execute Each Mapper Thread
            try {
                Future<ArrayList<KeyValuePair>> mappedChunk = mapperThreads.submit(singleMapper);
                mappedEntries.addAll(mappedChunk.get()); //Keep the result of each Mapper.
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        mapperThreads.shutdown(); //Ensure all threads stopped.
        LOGGER.log(Level.INFO, "All Mapper Thread Executions Completed!");
        return shuffleSortMappedData(mappedEntries); //Call Sorting function to sort Mapped KeyValue Pairs.
    }


    /**
     * Using standard Java Collections, sort the list of Mapped KeyValue pairs by the Key Name.
     * @param mappedResults List of Mapped KeyValue Pairs that need sorting.
     * @return A list of SORTED by key KeyValue Pairs.
     */
    private ArrayList<KeyValuePair> shuffleSortMappedData(ArrayList<KeyValuePair> mappedResults)
    {
        LOGGER.log(Level.INFO, "Shuffle & Sorting Mapped Data...");
        Collections.sort(mappedResults); //Sort & Shuffle the Mapped Entries
        LOGGER.log(Level.INFO, "Shuffle Sort Completed!");
        return mappedResults;
    }

}
