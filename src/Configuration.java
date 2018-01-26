import java.io.File;
import java.nio.file.Path;

public class Configuration {
    public static final int MAX_RUNNING_MAPPERS = 5; //The amount of mapper objects that can be running at anyone time.
    public static final int MAX_MAPPER_DATAENTRIES = 50; //Amount of entries that can only be parsed in to a single mapper.
    public static final int MAX_RUNNING_REDUCERS = 5; //The amount of Reducers objects that can be running at anyone time.

    public static File passengerDataFilePath; //Set on program startup - An Passenger Data File on disk.
    public static File airportDataFilePath; //Set on program startup - An Top30 Data File on disk.

    public static final boolean strictlyUseSpecValidation = false;

}
