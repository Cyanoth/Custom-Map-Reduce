import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A static class that has functions which write to a file on disk.
 * The outputFile should be selected by the user at program startup.
 */
public class OutputFile {
    public static File outputFile; //File Object (on-disk) where the log-entries will be outputted to.

    /**
     * Should only be called once when the OutputFile is first created.
     * This adds introduction text to the output file including the configuration parameters.
     */
    public static void addIntroText() {
        write("Charles Knight -- 23012360 -- Advanced Computing CS3AC16\r\n-----------------------------------------------\r\n\r\n");

        String configText = "Configuration: \r\n" +
                "Maximum Simultaneous Mappers: " + Configuration.MAX_RUNNING_MAPPERS +
                "\r\nMaximum Entries Per Mapper: " + Configuration.MAX_MAPPER_DATAENTRIES +
                "\r\nMaximum Simultaneous Reducers: " + Configuration.MAX_RUNNING_REDUCERS +
                "\r\nPermit Spaces in Airport Name (Strict Validation): " + Configuration.strictlyUseSpecValidation +
                "\r\nPassenger Data File Path:  " + Configuration.passengerDataFilePath +
                "\r\nAirport Data File Path: " + Configuration.airportDataFilePath + "\r\n";
        write(configText);
    }

    /**
     * Opens the output file and writes a string to the text file.
     * Closes & Flushes are written or when an error occurs.
     * @param text String to write to the text file.
     */
    public static void write(String text) {
        text = text.replace("\n", "\r\n");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(outputFile, true));
            bw.write(text);
            bw.flush();
        } catch (IOException ioE) {
            ErrorManager.generateError("Failed to write to output file, See Log for more details.", ErrorType.Fatal, ErrorKind.Other);
            ioE.printStackTrace();
        } finally { //Always close output file onces finished (or error)
            if (bw != null) try {
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
