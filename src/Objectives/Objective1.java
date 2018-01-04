import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    public static void startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        ErrorManager.resetErrorManager();
        ParsedData parsedEntries = DataFileParser.parseAllFiles();
        if (ErrorManager.hasFatalErrorOccurred()) { return; }//Fatal Error Occurred, Cannot Continue.


        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllFlights()), Keys.FromAirport, null); //Just a counter.
        ArrayList<KeyValuePair> totalMappedEntities = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(totalMappedEntities, Reducer.Type.Count);
        ArrayList<KeyValuePair> reducedEntities = mReducerManager.executeAllReducerThreads();
        String unusedAirports = getUnusedAirports(parsedEntries.getAllAirports(), reducedEntities);
        outputResults(reducedEntities, unusedAirports);
        ErrorManager.displayErrorSummary();

    }

    private static void outputResults(ArrayList<KeyValuePair> results, String unusedAirportResults) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n----------------------------\nObjective 1 Results - Total Flights By Airports\n----------------------------\n");
        int lineCount = 0;
        for (KeyValuePair result: results) {
                outputBuilder.append(result.asFormattedOutputString() + "  |  ");
                if (++lineCount % 5 == 0)
                    outputBuilder.append("\n");

        }
        outputBuilder.append("\nUnused Airports (from the Top-30 Airports):");
        outputBuilder.append(unusedAirportResults);
        OutputFile.write(outputBuilder.toString());
        System.out.println(outputBuilder.toString());
    }

    private static String getUnusedAirports(ArrayList<AirportDetails> airportEntries, ArrayList<KeyValuePair> reducedEntries)
    {
        StringBuilder builder = new StringBuilder();
        for (AirportDetails entry: airportEntries) { //Should try to optimise this.
            boolean contained = false;

            for (KeyValuePair reducedEntry: reducedEntries)
                if (entry.getValueByName(Keys.AirportCode).equals(reducedEntry.getMapKey())) {
                    contained = true;
                    break;
                }

            if (!contained)
                builder.append(entry.getValueByName(Keys.AirportCode)).append(", ");
        }
        if (builder.length() > 3) //Prevent runtime error, in case of failure.
           builder.delete(builder.length() - 2, builder.length()); //Get rid of the last comma

        return builder.toString();

    }
}
