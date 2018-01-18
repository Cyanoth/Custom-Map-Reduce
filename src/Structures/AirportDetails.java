import java.util.regex.Pattern;

/**
 * A data detail object that stores the data from a row of parsed data from the Top30 Airport File in memory.
 * Details are parsed from the DataParser to this object through the constructor and then the validation function is called
 * If a validation error occurs, this object is marked as having an error and is no longer considered part of the data file.
 */
public class AirportDetails extends AbstractDetails {
    private final String airportName;
    private final String airportCode;
    private final double latitude;
    private final double longitude;

     /**
     * Constructor for this Data Detail object. Parameters are passed through the file parser.
     * @param airportName A-Z (3 to 20 characters) String for the name of the Airport
     * @param airportCode A-Z (3 Characters) String for the Airport Code.
     * @param latitude Parse as a String but will be validated & converted to a double
     * @param longitude Parse as a String but will be validated & converted to a double
     * @param fromLineNumber Integer for the current line number from the Data File (Used for Error Explanation)
     */
    AirportDetails(String airportName, String airportCode, String latitude, String longitude, int fromLineNumber) {
        super.fromLineNumber = fromLineNumber;
        this.latitude = parseValidateLatitude(latitude);
        this.longitude = parseValidateLongitude(longitude);
        this.airportName = airportName;
        this.airportCode = airportCode;
        performPatternValidation();
    }

    /**
     * Performs various validation verification on the data. If an error occurs, it is sent to the parent
     * ErrorHandler where this object is marked as having an error and subsequently ignored.
     */
    @Override
    protected void performPatternValidation() {
        Pattern pattern_airportCode = Pattern.compile("[A-Z]{3}"); //Pattern XXX
        Pattern pattern_airportName = Pattern.compile("[A-Z]{3,20}"); //Pattern A-Z 3 to 20 length //TODO: Clarify whether I should Include space & forward-slash (Half-results are eliminated otherwise)

        if (!pattern_airportCode.matcher(this.airportCode).matches())
            super.handleError("Line: " + fromLineNumber + " Airport Code: '" + this.airportCode + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportName.matcher(this.airportName).matches())
            super.handleError("Line: " + fromLineNumber + " Airport Name: '" + this.airportName + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

    /**
     * Validates a Longitude
     * @param parsevalue
     * @return
     */
    private double parseValidateLongitude(String parsevalue) {
        try {
            double result = Double.parseDouble(parsevalue);

            if (result > -180.0 && result < 180.0)
                return result;
            else {
                super.handleError("Line: " + fromLineNumber + " Longitude: '" + parsevalue + "' is invalid! (It is not within range for longitude!)", ErrorType.Warning);
                return -1;
            }
        }
        catch (NumberFormatException e) {
            super.handleError("Line: " + fromLineNumber + " Longitude: '" + parsevalue + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
            return -1;
        }
    }

    private double parseValidateLatitude(String parsevalue) {
        try {
            double result = Double.parseDouble(parsevalue);

            if (result > -90.0 && result < 90.0)
                return result;
            else {
                super.handleError("Line: " + fromLineNumber + " Latitude: '" + parsevalue + "' is invalid! (It is not within range for longitude!)", ErrorType.Warning);
                return -1;
            }
        }
        catch (NumberFormatException e) {
            super.handleError("Line: " + fromLineNumber + " Latitude: '" + parsevalue + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
            return -1;
        }
    }


    @Override
    public Object getValueByName(Keys keyname) //Return string, long or int depending on keyType
    {
        switch (keyname) {
            case AirportName:
                return airportName;
            case AirportCode:
                return airportCode;
            case Latitude:
                return latitude;
            case Longitude:
                return longitude;
            default://This should never be hit due to enum restrictions.
                handleError("Warning! The code requested an invalid KeyName: " + keyname.toString(), ErrorType.Fatal);
                return "INVALID_KEY";
        }
    }

    public String getFormattedAirportNameCode() {
        return airportCode + " (" + airportName + ")";
    }
}
