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

    public void addKeyValuePair(KeyValuePair obj) {
        mStoredKeysValuePairs.add(obj);
        LOGGER.log(Level.FINE, "Added an entity to the reducer: " + mReducerID);
    }

    @Override
    public KeyValuePair call() throws Exception {
        String keyValue =  mStoredKeysValuePairs.get(0).getKey1();
        int totalCount = 0;
        for (KeyValuePair singlePair: mStoredKeysValuePairs)
            totalCount += singlePair.getKey2();

        return new KeyValuePair(keyValue, totalCount);
    }
}
