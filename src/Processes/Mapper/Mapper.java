import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mapper implements Callable<ArrayList<KeyValuePair>> {
    private static final Logger LOGGER = Logger.getLogger(Mapper.class.getName());
    private final int mMapperID;

    private ArrayList<AbstractDetails> mDataChunk = new ArrayList<>();
    private Keys mapKey;
    private Keys mapToValue = null;

    Mapper(Keys mapKey, int mapperID) //Simple counter mapper.
    {
        this.mapKey = mapKey;
        this.mMapperID = mapperID;
        LOGGER.log(Level.FINE, "A Counter Mapper with the ID: " + mMapperID + " has been initialized!");
    }

    Mapper(Keys mapKeyName, Keys mapKeyValue, int mapperID) //Key-Value mapper.
    {
        this.mapKey = mapKeyName;
        this.mapToValue = mapKeyValue;
        this.mMapperID = mapperID;
        LOGGER.log(Level.FINE, "A Key-Value mapper with the ID: " + mMapperID + " has been initialized");
    }

    public int addEntry(AbstractDetails obj) {
        //TODO: Systmatic Checking should take place in this function.
        try {
            mDataChunk.add(obj);
            LOGGER.log(Level.FINE, "Added an entity to the mapper: " + mMapperID);
            return 0; //return success.
        }
        catch (Exception e) {
            return -1;
        }
    }

    @Override
    public ArrayList<KeyValuePair> call() { //Returns mapped results
        //TODO: Checker mapper actually has entities to return.
        LOGGER.log(Level.INFO, "A Mapper with the ID: " + mMapperID + " has started!");
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (int i = 0; i < mDataChunk.size(); i++) {
            KeyValuePair mappedPair;

            if (mapToValue == null)
                mappedPair = new KeyValuePair((String) mDataChunk.get(i).getValueByName(mapKey), 1);
            else
                mappedPair = new KeyValuePair((String) mDataChunk.get(i).getValueByName(mapKey), mDataChunk.get(i).getValueByName(mapToValue));

            mappedEntries.add(mappedPair);
        }
        LOGGER.log(Level.FINE, "Execution of mapper thread " + mMapperID + " has completed.");
        return mappedEntries;
    }
}
