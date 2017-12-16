import javax.swing.table.AbstractTableModel;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mapper implements Callable<ArrayList<KeyValuePair>> {
    private static final Logger LOGGER = Logger.getLogger(Mapper.class.getName());
    private final int mapperID;
    private final ArrayList<PassengerEntry> dataChunk;
    private final String compareKey; //TODO: Probably used for a later objective, so leaving in.

    public Mapper(ArrayList<PassengerEntry> dataChunk, String keyName, int mapperID)
    {
        //TODO: Systmatic Checking should take place in this function.
        this.dataChunk = dataChunk;
        this.compareKey = keyName;
        this.mapperID = mapperID;
        LOGGER.log(Level.FINE, "A Mapper with the ID: " + mapperID + " has been setup and is ready to run.");
    }

    @Override
    public ArrayList<KeyValuePair> call() {
        LOGGER.log(Level.INFO, "A Mapper with the ID: " + mapperID + " has started!");
        ArrayList<KeyValuePair> mappedEntries = new ArrayList<>();

        for (int i = 0; i < dataChunk.size(); i++) {
            KeyValuePair entry = new KeyValuePair(dataChunk.get(i).getFromAirport(), 1); //TODO: change this into 'keyName'
            mappedEntries.add(entry);

        }
        LOGGER.log(Level.FINE, "Execution of mapper thread " + mapperID + " has completed.");
        return mappedEntries;
    }
}
