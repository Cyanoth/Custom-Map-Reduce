import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mapper implements Callable<ArrayList<KeyValuePair>> {
    private static final Logger LOGGER = Logger.getLogger(Mapper.class.getName());
    private final int mMapperID;

    //CONSIDER: Add 'mode' variable for simple mapper or combiner & mapper (first uses ArrayList, second uses HashMap)?
    private ArrayList<PassengerEntry> mDataChunk;
    private String compareKey; //TODO: Will Probably used for a later objective, so leaving in.

    public Mapper(String keyName, int mapperID)
    {
        this.mDataChunk = new ArrayList<>();
        this.compareKey = keyName;
        this.mMapperID = mapperID;
        LOGGER.log(Level.FINE, "A Mapper with the ID: " + mMapperID + " has been initialized!");
    }

    public int addPassengerEntry(PassengerEntry obj) {
        //TODO: Systmatic Checking should take place in this function. //TODO: Check this does not exceed MAX_ChunkSize?
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
        //TODO: Checker mapper actually has entities to return.s
        LOGGER.log(Level.INFO, "A Mapper with the ID: " + mMapperID + " has started!");
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (int i = 0; i < mDataChunk.size(); i++) {
            KeyValuePair pair = new KeyValuePair((String) mDataChunk.get(i).getValueByName(compareKey), 1); //TODO: Change 1?
            mappedEntries.add(pair);
        }
        LOGGER.log(Level.FINE, "Execution of mapper thread " + mMapperID + " has completed.");
        return mappedEntries;
    }
}
