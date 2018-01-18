/**
 * This object is used by mappers & reducers to contain a key, a value & an optional counter.
 * Mappers pass instances of these objects to reducers. The reducers combine them into one and is used for the output.
 * Implements Comparable so that the 'Key' can be sorted quickly using standard java libraries.
 */
public class KeyValuePair implements Comparable<KeyValuePair> {
    private Object mapKey; //These are 'objects' because they can be anything (string/int/double)
    private Object mapValue;
    private int reducedTotalCount = -1; //Optional counter (used for Reducer) which contains how many were reduced. Useful for some objectives.


    /**
     * First Constructor, this one is used by Mappers to Map a Key to a Value.
     * Again they are 'objects' because they can be anything (string/int/double)
     * @param mapKey Value for which key is being used
     * @param mapValue The value that is being 'mapped' to the key.
     */
    KeyValuePair(Object mapKey, Object mapValue)
    {
        this.mapKey = mapKey;
        this.mapValue = mapValue;
    }

    /**
     * Second Constructor, this one is used by Reducers to Map a Key to a Value and contain a reduced counter
     * Again they are 'objects' because they can be anything (string/int/double)
     * @param mapKey Value for which key is being used
     * @param mapValue The value that is being 'mapped' to the key.
     * @param reducedCount How many pairs have been used to reduce to one (useful for objective 2 & 3, see report)
     */
    KeyValuePair(Object mapKey, Object mapValue, int reducedCount)
    {
        this.mapKey = mapKey;
        this.mapValue = mapValue;
        this.reducedTotalCount = reducedCount;
    }

    /**
     * @return The value of the Key.
     */
    public Object getMapKey() {
        return mapKey;
    }

    /**
     * @return The value which was mapped to the key.
     */
    public Object getMapValue() {
        return mapValue;
    }

    /**
     * @return The amount of pairs which were reduced to obtain a single pair.
     */
    public int getTotalReducedCount() {
        return reducedTotalCount;
    }

    /**
     * Necessary for the Comparable class. Allows quick sorting of the Mapped KeyValue pairs by key.
     * Uses standard Java libraries to Alphabetically Sort by 'Key'. Ref [todo]
     * @param com Compare this instance to another KeyValue Pair.
     * @return A integer either 0 if equal, 1 if greater or -1 if less than (alphabetical)
     */
    @Override
    public int compareTo(KeyValuePair com) {
        if (mapKey.equals(com.mapKey.toString()))
            return 0;
        else if (mapKey.toString().compareToIgnoreCase(com.mapKey.toString()) > 0)
            return 1;
        else
            return -1;
    }
}
