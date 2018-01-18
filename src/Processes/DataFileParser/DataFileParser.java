import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains functions to parse the data in the two data files. Uses a 'ParsedData' object for storage.
 */
public class DataFileParser {
    private static final Logger LOGGER = Logger.getLogger(DataFileParser.class.getName());

    /**
     * The initial function for parsing the two data files.
     * @return An object (ParsedData) which contains the two data files parsed into ArrayLists.
     */
    public static ParsedData parseAllFiles() {
        ParsedData allParsed = new ParsedData();
        allParsed.setAllAirports(parseAirportFile()); //Call function to parse the Top30 Data File.
        if (ErrorManager.hasFatalErrorOccurred()) { return null; } //Fatal Error Occurred, Cannot Continue.

        allParsed = parsePassengerFile(allParsed); //Call function to parse the Passenger Data File.
        return allParsed;
    }

    /**
     * Parse the CSV Passenger Data by current a buffer read and creating appropriate AbstractDetails objects.
     * Note that syntax validation happens when an AbstractDetail class is instantiated.
     * @param currentData Pass in an initialised ParsedData object (should contain already Parsed Top30 DataFile)
     * @return A ParsedObject which contains the parsed Passenger Data.
     */
    private static ParsedData parsePassengerFile(ParsedData currentData) {
        LOGGER.log(Level.INFO, "Parsing Passenger Data File: " + Configuration.passengerDataFilePath);
        ParsedData parsedPassengersAndFlights = currentData; //Don't overwrite the previously parsed Top30 Data File.
        if (currentData == null) //If passed parameter was null, initialise it here instead.
            parsedPassengersAndFlights= new ParsedData();

        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(Configuration.passengerDataFilePath));
            String currentLine;
            int currentLineNumber = 1;

            while ((currentLine = csvReader.readLine()) != null) { //For each line in the data file.
                String[] sl = currentLine.split(","); //Split line by comma.
                if (sl.length != 6) //If not 6 values, then there is already an issue with the line. Skip line, show warning.
                    ErrorManager.generateError("[Passenger File] Invalid amount or missing arguments on line: " + currentLineNumber, ErrorType.Warning, ErrorKind.Parsing);
                else {
                    FlightDetails parsedLineFlight = new FlightDetails(sl[1], sl[2], sl[3], sl[4], sl[5], currentData, currentLineNumber); //Create FlightDetails with data from the line.

                    if (parsedLineFlight.isValid()) { //Call function to syntactically validate the flight details
                        PassengerDetails parsedLinePassenger = new PassengerDetails(sl[0], sl[1], currentLineNumber); //Create PassengerDetails with data from the line

                        if (parsedLinePassenger.isValid()) { //Call function to syntactically validate the passenger details.
                            /* Flight Detail & Passenger Detail are syntactically correct, so add them to the ParsedData object */
                            parsedPassengersAndFlights.addPassenger(parsedLinePassenger);
                            parsedPassengersAndFlights.addFlight(parsedLineFlight);
                            parsedLinePassenger.linkFlightDetails(parsedPassengersAndFlights.getFlightDetailsByID(sl[1]));
                        }
                    }
                    currentLineNumber++;
                }
            }
            LOGGER.log(Level.INFO, "Parsed Passenger Data File Successfully!");
        }
        catch (IOException e)
        {
            ErrorManager.generateError("An I/O error occurred when parsing the passenger datafile! Please see log for error.", ErrorType.Fatal, ErrorKind.Other);
            e.printStackTrace();
        }
        finally {
            if (csvReader != null) {
                try {
                    csvReader.close(); //Close the file after it has been parsed or an error occurs.
                }
                catch (IOException ignored) { } //Failed to close the file, but this does not matter.
            }
        }
        return parsedPassengersAndFlights;
    }

    /**
     * Parse the Top30 Airport File into an ArrayList containing AirportDetail objects.
     * @return ArrayList containing the parsed Top30 airport data file in the form of AirportDetail objects.
     */
    private static ArrayList<AirportDetails> parseAirportFile() {
        LOGGER.log(Level.INFO, "Parsing Top Airport Data File: " + Configuration.airportDataFilePath);
        ArrayList<AirportDetails> airportEntries = new ArrayList<>();

        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(Configuration.airportDataFilePath));
            String currentLine;
            int currentLineNumber = 0;

            while ((currentLine = csvReader.readLine()) != null) { //For each line in the data file.
                String[] sl = currentLine.split(","); //Split line by comma.
                if (sl.length < 4)  //If not 4 values, then there is already an issue with the line. Skip line, show warning.
                    ErrorManager.generateError("[Top30 Airports] Invalid amount or missing arguments on line: " + currentLineNumber, ErrorType.Warning, ErrorKind.Parsing);
                else {
                    AirportDetails airport = new AirportDetails(sl[0], sl[1], sl[2], sl[3], currentLineNumber); //Create AirportDetails with data from the line
                    boolean airportAlreadyExists = false;

                    for (AirportDetails existingAirport: airportEntries) { //Prevent duplicate Airport codes being added from datafile, display warning.
                        if (existingAirport.getValueByName(Keys.AirportCode) == airport.getValueByName(Keys.AirportCode)) {
                            airportAlreadyExists = true;
                            ErrorManager.generateError("The AirportCode: " + airport.getValueByName(Keys.AirportCode) + " has occurred more than once in the Top30 " +
                                                        "DataFile, please verify this.", ErrorType.Warning, ErrorKind.Parsing);
                            break;
                        }
                    }

                    if (!airportAlreadyExists && airport.isValid()) { //Entry is not a duplicate & syntactically correct (isValid()). Add to results.
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
        finally {
            if (csvReader != null) {
                try {
                    csvReader.close(); //Close the file after it has been parsed or an error occurs.
                }
                catch (IOException ignored) { } //Failed to close the file, but this does not matter.
            }
        }
        return airportEntries;
    }
}