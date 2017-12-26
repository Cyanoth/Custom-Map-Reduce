import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective2And3 {
    private static final Logger LOGGER = Logger.getLogger(Objective2And3.class.getName());

    private MapperManager mMapperManager;
    private ReducerManager mReducerManager;

    public int startObjective2_3() {
        LOGGER.log(Level.INFO, "Starting Objective 2");
        ParsedData parsedEntries = DataFileParser.parseAllFiles();
        mMapperManager = new MapperManager();
        mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllPassengers()), Keys.FlightID, Keys.PassengerID);
        ArrayList<KeyValuePair> mappedPassengerOnFlights = mMapperManager.executeAllMapperThreads();

        mReducerManager.createReducerObjects(mappedPassengerOnFlights, Reducer.Type.Concatenate);
        ArrayList<KeyValuePair> reducedPassengerList = mReducerManager.executeAllReducerThreads();
        ArrayList<KeyValuePair> totalPassengerCount = doObjective3(parsedEntries);

        outputResults(parsedEntries, reducedPassengerList, totalPassengerCount);

        return 0;
    }

    private ArrayList<KeyValuePair> doObjective3(ParsedData data) {
        mMapperManager = new MapperManager();
        mReducerManager = new ReducerManager();

        LOGGER.log(Level.INFO, "Rerunning Mappers & Reducers to get a total passenger count (objective 3)");
        mMapperManager.createMappers(new ArrayList<>(data.getAllPassengers()), Keys.FlightID, null);
        ArrayList<KeyValuePair> mappedPassengerCount = mMapperManager.executeAllMapperThreads();

        mReducerManager = new ReducerManager();
        mReducerManager.createReducerObjects(mappedPassengerCount, Reducer.Type.Count);
        return mReducerManager.executeAllReducerThreads();
    }

    private void outputResults(ParsedData data, ArrayList<KeyValuePair> flights, ArrayList<KeyValuePair> passengerCount) {
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

    private String formatPassengerID(String passengerValueList) {
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
