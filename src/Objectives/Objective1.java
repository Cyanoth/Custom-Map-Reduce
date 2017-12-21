import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());

    private MapperManager mMapperManager;
    private ReducerManager mReducerManager;

    public int startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        ArrayList<PassengerEntry> mParsedPassengerDataFile = DataFileParser.parsePassengerFile(Configuration.passengerDataFilePath); //Any IO Errors, Handled are in the function.
        mMapperManager = new MapperManager();
        mReducerManager = new ReducerManager();

        mMapperManager.setupMappers("fromAirport", mParsedPassengerDataFile);
        ArrayList<KeyValuePair> totalMappedEntities = mMapperManager.executeAllMapperThreads();

        mReducerManager.setupReducerObjects(totalMappedEntities);
        ArrayList<KeyValuePair> reducedEntities = mReducerManager.executeAllReducerThreads();
        outputResults(reducedEntities);
        return 0;
    }

    private void outputResults(ArrayList<KeyValuePair> results) {
        System.out.println("\n----------------------------\n\t\tResults\n----------------------------");
        int lineCount = 0;

        for (KeyValuePair result: results) {
                System.out.print(result.asFormattedOutputString() + "  |  ");
                if (++lineCount % 5 == 0)
                    System.out.print("\n");

        }
    }
}
