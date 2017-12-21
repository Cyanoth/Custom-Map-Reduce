import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Charlie on 09/11/2017.
 */
public class Reducer implements Callable<KeyValuePair> {
    private static final Logger LOGGER = Logger.getLogger(Reducer.class.getName());
    private ArrayList<KeyValuePair> mStoredKeysValuePairs;
    private final int mReducerID;

    public Reducer(String keyName, int reducerThreadID) { //TODO: is keyName really necessary for a reducer?!
        this.mReducerID = reducerThreadID;
        mStoredKeysValuePairs = new ArrayList<>();
        LOGGER.log(Level.FINE, "A Reducer with the ID: " + mReducerID + " has been initialized!");
    }

    public int addKeyValuePair(KeyValuePair obj) {
          try {
             mStoredKeysValuePairs.add(obj);
             LOGGER.log(Level.FINE, "Added an entity to the reducer: " + mReducerID);
             return 0; //return success.
        }
        catch (Exception e) {
            return -1;
        }
    }

    @Override
    public KeyValuePair call() throws Exception {
        return new KeyValuePair(mStoredKeysValuePairs.get(0).getKey1(), mStoredKeysValuePairs.size());
    }
}
