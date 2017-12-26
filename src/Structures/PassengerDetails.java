import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Validation & Logging Output
public class PassengerDetails extends AbstractDetails {
    private static final Logger LOGGER = Logger.getLogger(PassengerDetails.class.getName());

    private final String passengerID;
    private final String flightID;

    public PassengerDetails(String passengerID, String flightID)
    {
        this.passengerID = passengerID;
        this.flightID = flightID;
    }

    @Override
    public String getValueByName(Keys keyname) //Return string, long or int depending on keyType
    {
        switch (keyname) {
            case PassengerID:
                return passengerID;
            case FlightID:
                return flightID;
            default://This should never (and cannot? with enum) be hit.
                LOGGER.log(Level.SEVERE, "Warning! Code requested an invalid KeyName: " + keyname.toString());
                return "INVALIDKEY";
        }
    }
}
