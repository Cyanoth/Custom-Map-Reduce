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

    public void addEntry(AbstractDetails obj) {
        mDataChunk.add(obj);
        LOGGER.log(Level.FINE, "Added an entity to the mapper: " + mMapperID);
    }

    @Override
    public ArrayList<KeyValuePair> call() { //Returns mapped results
        LOGGER.log(Level.INFO, "A Mapper with the ID: " + mMapperID + " has started!");
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (AbstractDetails dataChunk : mDataChunk) {
            KeyValuePair mappedPair;

            if (mapToValue == null)
                mappedPair = new KeyValuePair(dataChunk.getValueByName(mapKey), 1);
            else
                mappedPair = new KeyValuePair(dataChunk.getValueByName(mapKey), dataChunk.getValueByName(mapToValue));

            mappedEntries.add(mappedPair);
        }
        LOGGER.log(Level.FINE, "Execution of mapper thread " + mMapperID + " has completed.");
        return mappedEntries;
    }
}
