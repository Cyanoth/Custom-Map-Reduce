import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Objective 1 of the assignment.
 *  Determines the number of flights from each airport including any unused AirPorts.
 */
public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    /**
     *  The initial function for executing functions necessary to complete Objective 1.
     */
    public static void startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        ErrorManager.resetErrorManager(); //Clear any previous errors if previous any objectives have ran.
        ParsedData parsedEntries = DataFileParser.parseAllFiles(); //Call DataParse to parse the Passenger & Top30 Data Files.
        if (ErrorManager.hasFatalErrorOccurred() || parsedEntries == null) { return; }//Fatal Error Occurred, Cannot Continue.

        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllFlights()), Keys.FromAirport, Keys.FlightID); //Setup Mappers Passenger File: FromAirport -> FlightID
        ArrayList<KeyValuePair> totalMappedEntities = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(totalMappedEntities, false); //Setup Reducers with mapped data. Do not warn of 'duplicates' (unnecessary for this objective)
        ArrayList<KeyValuePair> reducedEntities = mReducerManager.executeAllReducerThreads();

        String unusedAirports = getUnusedAirports(parsedEntries.getAllAirports(), reducedEntities); //Using reduced data, find unused Airports from Top30 data file.
        outputResults(reducedEntities, unusedAirports);
        LOGGER.log(Level.INFO, "End Objective 1");
        ErrorManager.displayErrorSummary();

    }

    /**
     * Outputs the results formatted in a table-like form.
     * @param results The reduced entries from Objective 1
     * @param unusedAirportResults A single string containing unused airport codes.
     */
    private static void outputResults(ArrayList<KeyValuePair> results, String unusedAirportResults) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n----------------------------\nObjective 1 Results - Total flights originating from each Airport\n----------------------------\n");
        int lineCount = 0;
        for (KeyValuePair result: results) {
                outputBuilder.append(result.getMapKey()).append(": ").append(result.getTotalReducedCount()).append("  |  ");
                if (++lineCount % 5 == 0) //For every 5th item, create a new line.
                    outputBuilder.append("\n");

        }
        outputBuilder.append("\n\nUnused Airports (from the Top-30 Airports): ");
        outputBuilder.append(unusedAirportResults);

        OutputFile.write(outputBuilder.toString()); //Write output to file.
        System.out.println(outputBuilder.toString()); //Write output to console.
    }

    /**
     * Compares the list of reduced single airport codes with the Top30 entries.
     * Any entries listed in Top30 but not in the reduced data are 'unused'
     * @param airportEntries A list of all airport codes in the Top30 Data File.
     * @param reducedEntries Airport Codes which were the results from Objective 1.
     * @return A single-line string that has a list of unused airports.
     */
    private static String getUnusedAirports(ArrayList<AirportDetails> airportEntries, ArrayList<KeyValuePair> reducedEntries)
    {
        StringBuilder builder = new StringBuilder();
        for (AirportDetails entry: airportEntries) { //For each entry in the Top30 Data File
            boolean contained = false;
            for (KeyValuePair reducedEntry: reducedEntries) //See if it has at least one result, break if it does.
                if (entry.getValueByName(Keys.AirportCode).equals(reducedEntry.getMapKey())) {
                    contained = true;
                    break;
                }

            if (!contained) //If it doesn't then append the airport code to the single string.
                builder.append(entry.getValueByName(Keys.AirportCode)).append(", ");
        }
        if (builder.length() > 3) //Prevents a runtime error, in case of failure no entries
           builder.delete(builder.length() - 2, builder.length()); //Remove the final of the last comma

        return builder.toString();

    }
}
