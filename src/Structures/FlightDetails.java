import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * A data detail object that stores FlightDetails for one FlightID. The data is from ParsedData of the Passenger Data File.
 * Details are passed from the DataParser to this object through the constructor and then the validation function is called
 * If a validation error occurs, this object is marked as having an error and is no longer considered part of the data file.
 */
public class FlightDetails extends AbstractDetails {
    private final String flightID;
    private final String fromAirport;
    private final String toAirport;
    private final long departureTime;
    private final int flightTime;

    /**
     * Constructor for this Data Detail object. Parameters are passed through the file parser.
     * @param flightID String for the ID of the Flight
     * @param fromAirport String for the Airport Code for departure airport.
     * @param toAirport String for the Airport Code for destination airport.
     * @param departureTime Parse as a String but will be validated & converted to a long. As epoch time.
     * @param flightTime Parse as a String but will be validated & converted to a integer.
     * @param curData Uses the currently Parsed Top30 Airport Data file and checks that the Airport Codes Exist.
     * @param fromLineNumber Integer for the current line number from the Data File (Used for Error Explanation)
     */
    FlightDetails(String flightID, String fromAirport, String toAirport, String departureTime, String flightTime, ParsedData curData, int fromLineNumber)
    {
        super.fromLineNumber = fromLineNumber;
        this.flightID = flightID;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureTime = parseValidateDepartureTime(departureTime);

        if (isValid()) //Only if Departure Time was valid, continue validation checking.
            this.flightTime = parseValidateFlightTime(flightTime);
        else
            this.flightTime = -1;

        if (isValid()) //Only if Flight Time was valid, continue validation checking.
            performPatternValidation();

        if (curData != null && isValid()) //Check that the Airport Codes exist in the Top30 Airport Data File.
            performCheckAirportExists(curData);

    }

    /**
     * Performs various validation verification on the data. If an error occurs, it is sent to the parent
     * ErrorHandler where this object is marked as having an error and subsequently ignored.
     */
    @Override
    protected void performPatternValidation() {
        Pattern pattern_flightID = Pattern.compile("[A-Z]{3}[0-9]{4}[A-Z]"); //Pattern: XXXnnnnX
        Pattern pattern_airportCode = Pattern.compile("[A-Z]{3}"); //Pattern XXX

        if (!pattern_flightID.matcher(this.flightID).matches())
            super.handleError("Line: " + fromLineNumber + " FlightID: '" + this.flightID + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportCode.matcher(this.fromAirport).matches())
            super.handleError("Line: " + fromLineNumber + " FromAirport Code: '" + this.fromAirport + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportCode.matcher(this.toAirport).matches())
            super.handleError("Line: " + fromLineNumber + " ToAirport Code: '" + this.toAirport + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

    /**
     * Checks that the From & To Airport Code exists in the Parsed Top30 Airport File.
     * If it doesn't then generate an error.
     * @param curData Parsed Data that contains the Top30 Airport Data.
     */
    private void performCheckAirportExists(ParsedData curData) {
        if (curData.getAirportDetailsByCode(fromAirport) == null) //Check Departing airport code exists
            super.handleError("Line: " + fromLineNumber + " FromAirport Code: '" + this.fromAirport + "' is invalid! (It does not exist in the Top30 File!)", ErrorType.Warning);

        else if (curData.getAirportDetailsByCode(toAirport) == null) //Check arrival airport code exists
            super.handleError("Line: " + fromLineNumber + " ToAirport Code: '" + this.toAirport + "' is invalid! (It does not exist in the Top30 File!)", ErrorType.Warning);

    }

    /**
     * Validates a Departure Time String and converts the result to a long.
     * @param depatureTime The Departure Time (epoch time) String which has been parsed from the DataFile.
     * @return  If successful, return it as a long. Otherwise generate error and return -1.
     */
    private long parseValidateDepartureTime(String depatureTime) {
        try { //Try to convert String to Long. If it fails goto 'catch'
            Long result = Long.parseLong(depatureTime);

            if (result < 999999999 || result > 9999999999L) { //This is the range in the specification.
                super.handleError("Line: " + fromLineNumber + " Departure Time: '" + result + "' is invalid! (It is out of range!)", ErrorType.Warning);
                return -1;
            }
            else
                return result;
        }
        catch (NumberFormatException e) { //Couldn't convert the string to long.
            super.handleError("Line: " + fromLineNumber + " Departure Time: '" + depatureTime + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
            return -1;
        }
    }

    /**
     * Validates a Flight Time String and converts the result to a int.
     * @param flightTime The Flight Time (minutes) String which has been parsed from the DataFile.
     * @return  If successful, return it as a int. Otherwise generate error and return -1.
     */
    private int parseValidateFlightTime(String flightTime) {
        try { //Try to convert String to int. If it fails goto 'catch'
            int result = Integer.parseInt(flightTime);

            if (result < 0 || result > 9999) { //This is the range in the specification.
                super.handleError("Line: " + fromLineNumber + " Flight Time: " + result + "' is invalid! (It is out of range!)", ErrorType.Warning);
                return -1;
            }
            else
                return result;
        }
        catch (NumberFormatException e) {  //Couldn't convert the string to int.
            super.handleError("Line: " + fromLineNumber + " Flight Time: '" + flightTime + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
            return -1;
        }
    }


    /**
     * @param keyname Get the value of a Key (see Enum: Keys)
     * @return The value stored in the relevant Key.
     */
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
            default://Should not be hit, will only occur if code requests a key not related to the details object.
                handleError("Warning! The code requested an invalid KeyName: " + keyname.toString(), ErrorType.Fatal); //Fatal since this is potentially a code issue and may generate invalid results.
                return "INVALID_KEY";
        }
    }

    /**
     * Converts the epoch time into a formatted GMT Time as requested by the specification
     * Reference in report [9]
     * @return A string which is a human-readable GMT format conversion of the epoch time.
     */
    public String getFormattedDepartureTime() {
        ZonedDateTime formatDepartureTime = Instant.ofEpochMilli(departureTime * 1000).atZone(ZoneId.of("GMT"));
        return formatDepartureTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " (GMT)"; //Convert EPOCH time to GMT
    }

    /**
     * Converts the FlightTime from Minutes to HH:MM
     * @return A string which is human-readable form of the FlightTime.
     */
    public String getFormattedFlightTime() {
        int hours = flightTime / 60;
        int minutes = flightTime % 60;
        String appendMinutesHours = "";
        String appendMinutesZero = "";
        if (hours < 10) //Prefix zero if below 10 hours
            appendMinutesHours = "0";
        if (minutes < 10) //Prefix zero if below 10 minutes
            appendMinutesZero = "0";

        return appendMinutesHours + hours + ":" + appendMinutesZero + minutes;
    }

    /**
     * Adds the FlightTime minutes to the depature time epoch time to calculate the arrival time.
     * @return A string which contains the calculated arrival time of the flight.
     */
    public String calculateArrivalTime() {
        ZonedDateTime calcArrivalTime = Instant.ofEpochMilli(departureTime * 1000).atZone(ZoneId.of("GMT"));
        calcArrivalTime = calcArrivalTime.plusMinutes(flightTime);
        return calcArrivalTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " (GMT)"; //Convert EPOCH time to GMT
    }
}
