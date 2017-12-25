import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reducer implements Callable<KeyValuePair> {
    public enum Type { Count, Concatenate}

    private static final Logger LOGGER = Logger.getLogger(Reducer.class.getName());

    private ArrayList<KeyValuePair> mStoredKeysValuePairs;
    private Type mReducerType;
    private final int mReducerID;

    Reducer(Type reducerType, int reducerThreadID) {
        this.mReducerID = reducerThreadID;
        this.mReducerType = reducerType;
        mStoredKeysValuePairs = new ArrayList<>();
        LOGGER.log(Level.FINE, "A Reducer with the ID: " + mReducerID + " has been initialized!");
    }

    public void addKeyValuePair(KeyValuePair obj) {
        mStoredKeysValuePairs.add(obj);
        LOGGER.log(Level.FINE, "Added an entity to the reducer: " + mReducerID);
    }

    @Override
    public KeyValuePair call() throws Exception {
        String keyValue = (String) mStoredKeysValuePairs.get(0).getMapKey();
        StringBuilder valueResult = new StringBuilder();

        int totalCount = 0;
        int lineCount = 0;

        for (KeyValuePair singlePair : mStoredKeysValuePairs) {
            if (mReducerType == Type.Count)
                totalCount += (int) singlePair.getMapValue();
            else if (mReducerType == Type.Concatenate) {  //TODO: Ensure that each is passengerEntry is unique for a flight (this simply adds one for now)
                valueResult.append(singlePair.getMapValue()).append(" ");
            }
        }

        if (mReducerType == Type.Count)
            return new KeyValuePair(keyValue, totalCount);
        else if (mReducerType == Type.Concatenate)
            return new KeyValuePair(keyValue, valueResult.toString());
        else
            return new KeyValuePair("ERROR", "ERROR");
    }
}
