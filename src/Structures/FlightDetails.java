import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightDetails extends AbstractDetails {
    private static final Logger LOGGER = Logger.getLogger(FlightDetails.class.getName());

    private final String flightID;
    private final String fromAirport;
    private final String toAirport;
    private final long departureTime;
    private final int flightTime;

    public FlightDetails(String flightID, String fromAirport, String toAirport, String departureTime, String flightTime) //TODO: Poly this with the correct args.
    {
        //TODO: Add validation
        this.flightID = flightID;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureTime = Long.parseLong(departureTime); //validation check
        this.flightTime = Integer.parseInt(flightTime); // validation check
    }

    @Override
    public Object getValueByName(Keys keyname) //Return string, long or int depending on keyType
    {
        switch (keyname) {
            case FlightID:
                return flightID;
            case FromAirport:
                return fromAirport;
            case ToAirport:
                return toAirport;
            case DepartureTime:
                return departureTime;
            case FlightTime:
                return flightTime;
            default:
                LOGGER.log(Level.SEVERE, "Warning! Code requested an invalid KeyName: " + keyname.toString());
                return "INVALIDKEY";
        }
    }

    public String getFormattedDepartureTime() {
        return "00:22:11";
    }

    public String getFormattedFlightTime() {
        return "10 Minutes";
    }

    public String calculateArrivalTime() {
        return "22:33:44";
    }

    public void handleParsingError()
    {
        System.out.println("Parsing Error! ");
    }
}
