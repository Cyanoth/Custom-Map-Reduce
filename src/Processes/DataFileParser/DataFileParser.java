import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataFileParser {
    private static final Logger LOGGER = Logger.getLogger(DataFileParser.class.getName());

    public static ParsedData parseAllFiles() {
        ParsedData allParsed = parsePassengerFile();
        allParsed.setAllAirports(parseAirportFile());
        return allParsed;
    }

    private static ParsedData parsePassengerFile() {
        LOGGER.log(Level.INFO, "Parsing Passenger Data File: " + Configuration.passengerDataFilePath);
        ParsedData parsedPassengersFlights = new ParsedData();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(Configuration.passengerDataFilePath));
            String currentLine;
            int currentLineNumber = 0;

            while ((currentLine = csvReader.readLine()) != null) {
                String[] sl = currentLine.split(",");
                if (sl.length < 6)
                    ErrorManager.generateError("Invalid amount or missing arguments on line: " + currentLineNumber, ErrorType.Warning, ErrorKind.Parsing);
                else {
                    PassengerDetails parsedLinePassenger = new PassengerDetails(sl[0], sl[1]);
                    if (parsedLinePassenger.isValid()) {
                        FlightDetails parsedFlightDetails = new FlightDetails(sl[1], sl[2], sl[3], sl[4], sl[5]);
                        if (parsedFlightDetails.isValid()) {
                            parsedPassengersFlights.addPassenger(parsedLinePassenger); //TODO: Pass line number for error manager?
                            parsedPassengersFlights.addFlight(parsedFlightDetails); //TODO: Pass line number for error manager?
                        }
                    }
                }
                currentLineNumber++;
            }
            LOGGER.log(Level.INFO, "Parsed Passenger Data File Successfully!");
        }
        catch (IOException e)
        {
            ErrorManager.generateError("An I/O error occurred when parsing the passenger datafile! Please see log for error.", ErrorType.Fatal, ErrorKind.Other);
            e.printStackTrace();
        }

        return parsedPassengersFlights;
    }

    private static ArrayList<AirportDetails> parseAirportFile() {
        LOGGER.log(Level.INFO, "Parsing Top Airport Data File: " + Configuration.airportDataFilePath);
        ArrayList<AirportDetails> airportEntries = new ArrayList<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(Configuration.airportDataFilePath));
            String currentLine;
            int currentLineNumber = 0;

            while ((currentLine = csvReader.readLine()) != null) {
                String[] sl = currentLine.split(",");
                if (sl.length < 4)
                    ErrorManager.generateError("Invalid amount or missing arguments on line: " + currentLineNumber, ErrorType.Warning, ErrorKind.Parsing);
                else {
                    AirportDetails airport = new AirportDetails(sl[0], sl[1], sl[2], sl[3]); //TODO: Pass line number for error manager?
                    boolean airportAlreadyExists = false;

                    for (AirportDetails existingAirport: airportEntries) {
                        if (existingAirport.getValueByName(Keys.AirportCode) == airport.getValueByName(Keys.AirportCode)) {
                            airportAlreadyExists = true;
                            ErrorManager.generateError("The AirportCode: " + airport.getValueByName(Keys.AirportCode) + " has occurred more than once in the Top30 " +
                                                        "DataFile, please verify this.", ErrorType.Warning, ErrorKind.Parsing);
                            break;
                        }
                    }

                    if (!airportAlreadyExists && airport.isValid()) {
                        airportEntries.add(airport);
                    }
                }
                currentLineNumber++;
            }
            LOGGER.log(Level.INFO, "Parsed Airport Data File Successfully!");
        }
        catch (IOException e)
        {
            ErrorManager.generateError("An I/O error occurred when parsing the Top30 Airport datafile! Please see log for error.", ErrorType.Fatal, ErrorKind.Other);
            e.printStackTrace();
        }

        return airportEntries;
    }
}
