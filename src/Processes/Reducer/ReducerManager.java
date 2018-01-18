import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread Management for the Reducers. Setups a number of Reducer and passes each one a
 *  data with a single key.  Once all have been setup. Uses ThreadPoolExecutor to simultaneously execute a number of
 *  Reducers and pass back all results when all Reducer objects have been completed.
 *  */
class ReducerManager {
    private static final Logger LOGGER = Logger.getLogger(MapperManager.class.getName());
    private ArrayList<Reducer> mReducers = new ArrayList<>();  //Store each Mapper object in a list.

    public void createReducerObjects(ArrayList<KeyValuePair> sortedMappedEntities, boolean warnLogicalErrors) //IMPORTANT: THE DATA MUST BE SHUFFLED/SORTED FOR THIS FUNCTION TO COMPLETE CORRECTLY...
    {
        String currentKeyValue = "";
        int currentReducerNumber = -1;

        for (KeyValuePair sortedMappedEntity : sortedMappedEntities) { //Get key, if unique (not in Unique Key array) then start sending these to the reducer until a new key pops up.
            if (!currentKeyValue.equals(sortedMappedEntity.getMapKey())) { //If not the same as previous value, we need a new reducer object.
                mReducers.add(new Reducer(++currentReducerNumber, warnLogicalErrors)); //Create new reducer.
                currentKeyValue = (String) sortedMappedEntity.getMapKey(); //Set current-key value.
            }
            mReducers.get(currentReducerNumber).addKeyValuePair(sortedMappedEntity);
        }
    }

    /**
     * Executes each Reducer. Queue-like function so will only run x amount of Reducer and run the next when
     * one has finished. Ends when all Reducer threads have completed.
     * @return A list of Reduced KeyValue Pairs
     */
    public ArrayList<KeyValuePair> executeAllReducerThreads()
    {
        LOGGER.log(Level.INFO, "Executing Reducers" + " (Max Simultaneous: " + Configuration.MAX_RUNNING_REDUCERS +  ") Please Wait...");
        ThreadPoolExecutor reducerThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.MAX_RUNNING_REDUCERS); //Simultaneous Reducer Limit
        ArrayList<KeyValuePair> reducedPairs  = new ArrayList<>();

        for (Reducer singleReducer : mReducers) { //Execute Each Reducer
            try {
                Future<KeyValuePair> reducerResult = reducerThreads.submit(singleReducer);
                reducedPairs.add(reducerResult.get());  //Keep the result of each Reducer.
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        reducerThreads.shutdown(); //Ensure all threads stopped.
        LOGGER.log(Level.INFO, "All Reducer Thread Executions Completed!");
        return reducedPairs;
    }
}
