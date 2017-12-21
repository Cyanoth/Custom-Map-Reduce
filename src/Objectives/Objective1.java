import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//Objective 1 TODO: Get top 30 airports, output ones which HAVENT been used.
public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    private MapperManager mMapperManager;
    private ReducerManager mReducerManager;

    public int startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        ArrayList<PassengerEntry> parsedPassengerDataFile = DataFileParser.parsePassengerFile(Configuration.passengerDataFilePath); //Any IO Errors, Handled are in the function.
        ArrayList<AirportEntry> parsedAirportDataFile = DataFileParser.parseAirportFile(Configuration.airportDataFilePath); //Any IO Errors, Handled are in the function.

        mMapperManager = new MapperManager();
        mReducerManager = new ReducerManager();

        mMapperManager.setupMappers("fromAirport", parsedPassengerDataFile);
        ArrayList<KeyValuePair> totalMappedEntities = mMapperManager.executeAllMapperThreads();

        mReducerManager.setupReducerObjects(totalMappedEntities);
        ArrayList<KeyValuePair> reducedEntities = mReducerManager.executeAllReducerThreads();
        String unusedAirports = getUnusedAirports(parsedAirportDataFile, reducedEntities);
        outputResults(reducedEntities, unusedAirports);
        return 0;
    }

    private void outputResults(ArrayList<KeyValuePair> results, String unusedAirportResults) {
        System.out.println("\n----------------------------\n\t\tResults\n----------------------------");
        int lineCount = 0;
        for (KeyValuePair result: results) {
                System.out.print(result.asFormattedOutputString() + "  |  ");
                if (++lineCount % 5 == 0)
                    System.out.print("\n");

        }
        System.out.println("\nUnused Airports (from the Top-30 Airports):");
        System.out.println(unusedAirportResults);
    }

    private String getUnusedAirports(ArrayList<AirportEntry> airportEntries, ArrayList<KeyValuePair> reducedEntries)
    {
        StringBuilder builder = new StringBuilder();
        for (AirportEntry entry: airportEntries) { //Should try to optimise this.
            boolean contained = false;

            for (KeyValuePair reducedEntry: reducedEntries)
                if (entry.getAirportCode().equals(reducedEntry.getKey1())) {
                    contained = true;
                    break;
                }

            if (!contained)
                builder.append(entry.getAirportCode()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length()); //Get rid of the last comma (TODO: should ensure there is something to delete)
        return builder.toString();
    }
}
