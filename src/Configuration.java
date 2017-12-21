
public class Configuration {
    public static final int MAX_RUNNING_MAPPERS = 5; //The amount of mapper objects that can be running at anyone time.
    public static final int MAX_MAPPER_DATAENTRIES = 50; //Amount of entries that can only be parsed in to a single mapper.

    public static final int MAX_RUNNING_REDUCERS = 5; //The amount of Reducers objects that can be running at anyone time.


    public static String passengerDataFilePath = "/home/charlie/Documents/MapReduce-AdvComp/Data/AComp_Passenger_data_no_error.csv";
    public static String airportDataFilePath = "/home/charlie/Documents/MapReduce-AdvComp/Data/Top30_airports_LatLong.csv";


}
