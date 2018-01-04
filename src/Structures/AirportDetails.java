import java.util.regex.Pattern;

public class AirportDetails extends AbstractDetails {
    private final String airportName;
    private final String airportCode;
    private final double latitude;
    private final double longitude;

    AirportDetails(String airportName, String airportCode, String latitude, String longitude, int fromLineNumber) {
        super.fromLineNumber = fromLineNumber;
        this.latitude = parseValidateLatitude(latitude);
        this.longitude = parseValidateLongitude(longitude);
        this.airportName = airportName;
        this.airportCode = airportCode;
        performPatternValidation();
    }

    @Override
    protected void performPatternValidation() {
        Pattern pattern_airportCode = Pattern.compile("[A-Z]{3}"); //Pattern XXX
        Pattern pattern_airportName = Pattern.compile("[A-Z\\s\\/]{3,20}"); //Pattern A-Z 3 to 20 length

        if (!pattern_airportCode.matcher(this.airportCode).matches())
            super.handleError("Line: " + fromLineNumber + " Airport Code: '" + this.airportCode + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportName.matcher(this.airportName).matches())
            super.handleError("Line: " + fromLineNumber + " Airport Name: '" + this.airportName + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

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

    public String getFormattedairportNameCode() {
        return airportCode + " (" + airportName + ")";
    }
}
