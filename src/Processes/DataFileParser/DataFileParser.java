import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataFileParser {
    private static final Logger LOGGER = Logger.getLogger(DataFileParser.class.getName());

    public static ParsedData parseAllFiles() {
        ParsedData allParsed = new ParsedData();
        allParsed = parsePassengerFile();
        allParsed.setAllAirports(parseAirportFile());

        return allParsed;
    }

    private static ParsedData parsePassengerFile() {
        LOGGER.log(Level.FINE, "Parsing Passenger Data File: " + Configuration.passengerDataFilePath);
        ParsedData parsedPassengersFlights = new ParsedData();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(Configuration.passengerDataFilePath));
            String currentLine;

            while ((currentLine = csvReader.readLine()) != null) {
                String[] sl = currentLine.split(",");
                if (sl.length < 6)
                    System.out.println("Invalid amount of arguments on line number: UNKNOWN"); //TODO: Handle This Correctly.
                else {
                    parsedPassengersFlights.addPassenger(new PassengerDetails(sl[0], sl[1]));
                    parsedPassengersFlights.addFlight(new FlightDetails(sl[1], sl[2], sl[3], sl[4], sl[5]));
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Error has occurred with the passenger data file");//TODO: Handle this efficently.
            e.printStackTrace();
        }

        LOGGER.log(Level.FINE, "Parsed Passenger Data File Successfully!");
        return parsedPassengersFlights;
    }

    public static ArrayList<AirportDetails> parseAirportFile() {
        LOGGER.log(Level.FINE, "Parsing Top Airport Data File: " + Configuration.airportDataFilePath);
        ArrayList<AirportDetails> airportEntries = new ArrayList<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(Configuration.airportDataFilePath));
            String currentLine;

            while ((currentLine = csvReader.readLine()) != null) {
                String[] sl = currentLine.split(",");
                if (sl.length < 4)
                    System.out.println("Invalid amount of arguments on line number: UNKNOWN"); //TODO: Handle This Correctly.
                else {
                    AirportDetails airport = new AirportDetails(sl[0], sl[1], Double.parseDouble(sl[2]), Double.parseDouble(sl[3]));
                    boolean airportAlreadyExists = false;

                    for (AirportDetails existingAirport: airportEntries) {
                        if (existingAirport.getValueByName(Keys.AirportCode) == airport.getValueByName(Keys.AirportCode)) {
                            airportAlreadyExists = true;
                            LOGGER.log(Level.FINE, "Airport with the Code:" + existingAirport.getValueByName(Keys.AirportCode) + " already exists, so wasn't added!");
                            break;
                            //TODO: Validation will go here, check that the new airport matches the existing airport details. If it doesn't there is a conflict of data and will need to throw an error.
                        }
                    }

                    if (!airportAlreadyExists) {
                        airportEntries.add(airport);
                        LOGGER.log(Level.FINE, "Added a airport entry (" + airportEntries.size() + ")" + " AirportCode: " + airport.getValueByName(Keys.AirportCode) +
                                " AirportName: " + airport.getValueByName(Keys.AirportName) + " Lat: " + airport.getValueByName(Keys.Latitude) +
                                " Longitude: " + airport.getValueByName(Keys.Longitude));
                    }
                }

            }
        }
        catch (IOException e)
        {
            LOGGER.log(Level.SEVERE, "Error has occurred with the Airport data file");//TODO: Handle this efficently.
            e.printStackTrace();
        }

        LOGGER.log(Level.FINE, "Parsed Airport Data File Successfully!");
        return airportEntries;
    }
}
