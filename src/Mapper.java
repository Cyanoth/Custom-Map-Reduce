import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Charlie on 09/11/2017.
 */
public class Mapper {
    private String STATUS = "";
    //Pass A Reducer Object?

    public HashMap<String, Integer> Mapper(ArrayList<PassengerEntry> dataChunk)
    {
        //TODO: Systmatic Checking should take place in this function.
        HashMap<String, Integer> mappedValues = new HashMap<String, Integer>();

        for (PassengerEntry entry: dataChunk) {
            mappedValues.put(entry.getFromAirport(), 1);
        }
        return mappedValues;
    }
}
