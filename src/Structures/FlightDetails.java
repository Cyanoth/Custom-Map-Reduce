import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        ZonedDateTime formatDepartureTime = Instant.ofEpochMilli(departureTime * 1000).atZone(ZoneId.of("GMT"));
        return formatDepartureTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " (GMT)"; //Convert EPOCH time to GMT
    }

    public String getFormattedFlightTime() {
        int hours = flightTime / 60;
        int minutes = flightTime % 60;
        String appendMinutesHours = "";
        String appendMinutesZero = "";
        if (hours < 10)
            appendMinutesHours = "0";
        if (minutes < 10)
            appendMinutesZero = "0";

        return appendMinutesHours + hours + ":" + appendMinutesZero + minutes;
    }

    public String calculateArrivalTime() {
        ZonedDateTime calcArrivalTime = Instant.ofEpochMilli(departureTime * 1000).atZone(ZoneId.of("GMT"));
        calcArrivalTime = calcArrivalTime.plusMinutes(flightTime);
        return calcArrivalTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " (GMT)";
    }
}
