import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective3 {
    private static final Logger LOGGER = Logger.getLogger(Objective3.class.getName());

    public static ArrayList<KeyValuePair> startObjective3(ParsedData data, boolean outputToScreen) {
        LOGGER.log(Level.INFO, "Starting Objective 3");

        if (data == null) //Makes objective 3 standalone, if not called from objective2 then get its own records.
            data = DataFileParser.parseAllFiles();

        if (ErrorManager.hasFatalErrorOccurred()) { return null; }//Fatal Error Occurred, Cannot Continue.

        MapperManager mMapperManager = new MapperManager();

        mMapperManager.createMappers(new ArrayList<>(data.getAllPassengers()), Keys.FlightID, null);
        ArrayList<KeyValuePair> mappedPassengerCount = mMapperManager.executeAllMapperThreads();

        ReducerManager mReducerManager = new ReducerManager();
        mReducerManager.createReducerObjects(mappedPassengerCount, Reducer.Type.Count);
        ArrayList<KeyValuePair> reducedPassengerCount = mReducerManager.executeAllReducerThreads();

        if (outputToScreen)
            outputResults(reducedPassengerCount);

        return reducedPassengerCount;
    }

    private static void outputResults(ArrayList<KeyValuePair> results) {
        System.out.println("\n----------------------------\n\t\tResults: Passenger Counts\n----------------------------");
        int lineCount = 0;
        for (KeyValuePair result: results) {
            System.out.print(result.asFormattedOutputString() + "  |  ");
            if (++lineCount % 5 == 0)
                System.out.print("\n");

        }
    }
}
