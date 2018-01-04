import java.lang.reflect.Array;
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
    }

    public void addKeyValuePair(KeyValuePair obj) {
        mStoredKeysValuePairs.add(obj);
    }

    @Override
    public KeyValuePair call() {
        String keyValue = (String) mStoredKeysValuePairs.get(0).getMapKey();
        ArrayList<KeyValuePair> uniquePairs = new ArrayList<>();
        StringBuilder valueResult = new StringBuilder();

        int totalCount = 0;

        for (KeyValuePair singlePair : mStoredKeysValuePairs) {
            if (mReducerType == Type.Count) {
                totalCount += (int) singlePair.getMapValue();
            }
            else if (mReducerType == Type.Concatenate) {
                boolean alreadyExists = false;
                String compareValue = singlePair.getMapValue().toString();

                for (KeyValuePair comparePair : uniquePairs) {
                    if (compareValue.equals(comparePair.getMapValue().toString()))
                        alreadyExists = true;
                }

                if (alreadyExists)
                    ErrorManager.generateError("Duplicate Entry Found & Ignored. Value: " + singlePair.getMapValue() + " already exists in Key: " + singlePair.getMapKey(), ErrorType.Warning, ErrorKind.Logical);
                else {
                    uniquePairs.add(singlePair);
                    valueResult.append(singlePair.getMapValue()).append(" ");
                }
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
