import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {

    public static File outputFile;
    private static boolean fileOpen = false;
    private static boolean fileReady = false;

    public static void addIntroText() {
        write("Charles Knight -- 23012360 -- Advanced Computing CS3AC16\n-----------------------------------------------\n\n");

        StringBuilder configText = new StringBuilder();
        configText.append("Configuration: \n");
        configText.append("Maximum Simultaneous Mappers: " + Configuration.MAX_RUNNING_MAPPERS);
        configText.append("\nMaximum Entries Per Mapper: " + Configuration.MAX_MAPPER_DATAENTRIES);
        configText.append("\nMaximum Simultaneous Reducers: " + Configuration.MAX_RUNNING_REDUCERS);
        configText.append("\nPassenger Data File Path:  " + Configuration.passengerDataFilePath);
        configText.append("\nAirport Data File Path: " + Configuration.airportDataFilePath + "\n");
        write(configText.toString());
    }

    public static void write(String text) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(outputFile, true));
            bw.write(text);
            bw.flush();
        } catch (IOException ioE) {
            ErrorManager.generateError("Failed to write to output file, See Log for more details.", ErrorType.Fatal, ErrorKind.Other);
            ioE.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
