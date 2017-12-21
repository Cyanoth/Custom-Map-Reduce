import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

class ReducerManager {
    private static final Logger LOGGER = Logger.getLogger(MapperManager.class.getName());
    private ArrayList<Reducer> mReducers = new ArrayList<>();

    public void setupReducerObjects(ArrayList<KeyValuePair> sortedMappedEntities)
    {
        //IMPORTANT: THE DATA MUST BE SHUFFLED/SORTED FOR THIS FUNCTION TO COMPLETE CORRECTLY...
        //Get key, if unique (not in Unique Key array) then start sending these to the reducer until a new key pops up.
        String currentKeyValue = "";
        int currentReducerNumber = -1;

        for (KeyValuePair sortedMappedEntity : sortedMappedEntities) {
            if (!currentKeyValue.equals(sortedMappedEntity.getKey1())) { //If not the same as previous value, we need a new reducer object.
                mReducers.add(new Reducer(++currentReducerNumber)); //Create new reducer.
                currentKeyValue = sortedMappedEntity.getKey1(); //Set current-key value.
            }

            mReducers.get(currentReducerNumber).addKeyValuePair(sortedMappedEntity);
        }
    }

    public ArrayList<KeyValuePair> executeAllReducerThreads()
    {
        LOGGER.log(Level.INFO, "Executing Reducers" + " (Max Simultaneous: " + Configuration.MAX_RUNNING_REDUCERS +  ") Please Wait...");
        ThreadPoolExecutor reducerThreads = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configuration.MAX_RUNNING_REDUCERS);
        ArrayList<KeyValuePair> reducedPairs  = new ArrayList<>();

        for (Reducer singleReducer : mReducers) { //Execute Each Mapper
            try {
                Future<KeyValuePair> reducerResult = reducerThreads.submit(singleReducer);
                reducedPairs.add(reducerResult.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        reducerThreads.shutdown();
        LOGGER.log(Level.INFO, "All Reducer Thread Executions Completed!");

        return reducedPairs;
    }
}
