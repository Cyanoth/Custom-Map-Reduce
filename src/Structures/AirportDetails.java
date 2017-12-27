import java.util.regex.Pattern;

public class AirportDetails extends AbstractDetails {
    private final String airportName;
    private final String airportCode;
    private final double latitude;
    private final double longitude;

    AirportDetails(String airportName, String airportCode, String latitude, String longitude) {

        this.latitude = parseValidateLongitudeLatitude(latitude, "Latitude");
        this.longitude = parseValidateLongitudeLatitude(longitude, "Longitude");
        this.airportName = airportName;
        this.airportCode = airportCode;
        performPatternValidation();
    }

    @Override
    protected void performPatternValidation() {
        Pattern pattern_airportCode = Pattern.compile("[A-Z]{3}"); //Pattern XXX
        Pattern pattern_airportName = Pattern.compile("[A-Z\\s\\/]{3,20}"); //Pattern A-Z 3 to 20 length

        if (!pattern_airportCode.matcher(this.airportCode).matches())
            super.handleError("Airport Code: '" + this.airportCode + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_airportName.matcher(this.airportName).matches())
            super.handleError("Airport Name: '" + this.airportName + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

    private double parseValidateLongitudeLatitude(String parsevalue, String type) {
        try {
            double result = Double.parseDouble(parsevalue);

//            if (result < 999 || result > 9999999999999D) { //TODO: Fix the validation for Lat/Long
//                super.handleError(type + ": '" + parsevalue + "' is invalid! (It is out of range!)", ErrorType.Warning);
//                return -1;
//            }
//            else
                return result;
        }
        catch (NumberFormatException e) {
            super.handleError(type + ": '" + parsevalue + "' is invalid! (It is not a valid number.)", ErrorType.Warning);
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
