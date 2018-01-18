import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParsedData {
    private ArrayList<PassengerDetails> allPassengers = new ArrayList<>();
    private ArrayList<FlightDetails> allFlights = new ArrayList<>();
    private ArrayList<AirportDetails> allAirports = new ArrayList<>();

    /**
     * Add a validated PassengerDetails to data storage.
     * @param passenger A validated, parsed PassengerDetail object.
     */
    public void addPassenger(PassengerDetails passenger) { //Future Consideration: Duplicate Passenger/Flight could go here.
        allPassengers.add(passenger);
    }

    /**
     * Adds a validated FlightDetails to the data storage. Only stores one & check conflicting data.
     * @param flight A validated, parsed FlightDetails object.
     */
    public void addFlight(FlightDetails flight) {
        boolean flightAlreadyExists = false; //Ensure that only one FlightID store singularly (See Report)
        boolean conflictingData = false; //Ensure that if the flight already exists, the data matches (otherwise its confliction, this is further validation)
        for (FlightDetails existingFlight: allFlights) {
            if (existingFlight.getValueByName(Keys.FlightID) == flight.getValueByName(Keys.FlightID)) {
                flightAlreadyExists = true;

                /* Check if conflicting data with existing flight. */
                if (existingFlight.getValueByName(Keys.FromAirport) != flight.getValueByName(Keys.FromAirport))
                    conflictingData = true;
                else if (existingFlight.getValueByName(Keys.ToAirport) != flight.getValueByName(Keys.ToAirport))
                    conflictingData = true;
                else if (existingFlight.getValueByName(Keys.DepartureTime) != flight.getValueByName(Keys.DepartureTime))
                    conflictingData = true;
                else if (existingFlight.getValueByName(Keys.FlightTime) != flight.getValueByName(Keys.FlightTime))
                    conflictingData = true;

                break;
            }
        }

        if (conflictingData) //If conflict data occurred, skip line & generate error.
            ErrorManager.generateError("Conflicting Flight Data. Please check this. FlightID: " + flight.getValueByName(Keys.FlightID), ErrorType.Warning, ErrorKind.Parsing);

        if (!flightAlreadyExists && !conflictingData)
                allFlights.add(flight);

    }

    /**
     * Sets the AirportDetails arraylist to parsed airport entries.
     * @param airportEntries which have been Parsed & Validated AirportDetails
     */
    public void setAllAirports(ArrayList<AirportDetails> airportEntries) {
        allAirports = airportEntries;
    }

    /**
     * @return Stored AirportDetails from Parsed Data.
     */
    public ArrayList<AirportDetails> getAllAirports() {
        return allAirports;
    }

    /**
     * @return Stored FlightDetails from Parsed Data.
     */
    public ArrayList<FlightDetails> getAllFlights() {
        return allFlights;
    }

    /**
     * @return Stored PassengerDetails from Parsed Data.
     */
    public ArrayList<PassengerDetails> getAllPassengers() {
        return allPassengers;
    }

    /**
     * Get a FlightDetail object by searching through the objects and locating by FlightID
     * @param FlightID A string with a FlightID to search for
     * @return If found the object with a matching FlightID else it will return null.
     */
    public FlightDetails getFlightDetailsByID(String FlightID) {
        for (FlightDetails flight: allFlights)
            if (flight.getValueByName(Keys.FlightID) == FlightID)
                return flight;

        return null;
    }

    /**
     * Get a AirportDetails object by searching through the objects and locating by Airport Code
     * @param AirportCode A string with a AirportCode to search for
     * @return If found the object with a matching AirportCode else it will return null.
     */
    public AirportDetails getAirportDetailsByCode(String AirportCode) {
        for (AirportDetails airport: allAirports)
            if (airport.getValueByName(Keys.AirportCode).equals(AirportCode))
                return airport;
        return null;
    }
}
