import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective2 {
    private static final Logger LOGGER = Logger.getLogger(Objective2.class.getName());

    public static void startObjective2() {
        LOGGER.log(Level.INFO, "Starting Objective 2");
        ErrorManager.resetErrorManager();
        ParsedData parsedEntries = DataFileParser.parseAllFiles();
        if (ErrorManager.hasFatalErrorOccurred()) { return; }//Fatal Error Occurred, Cannot Continue.


        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllPassengers()), Keys.FlightID, Keys.PassengerID);
        ArrayList<KeyValuePair> mappedPassengerOnFlights = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(mappedPassengerOnFlights, Reducer.Type.Concatenate);
        ArrayList<KeyValuePair> reducedPassengerList = mReducerManager.executeAllReducerThreads();
        ArrayList<KeyValuePair> totalPassengerCount = Objective3.startObjective3(parsedEntries, true);

        outputResults(parsedEntries, reducedPassengerList, totalPassengerCount);
        ErrorManager.displayErrorSummary();
    }



    private static void outputResults(ParsedData data, ArrayList<KeyValuePair> flights, ArrayList<KeyValuePair> passengerCount) {
        System.out.println("\n----------------------------\n\t\tResults\n----------------------------");
        for (int i = 0; i < flights.size(); i++ ) {
            KeyValuePair reducedPassengerEntries = flights.get(i);
            KeyValuePair reducedPassengerCount = passengerCount.get(i);

            if (reducedPassengerEntries.getMapKey() != reducedPassengerCount.getMapKey()) {
                LOGGER.log(Level.SEVERE, "ABORT: Unknown Error - This error can only appear if reducers have mapped different data?");
                return;
            }

            FlightDetails flightDetails = data.getFlightDetailsByID((String) reducedPassengerEntries.getMapKey());
            System.out.println("Flight ID: " + reducedPassengerEntries.getMapKey());
            System.out.println("\tTotal Passengers: " + reducedPassengerCount.getMapValue());

            System.out.print("\tPassenger ID's: ");
                System.out.println(formatPassengerID((String) reducedPassengerEntries.getMapValue()));

            System.out.println("\tOriginating From Airport (IATA/FAA): "  + data.getAirportDetailsByCode((String) flightDetails.getValueByName(Keys.FromAirport)).getFormattedairportNameCode());
            System.out.println("\tDestination To Airport (IATA/FAA): " + data.getAirportDetailsByCode((String) flightDetails.getValueByName(Keys.ToAirport)).getFormattedairportNameCode());
            System.out.println("\tDeparture Time: " + flightDetails.getFormattedDepartureTime());
            System.out.println("\tArrival Time: " + flightDetails.calculateArrivalTime());
            System.out.println("\tTotal Flight Time: " + flightDetails.getFormattedFlightTime());
            System.out.println("\n");

        }
    }

    private static String formatPassengerID(String passengerValueList) {
        final int splitNOccurrence = 10;
        String[] splitString = passengerValueList.split("\\s");
        StringBuilder formattedString = new StringBuilder();

        for (int i = 0; i < splitString.length; i++) {
            if (i % splitNOccurrence == 0 && i != 0)
                formattedString.append("\n\t\t\t\t\t");
            formattedString.append(" ").append(splitString[i]);
        }
        return formattedString.toString();
    }

}
