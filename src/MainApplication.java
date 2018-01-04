import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.*;

/**
 * Created by Charlie on 09/11/2017.
 */
public class MainApplication
{
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());

    public static void main(String[] args){
        LOGGER.log(Level.INFO, "Application Started");
        if (selectTop30DataFile() && selectPassengerDataFile() && saveOutputFile()) {
            Objective1.startObjective1();
            Objective2.startObjective2();
            //Objective3.startObjective3(null, true); //Uncomment to run Objective3 as standalone (it is called in Objective 2 to add to information list)
        }
        //TODO: Close output file.
        OutputFile.write(ErrorManager.outputErrorLog());
        LOGGER.log(Level.INFO, "Application Ended");
    }

    private static boolean selectTop30DataFile() {
        LOGGER.log(Level.INFO, "Asking User To Select Top30 Data File...");
        FileDialog dialog = new FileDialog(new JFrame(), "Choose Top30 Airport DataFile", FileDialog.LOAD); //Windows dialog to select file.
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

    private static boolean selectPassengerDataFile() {
        LOGGER.log(Level.INFO, "Asking User To Select Passenger Data File...");
        FileDialog dialog = new FileDialog(new JFrame(), "Choose Passenger DataFile", FileDialog.LOAD); //Windows dialog to select file.
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

    private static boolean saveOutputFile() {
        LOGGER.log(Level.INFO, "Asking User To Save Output File..." );
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
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
