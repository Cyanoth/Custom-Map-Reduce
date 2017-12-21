import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataFileParser {
    private static final Logger LOGGER = Logger.getLogger(DataFileParser.class.getName());

    public static ArrayList<PassengerEntry> parsePassengerFile(String path) {
        LOGGER.log(Level.FINE, "Parsing Data File: " + path);
        ArrayList<PassengerEntry> parsedEntries = new ArrayList<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            String currentLine;

            while ((currentLine = csvReader.readLine()) != null) {
                String[] sl = currentLine.split(",");
                if (sl.length < 6)
                    System.out.println("Invalid amount of arguments!"); //TODO: Handle This Correctly.
                else
                    parsedEntries.add(new PassengerEntry(sl[0], sl[1], sl[2], sl[3], sl[4], sl[5]));
            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Error has occurred with the data file");//TODO: Handle this efficently.
            e.printStackTrace();
        }

        LOGGER.log(Level.FINE, "Amount of entries parsed from the data file: " + parsedEntries.size());
        LOGGER.log(Level.FINE, "Parsed Data File Successfully!");
        return parsedEntries;
    }

}
