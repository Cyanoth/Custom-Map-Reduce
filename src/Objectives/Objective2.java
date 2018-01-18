import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Objective 2 of the assignment.
 *  Create a list of flights based on id including passenger ID's, airport codes, formatted flight times.
 */
public class Objective2 {
    private static final Logger LOGGER = Logger.getLogger(Objective2.class.getName());

    /**
     *  The initial function for executing functions necessary to complete Objective 2.
     */
    public static void startObjective2() {
        LOGGER.log(Level.INFO, "Starting Objective 2");
        ErrorManager.resetErrorManager(); //Clear any previous errors if previous any objectives have ran.
        ParsedData parsedEntries = DataFileParser.parseAllFiles(); //Call DataParse to parse the Passenger & Top30 Data Files.
        if (ErrorManager.hasFatalErrorOccurred() || parsedEntries == null) { return; }//Fatal Error Occurred, Cannot Continue.

        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllPassengers()), Keys.FlightID, Keys.PassengerID);  //Setup Mappers Passenger File: FlightID -> PassengerID
        ArrayList<KeyValuePair> mappedPassengerOnFlights = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(mappedPassengerOnFlights, true); //Setup Reducers with mapped data. Warn of any duplicates data (logical errors)
        ArrayList<KeyValuePair> reducedPassengerList = mReducerManager.executeAllReducerThreads();

        outputResults(parsedEntries, reducedPassengerList);
        LOGGER.log(Level.INFO, "End Objective 2");
        ErrorManager.displayErrorSummary();
    }


    /**
     * Outputs the results formatted in a list-tabulated form.
     * @param data Parsed Data file, used to retrieve Airport Codes & Timings for each FlightID.
     * @param flights Reduced flight & passenger list.
     */
    private static void outputResults(ParsedData data, ArrayList<KeyValuePair> flights) {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n\n\n----------------------------\nObjective 2 & 3 Results - List of Flights based on Flight ID\n----------------------------\n");
        for (int i = 0; i < flights.size(); i++ ) { //For each flight in the reduced entry.
            KeyValuePair reducedPassengerEntries = flights.get(i);

            FlightDetails flightDetails = data.getFlightDetailsByID((String) reducedPassengerEntries.getMapKey());
            outputBuilder.append("Flight ID: " + reducedPassengerEntries.getMapKey() + "\n");
            outputBuilder.append("\tTotal Passengers: " + reducedPassengerEntries.getTotalReducedCount() + "\n");

            outputBuilder.append("\tPassenger ID's: ");
            outputBuilder.append(formatPassengerID((String) reducedPassengerEntries.getMapValue()) + "\n");

            outputBuilder.append("\tOriginating From Airport (IATA/FAA): "  + data.getAirportDetailsByCode((String) flightDetails.getValueByName(Keys.FromAirport)).getFormattedAirportNameCode() + "\n");
            outputBuilder.append("\tDestination To Airport (IATA/FAA): " + data.getAirportDetailsByCode((String) flightDetails.getValueByName(Keys.ToAirport)).getFormattedAirportNameCode() + "\n");
            outputBuilder.append("\tDeparture Time: " + flightDetails.getFormattedDepartureTime() + "\n");
            outputBuilder.append("\tArrival Time: " + flightDetails.calculateArrivalTime() + "\n");
            outputBuilder.append("\tTotal Flight Time: " + flightDetails.getFormattedFlightTime() + "\n");
            outputBuilder.append("\n");
        }
        OutputFile.write(outputBuilder.toString()); //Write output to file.
        System.out.println(outputBuilder.toString()); //Write output to console.
    }

    /**
     * Formats the passenger id's so they don't appear all on one line and a formatted correctly.
     * @param passengerValueList Single String containing a list of Passenger ID's
     * @return A formatted string, with line breaks and tabs.
     */
    private static String formatPassengerID(String passengerValueList) {
        final int splitNOccurrence = 10;
        String[] splitString = passengerValueList.split("\\s");
        StringBuilder formattedString = new StringBuilder();

        for (int i = 0; i < splitString.length; i++) { //For every nth (10) entry, split into a new line.
            if (i % splitNOccurrence == 0 && i != 0) //Tab each new line (pretty-formatting)
                formattedString.append("\n\t\t\t\t\t");
            formattedString.append(" ").append(splitString[i]);
        }
        return formattedString.toString();
    }

}
