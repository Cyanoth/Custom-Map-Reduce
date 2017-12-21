public class KeyValuePair implements Comparable<KeyValuePair> {
    private String key1;
    private int key2;

    public KeyValuePair(String key1, int key2)
    {
        this.key1 = key1;
        this.key2 = key2;

    }

    public String getKey1()
    {
        return key1;
    }

    public String asString() {
        return "[" + key1 + ", " + key2 + "]";
    }

    public String asFormattedOutputString() {
        return (key1 + ": " + key2);
    }

    @Override
    public int compareTo(KeyValuePair com) {
        if (key1.equals(com.key1))
            return 0;
        else if (key1.compareToIgnoreCase(com.key1) > 0)
            return 1;
        else
            return -1;
    }
}
