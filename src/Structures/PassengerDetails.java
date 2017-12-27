import java.util.regex.Pattern;

public class PassengerDetails extends AbstractDetails {
    private final String passengerID;
    private final String flightID;

    PassengerDetails(String passengerID, String flightID)
    {
        this.passengerID = passengerID;
        this.flightID = flightID;
        performPatternValidation();
      }

    @Override
    protected void performPatternValidation() {
        Pattern pattern_passengerID = Pattern.compile("[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]"); //Pattern: XXXnnnnXXn
        Pattern pattern_flightID = Pattern.compile("[A-Z]{3}[0-9]{4}[A-Z]"); //Pattern: XXXnnnnX

        if (!pattern_passengerID.matcher(this.passengerID).matches()) //If passengerID does not match the pattern
            super.handleError("PassengerID: + '" + this.passengerID + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

        else if (!pattern_flightID.matcher(this.flightID).matches())
            super.handleError("FlightID: '" + this.flightID + "' is invalid! (It does not match the required pattern)", ErrorType.Warning);

    }

    @Override
    public String getValueByName(Keys keyname) //Return string, long or int depending on keyType
    {
        switch (keyname) {
            case PassengerID:
                return passengerID;
            case FlightID:
                return flightID;
            default://This should never be hit due to enum restrictions.
               handleError("Warning! The code requested an invalid KeyName: " + keyname.toString(), ErrorType.Fatal);
               return "INVALID_KEY";
        }
    }



}
