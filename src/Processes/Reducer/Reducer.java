import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Each object of this class should be run as a separate thread.
 * A 'chunk' of data (ArrayList of Details of Parsed Data) is passed into this object
 * and the key is mapped to a specified value. Using a Java Callable, it returns a list of mapped KeyValue pairs.
 */
public class Reducer implements Callable<KeyValuePair> {
    private static final Logger LOGGER = Logger.getLogger(Reducer.class.getName());

    private ArrayList<KeyValuePair> mStoredKeysValuePairs;
    private final int mReducerID;
    private boolean mWarnLogicErrors; //Objective Dependant, warn on duplicate data.

    /**
     * Constructor for this object/
     * @param reducerThreadID A unique identifying number each individual reducer.
     * @param warnLogicErrors Depending on requirement for objective, should the reducer show warnings about duplicate data.
     */
    Reducer(int reducerThreadID, boolean warnLogicErrors) {
        this.mReducerID = reducerThreadID;
        this.mWarnLogicErrors = warnLogicErrors;
        mStoredKeysValuePairs = new ArrayList<>();
    }

    /**
     * Add an mapped KeyValue Pair to  initialised mappers data chunk.
     * @param obj Mapped KeyValue Pair. Note that they should all have the same key per reducer.
     */
    public void addKeyValuePair(KeyValuePair obj) {
        mStoredKeysValuePairs.add(obj);
    }

    /**
     * Thread management will call this function to execute the reducer function.
     * Reduces Mapped KeyValue Pairs down to one KeyValue pair.
     * @return A single KeyValue pair which has been reduced.
     */
    @Override
    public KeyValuePair call() {
        LOGGER.log(Level.INFO, "A Reducer with the ID: " + mReducerID + " has started!");
        String keyValue = (String) mStoredKeysValuePairs.get(0).getMapKey(); //Use the 'Key Name' from the first object, since they're all the same afterwards.
        ArrayList<KeyValuePair> uniquePairs = new ArrayList<>();
        StringBuilder valueResult = new StringBuilder();

        int totalCount = 0;

        for (KeyValuePair singlePair : mStoredKeysValuePairs) { //For each entry in the reducer
                boolean alreadyExists = false;
                String compareValue = singlePair.getMapValue().toString();

                for (KeyValuePair comparePair : uniquePairs) {
                    if (compareValue.equals(comparePair.getMapValue().toString())) //Check if the KeyValue pair is the same as any other previous reduced. Flag if True.
                        alreadyExists = true;
                }

                if (alreadyExists) { //KeyValue pair already existed thus not considered. Depending on objective display warning or not.
                    if (mWarnLogicErrors)
                        ErrorManager.generateError("Duplicate Entry Found & Ignored. Value: " + singlePair.getMapValue() + " already exists in Key: " +
                                singlePair.getMapKey(), ErrorType.Warning, ErrorKind.Logical);
                }
                else { //First time it appeared, add it to unique and reduce it
                    uniquePairs.add(singlePair);
                    valueResult.append(singlePair.getMapValue()).append(" ");
                    totalCount++;
                }
        }
         return new KeyValuePair(keyValue, valueResult.toString(), totalCount); //Return a KeyValue pair with a Key, Single Value and a count with total reduced objects.
    }
}
