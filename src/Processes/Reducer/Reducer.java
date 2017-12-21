import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reducer implements Callable<KeyValuePair> {
    private static final Logger LOGGER = Logger.getLogger(Reducer.class.getName());
    private ArrayList<KeyValuePair> mStoredKeysValuePairs;
    private final int mReducerID;

    public Reducer(int reducerThreadID) {
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
        return new KeyValuePair(mStoredKeysValuePairs.get(0).getKey1(), mStoredKeysValuePairs.size()); //TODO: cant do it by size, take the value then increment it...
    }
}