import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class FlightDetails extends AbstractDetails {

    private final String flightID;
    private final String fromAirport;
    private final String toAirport;
    private final long departureTime;
    private final int flightTime;

    FlightDetails(String flightID, String fromAirport, String toAirport, String departureTime, String flightTime)
    {
        this.flightID = flightID;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureTime = parseValidateDepartureTime(departureTime);
        this.flightTime = parseValidateFlightTime(flightTime);
        performPatternValidation();
    }

    @Override
    protected void performPatternValidation() {
        Pattern pattern_flightID = Pattern.compile("[A-Z]{3}[0-9]{4}[A-Z]"); //Pattern: XXXnnnnX
        Pattern pattern_airportCode = Pattern.compile("[A-Z]{3}"); //Pattern XXX

        if (!pattern_flightID.matcher(this.flightID).matches())
            super.handleError("FlightID: '" + this.flightID + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportCode.matcher(this.fromAirport).matches())  //TODO: Check it exists in the Top30 Airport?
            super.handleError("FromAirport Code: '" + this.fromAirport + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportCode.matcher(this.toAirport).matches())  //TODO: Check it exists in the Top30 Airport?
            super.handleError("ToAirport Code: '" + this.toAirport + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

    private long parseValidateDepartureTime(String depatureTime) {
        try {
            Long result = Long.parseLong(depatureTime);

            if (result < 999999999 || result > 9999999999L) {
                super.handleError("Departure Time: '" + result + "' is invalid! (It is out of range!)", ErrorType.Warning);
                return -1;
            }
            else
                return result;
        }
        catch (NumberFormatException e) {
            super.handleError("Departure Time: '" + depatureTime + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
            return -1;
        }
    }

    private int parseValidateFlightTime(String flightTime) {
        try {
            int result = Integer.parseInt(flightTime);

            if (result < 0 || result > 9999) {
                super.handleError("Flight Time: " + result + "' is invalid! (It is out of range!)", ErrorType.Warning);
                return -1;
            }
            else
                return result;
        }
        catch (NumberFormatException e) {
            super.handleError("Flight Time: '" + flightTime + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
            return -1;
        }
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
            default://This should never be hit due to enum restrictions.
                handleError("Warning! The code requested an invalid KeyName: " + keyname.toString(), ErrorType.Fatal);
                return "INVALID_KEY";
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
