import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Validation & Logging Output
public class PassengerEntry {
    private static final Logger LOGGER = Logger.getLogger(PassengerEntry.class.getName());

    public enum Keys { PassengerID, FlightID, FromAirport, ToAirport, DepartureTime, FlightTime; }

    private String passengerId;
    private String flightId;
    private String fromAirport;
    private String toAirport;
    private long departureTime;
    private int flightTime;

    public PassengerEntry(String passengerId, String flightId, String fromAirport, String toAirport, String departureTime, String flightTime) //TODO: Poly this with the correct args.
    {
        //TODO: Add validation
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureTime = Long.parseLong(departureTime); //validation check
        this.flightTime = Integer.parseInt(flightTime); // validation check
    }

    public Object getValueByName(Keys keyname) //Return string, long or int depending on keyType
    {
        switch (keyname) {
            case PassengerID:
                return passengerId;
            case FlightID:
                return flightId;
            case FromAirport:
                return fromAirport;
            case ToAirport:
                return toAirport;
            case DepartureTime:
                return departureTime;
            case FlightTime:
                return flightTime;
            default://This should never be hit.
                LOGGER.log(Level.SEVERE, "Warning! Code requested an invalid KeyName: " + keyname.toString());
                return "INVALIDKEY";
        }
    }

    public void handleParsingError()
    {
        System.out.println("Parsing Error! ");
    }

    //TODO: Add a tostring function
}
