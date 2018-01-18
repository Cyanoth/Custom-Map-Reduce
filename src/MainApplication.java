import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.*;

public class MainApplication
{
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());

    /**
     * Initial function for the application. Asks users to select the data files & outputs
     * and then starts the objectives. Comment out objectives to stop them running.
     * @param args Redundant.
     */
    public static void main(String[] args){
        LOGGER.log(Level.INFO, "Application Started");
        if (selectTop30DataFile() && selectPassengerDataFile() && saveOutputFile()) { //If all files are selected
            Objective1.startObjective1();
            Objective2.startObjective2();
            Objective3_Standalone.startObjective3();
        }
        OutputFile.write(ErrorManager.outputErrorLog()); //After all objectives, write the ErrorLog to File.
        LOGGER.log(Level.INFO, "Application Ended");
    }

    /**
     * Opens the File Select dialog which prompts the user to select the Top30 Airport file on Disk
     * @return True if a file was selected. False to abort program.
     */
    private static boolean selectTop30DataFile() {
        LOGGER.log(Level.INFO, "Asking User To Select Top30 Data File...");
        FileDialog dialog = new FileDialog(new JFrame(), "Choose Top30 Airport DataFile", FileDialog.LOAD); //Dialog to select file.
        dialog.setVisible(true);

        if (dialog.getFile() == null) {
            ErrorManager.generateError("No file was selected!", ErrorType.Fatal, ErrorKind.Other);
            return false;
        } else {
            Configuration.airportDataFilePath = new File(dialog.getDirectory() + dialog.getFile());
            LOGGER.log(Level.INFO, "Top30 Data File: " + Configuration.airportDataFilePath.getPath());
            return true;
        }
    }

    /**
     * Opens the File Select dialog which prompts the user to select the Passenger Data file on Disk
     * @return True if a file was selected. False to abort program.
     */
    private static boolean selectPassengerDataFile() {
        LOGGER.log(Level.INFO, "Asking User To Select Passenger Data File...");
        FileDialog dialog = new FileDialog(new JFrame(), "Choose Passenger DataFile", FileDialog.LOAD); //Dialog to select file.
        dialog.setVisible(true);

        if (dialog.getFile() == null) {
            ErrorManager.generateError("No file was selected!", ErrorType.Fatal, ErrorKind.Other);
            return false;
        } else {
            Configuration.passengerDataFilePath = new File(dialog.getDirectory() + dialog.getFile());
            LOGGER.log(Level.INFO, "Passenger Data File: " + Configuration.passengerDataFilePath.getPath());
            return true;
        }
    }

    /**
     * Opens the Save File  dialog which prompts the user to select the location to save the data file on Disk
     * @return True if a location was selected. False to abort the program.
     */
    private static boolean saveOutputFile() {
        LOGGER.log(Level.INFO, "Asking User To Save Output File..." );
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss"); //Default File Name is the current date & time.txt
        Calendar cal = Calendar.getInstance();

        FileDialog dialog = new FileDialog(new Frame(), "Save output file to..", FileDialog.SAVE);
        dialog.setFile(dateFormat.format(cal.getTime()) + ".txt");
                dialog.setVisible(true);

        if (dialog.getFile() == null) {
            ErrorManager.generateError("No output destination was selected!", ErrorType.Fatal, ErrorKind.Other);
            return false;
        } else {
            OutputFile.outputFile = new File(dialog.getDirectory() + dialog.getFile());
            OutputFile.addIntroText();
            LOGGER.log(Level.INFO, "Output File Set: " + OutputFile.outputFile.getPath());
            return true;
        }

    }
}
