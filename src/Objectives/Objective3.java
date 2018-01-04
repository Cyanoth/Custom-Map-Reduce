import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective3 {
    private static final Logger LOGGER = Logger.getLogger(Objective3.class.getName());

    public static ArrayList<KeyValuePair> startObjective3(ParsedData data, boolean outputToScreen) {
        LOGGER.log(Level.INFO, "Starting Objective 3");

        if (data == null) { //Makes objective 3 standalone, if not called from objective2 then get its own records.
            ErrorManager.resetErrorManager();
            data = DataFileParser.parseAllFiles();
        }

        if (ErrorManager.hasFatalErrorOccurred()) { return null; }//Fatal Error Occurred, Cannot Continue.

        MapperManager mMapperManager = new MapperManager();

        mMapperManager.createMappers(new ArrayList<>(data.getAllPassengers()), Keys.FlightID, null);
        ArrayList<KeyValuePair> mappedPassengerCount = mMapperManager.executeAllMapperThreads();

        ReducerManager mReducerManager = new ReducerManager();
        mReducerManager.createReducerObjects(mappedPassengerCount, Reducer.Type.Count);
        ArrayList<KeyValuePair> reducedPassengerCount = mReducerManager.executeAllReducerThreads();

        if (outputToScreen)
            outputResults(reducedPassengerCount);

        ErrorManager.displayErrorSummary();
        return reducedPassengerCount;
    }

    private static void outputResults(ArrayList<KeyValuePair> results) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n\n\n----------------------------\nObjective 3 Results - Passenger count on each Flight.\n----------------------------\n");
        int lineCount = 0;
        for (KeyValuePair result: results) {
            outputBuilder.append(result.asFormattedOutputString() + "  |  ");
            if (++lineCount % 5 == 0)
                outputBuilder.append("\n");

        }
        outputBuilder.append("\n");
        OutputFile.write(outputBuilder.toString());
        System.out.println(outputBuilder.toString());
    }
}
