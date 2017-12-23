import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Validation & Logging Output
public class PassengerEntry {
    private static final Logger LOGGER = Logger.getLogger(PassengerEntry.class.getName());

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

    public Object getValueByName(String keyName) //Return string, long or int depending on keyType
    {
        switch (keyName) {
            case "passengerID":
                return passengerId;
            case "flightID":
                return flightId;
            case "fromAirport":
                return fromAirport;
            case "toAirport":
                return toAirport;
            case "departureTime":
                return departureTime;
            case "flightTime":
                return flightTime;
            default:
                LOGGER.log(Level.SEVERE, "Warning! Code requested an invalid keyname: " + keyName);
                return "INVALIDKEY";
        }
    }

    public void handleParsingError()
    {
        System.out.println("Parsing Error! ");
    }

    //TODO: Add a tostring function
}
