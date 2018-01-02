import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParsedData {
    private static final Logger LOGGER = Logger.getLogger(DataFileParser.class.getName());

    private ArrayList<PassengerDetails> allPassengers = new ArrayList<>();
    private ArrayList<FlightDetails> allFlights = new ArrayList<>();
    private ArrayList<AirportDetails> allAirports = new ArrayList<>();

    public void addPassenger(PassengerDetails passenger) { //Future Consideration: Duplicate Passenger/Flight could go here.
        allPassengers.add(passenger);
    }

    public void addFlight(FlightDetails flight) {
        boolean flightAlreadyExists = false; //Structure relies on one entry per flight
        for (FlightDetails existingFlight: allFlights) {
            if (existingFlight.getValueByName(Keys.FlightID) == flight.getValueByName(Keys.FlightID)) {
                flightAlreadyExists = true;
                break;
                //TODO: Validation will go here, check that the new flight matches the existing flight details. If it doesn't there is a conflict of data and will need to throw an error.
            }
        }

        if (!flightAlreadyExists)
                allFlights.add(flight);

    }

    public void setAllAirports(ArrayList<AirportDetails> airportEntries) {
        allAirports = airportEntries;
    }

    public ArrayList<AirportDetails> getAllAirports() {
        return allAirports;
    }

    public ArrayList<FlightDetails> getAllFlights() {
        return allFlights;
    }

    public ArrayList<PassengerDetails> getAllPassengers() {
        return allPassengers;
    }

    public FlightDetails getFlightDetailsByID(String FlightID) {
        for (FlightDetails flight: allFlights)
            if (flight.getValueByName(Keys.FlightID) == FlightID)
                return flight;

        return null;
    }

    public AirportDetails getAirportDetailsByCode(String AirportCode) {
        for (AirportDetails airport: allAirports)
            if (airport.getValueByName(Keys.AirportCode).equals(AirportCode))
                return airport;
        return null;
    }
}
