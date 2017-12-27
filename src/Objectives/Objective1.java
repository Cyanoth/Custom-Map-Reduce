import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    public static int startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        ParsedData parsedEntries = DataFileParser.parseAllFiles();


        if (ErrorManager.hasFatalErrorOccurred()) //Fatal Parsing Occurred Error, Cannot Continue Map/Reduce Functions.
            return -1;

        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllFlights()), Keys.FromAirport, null); //Just a counter.
        ArrayList<KeyValuePair> totalMappedEntities = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(totalMappedEntities, Reducer.Type.Count);
        ArrayList<KeyValuePair> reducedEntities = mReducerManager.executeAllReducerThreads();
        String unusedAirports = getUnusedAirports(parsedEntries.getAllAirports(), reducedEntities);
        outputResults(reducedEntities, unusedAirports);
        return 0;
    }

    private static void outputResults(ArrayList<KeyValuePair> results, String unusedAirportResults) {
        System.out.println("\n----------------------------\n\t\tResults: Total Airports\n----------------------------");
        int lineCount = 0;
        for (KeyValuePair result: results) {
                System.out.print(result.asFormattedOutputString() + "  |  ");
                if (++lineCount % 5 == 0)
                    System.out.print("\n");

        }
        System.out.println("\nUnused Airports (from the Top-30 Airports):");
        System.out.println(unusedAirportResults);
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
        builder.delete(builder.length() - 2, builder.length()); //Get rid of the last comma (TODO: should ensure there is something to delete)
        return builder.toString();

    }
}
