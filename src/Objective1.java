import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());

    public static void startObjective1()
    {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        String passengerDataFile = ("/home/charlie/Documents/MapReduce-AdvComp/Data//AComp_Passenger_data_no_error.csv");

        try {
            ArrayList<PassengerEntry> mPassengers = parseDataFile(passengerDataFile);
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Handle more efficiently.
        }

    }

    private static ArrayList<PassengerEntry> parseDataFile(String path) throws IOException {
        LOGGER.log(Level.FINE, "Parsing Data File: " + path);
        ArrayList<PassengerEntry> parsedEntries = new ArrayList<PassengerEntry>();

        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String currentLine;

        while ((currentLine = csvReader.readLine()) != null) {
            String[] sl = currentLine.split(",");
            if (sl.length < 6)
                System.out.println("Invalid amount of arguments!"); //TODO: Handle This Correctly.
            else
                parsedEntries.add(new PassengerEntry(sl[0], sl[1], sl[2], sl[3], sl[4], sl[5]));
        }

        LOGGER.log(Level.FINE, "Parsed Data File Successfully!");
        return parsedEntries;
    }

}
