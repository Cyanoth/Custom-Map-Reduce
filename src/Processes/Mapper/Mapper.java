import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Each object of this class should be run as a separate thread.
 * A 'chunk' of data (ArrayList of Details of Parsed Data) is passed into this object
 * and the key is mapped to a specified value. Using a Java Callable, it returns a list of mapped KeyValue pairs.
 */
public class Mapper implements Callable<ArrayList<KeyValuePair>> {
    private static final Logger LOGGER = Logger.getLogger(Mapper.class.getName());
    private final int mMapperID;

    private ArrayList<AbstractDetails> mDataChunk = new ArrayList<>();
    private Keys mapKey;
    private Keys mapToValue;

    /**
     * Constructor for this object.
     * @param mapKeyName Field Name for the Key Name
     * @param mapKeyValue Field Name for the Value to map using the Key.
     * @param mapperID A unique identifying number each individual mapper.
     */
    Mapper(Keys mapKeyName, Keys mapKeyValue, int mapperID) //Key-Value mapper.
    {
        this.mapKey = mapKeyName;
        this.mapToValue = mapKeyValue;
        this.mMapperID = mapperID;
    }

    /**
     * Add an entry to an initialised mapper's data chunk.
     * @param obj An individual parsed data detail object.
     */
    public void addEntry(AbstractDetails obj) {
        mDataChunk.add(obj);
    }

    /**
     * Thread management will call this function to execute the mapping function.
     * Maps the value to the key for each entry in the data chunk
     * @return A list of KeyValue pairs which have been mapped.
     */
    @Override
    public ArrayList<KeyValuePair> call() { //Returns mapped results
        LOGGER.log(Level.INFO, "A Mapper with the ID: " + mMapperID + " has started!");
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (AbstractDetails dataChunk : mDataChunk) { //Each entry in this mapper's data chunk.
            KeyValuePair mappedPair;
            mappedPair = new KeyValuePair(dataChunk.getValueByName(mapKey), dataChunk.getValueByName(mapToValue));
            mappedEntries.add(mappedPair);
        }
        return mappedEntries;
    }
}
