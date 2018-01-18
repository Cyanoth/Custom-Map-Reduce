import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Objective 3 of the assignment.
 *  As this objective is achieved previously in Objective 2, this is for a standalone version of objective 3.
 *  See the Assignment report for more details.
 */
public class Objective3_Standalone {
    private static final Logger LOGGER = Logger.getLogger(Objective3_Standalone.class.getName());

    /**
     *  The initial function for executing functions if running Objective 3 as a standalone objective.
     */
    public static void startObjective3() {
        LOGGER.log(Level.INFO, "Starting Objective 3");
        ErrorManager.resetErrorManager(); //Clear any previous errors if previous any objectives have ran.
        ParsedData parsedEntries = DataFileParser.parseAllFiles(); //Call DataParse to parse the Passenger & Top30 Data Files.
        if (ErrorManager.hasFatalErrorOccurred() || parsedEntries == null) { return; }//Fatal Error Occurred, Cannot Continue.

        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllPassengers()), Keys.FlightID, Keys.PassengerID); //Setup Mappers Passenger File: FlightID -> PassengerID
        ArrayList<KeyValuePair> mappedPassengerOnFlights = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(mappedPassengerOnFlights, true);  //Setup Reducers with mapped data. Warn of any duplicates data (logical errors)
        ArrayList<KeyValuePair> reducedPassengerList = mReducerManager.executeAllReducerThreads();

        outputResults(reducedPassengerList);
        LOGGER.log(Level.INFO, "End Objective 3");
        ErrorManager.displayErrorSummary();
    }


    /**
     * Outputs the results formatted in a table-like form.
     * @param flights Reduced flight & passenger list.
     */
    private static void outputResults(ArrayList<KeyValuePair> flights) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n\n\n----------------------------\nObjective 3 Results - Total Passengers On Each Flight\n----------------------------\n");

        int lineCount = 0;
        for (KeyValuePair result: flights) {
            String appendSpace = "";
            if (result.getTotalReducedCount() < 10) //Add a 'leading space' to the number if under 10 (makes it '2 digits') so it formats correctly.
                appendSpace = " ";

            outputBuilder.append(result.getMapKey()).append(": ").append(appendSpace).append(result.getTotalReducedCount()).append("  |  ");
            if (++lineCount % 5 == 0) //Split every 10 lines
                outputBuilder.append("\n");

        }

        OutputFile.write(outputBuilder.toString()); //Write output to file.
        System.out.println(outputBuilder.toString()); //Write output to console.
    }
}
