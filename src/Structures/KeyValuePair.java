public class KeyValuePair implements Comparable<KeyValuePair> {
    private Object mapKey; //These are 'objects' because they can be anything (string/int/double)
    private Object mapValue;
    private int reducedTotalCount = -1;


    KeyValuePair(Object mapKey, Object mapValue)
    {
        this.mapKey = mapKey;
        this.mapValue = mapValue;
    }

    KeyValuePair(Object mapKey, Object mapValue, int reducedCount)
    {
        this.mapKey = mapKey;
        this.mapValue = mapValue;
        this.reducedTotalCount = reducedCount;
    }

    public Object getMapKey() {
        return mapKey;
    }

    public Object getMapValue() {
        return mapValue;
    }

    public int getTotalReducedCount() {
        return reducedTotalCount;
    }

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
