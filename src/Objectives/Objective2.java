import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective2 {
    private static final Logger LOGGER = Logger.getLogger(Objective2.class.getName());

    public int startObjective2() {
        LOGGER.log(Level.INFO, "Starting Objective 2");
        ParsedData parsedEntries = DataFileParser.parseAllFiles();

        MapperManager mMapperManager = new MapperManager();
        ReducerManager mReducerManager = new ReducerManager();

        mMapperManager.createMappers(new ArrayList<>(parsedEntries.getAllPassengers()), Keys.FlightID, Keys.PassengerID);
        ArrayList<KeyValuePair> mappedPassengerOnFlights = mMapperManager.executeAllMapperThreads();
        mReducerManager.createReducerObjects(mappedPassengerOnFlights, Reducer.Type.Concatenate);
        ArrayList<KeyValuePair> reducedEntities = mReducerManager.executeAllReducerThreads();
        outputResults(parsedEntries, reducedEntities);
        return 0;
    }

    private void outputResults(ParsedData data, ArrayList<KeyValuePair> flights) {
        System.out.println("\n----------------------------\n\t\tResults\n----------------------------");
        for (KeyValuePair result: flights) {
            FlightDetails flightDetails = data.getFlightDetailsByID((String) result.getMapKey());
            System.out.println("Flight ID: " + result.getMapKey());
            System.out.println("\tTotal Passengers: 0");

            System.out.print("\tPassenger ID's: ");
                System.out.println(formatPassengerID((String) result.getMapValue()));

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
