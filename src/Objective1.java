import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective1 {
    private static final Logger LOGGER = Logger.getLogger(Objective1.class.getName());
    private static ArrayList<KeyValuePair> totalMappedEntites = new ArrayList<>();
    //Mappers Global.
    //Reducers Global.
    //String - Output String.

    public static int startObjective1() {
        LOGGER.log(Level.INFO, "Starting Objective 1");
        String passengerDataFile = ("/home/charlie/Documents/MapReduce-AdvComp/Data/AComp_Passenger_data_no_error.csv");
        ArrayList<PassengerEntry> parsedPassengerDataFile;

        try {
            parsedPassengerDataFile = parseDataFile(passengerDataFile);
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Handle more efficiently.
            return -1;
        }

        int chunksize = 50;
        int sizeOfEntries = parsedPassengerDataFile.size();

        ArrayList<Mapper> initMappers = new ArrayList<>();
        ArrayList<PassengerEntry> tempDataChunk = new ArrayList<>();

        for (int i = 0; i < sizeOfEntries; i++) {
            if (i % chunksize == 0 && i != 0) {
                Mapper singleMapper = new Mapper(tempDataChunk, "FromAirport", initMappers.size());
                initMappers.add(singleMapper);
                tempDataChunk = new ArrayList<>();
            }
            else {
                tempDataChunk.add(parsedPassengerDataFile.get(i));
            }
        }

        //!!! Missing 58 Entries.
        for (Mapper singleMapper : initMappers) { //Execute Each Mapper
            try {

                totalMappedEntites.addAll(singleMapper.call());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(totalMappedEntites); //Sort & Shuffle the Mapped Entries //TODO: Add a time which tracks how long the sorting algorithm took.

        KeyValuePair[] allPairs = (KeyValuePair[])  totalMappedEntites.toArray();

        LOGGER.log(Level.FINE, "All Pairs: " + Arrays.toString(allPairs));



        return 0;
    }

    private static ArrayList<PassengerEntry> parseDataFile(String path) throws IOException {
        LOGGER.log(Level.FINE, "Parsing Data File: " + path);
        ArrayList<PassengerEntry> parsedEntries = new ArrayList<>();

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

    private static void reduceEntites()
    {

    }

    private static void outputResults() {

    }

}
