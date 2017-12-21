//TODO: Validation & Logging Output
public class PassengerEntry {
    private String passengerId;
    private String flightId;
    private String fromAirport;
    private String toAirport;
    private long departureTime;
    private int flightTime;

    public PassengerEntry(String passengerId, String flightId, String fromAirport, String toAirport, String departureTime, String flightTime) //TODO: Poly this with the correct args.
    {
        //TODO: Add validation
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.departureTime = Long.parseLong(departureTime); //validation check
        this.flightTime = Integer.parseInt(flightTime); // validation check
    }

    //todo: function to get value by string key name.

    public String getPassengerId() {
        return passengerId;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public String getToAirport() {
        return toAirport;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public int getFlightTime() {
        return flightTime;
    }

    public void handleParsingError()
    {
        System.out.println("Parsing Error! ");
    }

    //TODO: Add a tostring function
}
