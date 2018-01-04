import java.security.Key;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Objective3 {
    private static final Logger LOGGER = Logger.getLogger(Objective3.class.getName());

    public static ArrayList<KeyValuePair> startObjective3(ArrayList<KeyValuePair> obj2ReducedPairs) {
        LOGGER.log(Level.INFO, "Starting Objective 3");
        ArrayList<KeyValuePair> passengerCountPair = new ArrayList<>();

        for (KeyValuePair pair: obj2ReducedPairs) {
            String[] splitText = pair.getMapValue().toString().split(" ");
            passengerCountPair.add(new KeyValuePair(pair.getMapKey(), splitText.length));
        }
        return passengerCountPair;
    }
}
