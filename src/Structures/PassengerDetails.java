import java.util.regex.Pattern;

/**
 * A data detail object that stores each Passenger in the the data is from ParsedData of the Passenger Data File.
 * Linked to a Flight via the FlightID. If there was an issue with the any Flight Info then this object won't be created anyway
 * Details are passed from the DataParser to this object through the constructor and then the validation function is called
 * If a validation error occurs, this object is marked as having an error and is no longer considered part of the data file.
 */
public class PassengerDetails extends AbstractDetails {
    private final String passengerID;
    private final String flightID;
    private FlightDetails flightDetails; //Optional for future use, can be used in the future to map passengers to airports or anything else.

    /**
     * Constructor for this Data Detail object. Parameters are passed through the file parser.
     * @param passengerID String for the ID of the Passenger.
     * @param flightID String for the ID of the Flight
     * @param fromLineNumber Integer for the current line number from the Data File (Used for Error Explanation)
    */
    PassengerDetails(String passengerID, String flightID, int fromLineNumber) {
        super.fromLineNumber = fromLineNumber;
        this.passengerID = passengerID;
        this.flightID = flightID;
        performPatternValidation();
    }

    /**
     * Optional function that can be used in the future to Map-Reducer by allow different Keys unrelated to the assignment objectives.
     * @param details Parsed FlightDetails linked via the 'FlightID'
     */
    public void linkFlightDetails(FlightDetails details) {
        flightDetails = details;
    }

    /**
     * Performs various validation verification on the data. If an error occurs, it is sent to the parent
     * ErrorHandler where this object is marked as having an error and subsequently ignored.
     */
    @Override
    protected void performPatternValidation() {
        Pattern pattern_passengerID = Pattern.compile("[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]"); //Pattern: XXXnnnnXXn
        Pattern pattern_flightID = Pattern.compile("[A-Z]{3}[0-9]{4}[A-Z]"); //Pattern: XXXnnnnX

        if (!pattern_passengerID.matcher(this.passengerID).matches()) //If passengerID does not match the pattern
            super.handleError("Line: " + fromLineNumber + " PassengerID: + '" + this.passengerID + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_flightID.matcher(this.flightID).matches())
            super.handleError("Line: " + fromLineNumber + " FlightID: '" + this.flightID + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

    /**
     * @param keyname Get the value of a Key (see Enum: Keys)
     * @return The value stored in the relevant Key.
     */
    @Override
    public String getValueByName(Keys keyname) //Return string, long or int depending on keyType
    {
        switch (keyname) {
            case PassengerID:
                return passengerID;
            case FlightID:
                return flightID;
            default: //Should not be hit, will only occur if code requests a key not related to the details object.
               handleError("Warning! The code requested an invalid KeyName: " + keyname.toString(), ErrorType.Fatal); //Fatal since this is potentially a code issue and may generate invalid results.
               return "INVALID_KEY";
        }
    }
}
