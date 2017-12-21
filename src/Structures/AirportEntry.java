public class AirportEntry {
    private String airportName;
    private String airportCode;
    private double latitude;
    private double longitude;

    public AirportEntry(String airportName, String airportCode, double latitude, double longitude) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }
}
