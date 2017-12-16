import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Charlie on 09/11/2017.
 */
public class Reducer implements Callable<String>{
    private static final Logger LOGGER = Logger.getLogger(Reducer.class.getName());

    private final KeyValuePair[] pairs;

    public Reducer(KeyValuePair[] pairs, String keyName, int reducerThreadID) {
        this.pairs = pairs;
        LOGGER.log(Level.FINE, "A reducer with the threadID: " + reducerThreadID + " has been setup and is ready to start.");
    }


    @Override
    public String call() throws Exception {
        return null;
    }
}
