import java.util.logging.Level;
import java.util.logging.Logger;

public class AirportDetails extends AbstractDetails {
    private static final Logger LOGGER = Logger.getLogger(AirportDetails.class.getName());

    private final String airportName;
    private final String airportCode;
    private final double latitude;
    private final double longitude;

    public AirportDetails(String airportName, String airportCode, double latitude, double longitude) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.latitude = latitude;
        this.longitude = longitude;
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
            default:
                LOGGER.log(Level.SEVERE, "Warning! Code requested an invalid KeyName: " + keyname.toString());
                return "INVALIDKEY";
        }
    }

    public String getFormattedairportNameCode() {
        return airportCode + " (" + airportName + ")";
    }
}
