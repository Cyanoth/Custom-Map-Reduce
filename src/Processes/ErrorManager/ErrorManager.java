import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorManager {
    private static final Logger LOGGER = Logger.getLogger(ErrorType.class.getName());

    private static String errorLog = "";
    private static int totalWarnings = 0;
    private static boolean fatalErrorOccurred = false;


    public static void generateError(String errorDescription, ErrorType errorType, ErrorKind errorKind) {
       if (errorType == ErrorType.Warning) {
           LOGGER.log(Level.WARNING, errorKind.toString() + " error occurred: " + errorDescription);
           errorLog += "Warning: " + errorKind.toString() + " error occurred: " + errorDescription + "\n";
           totalWarnings++;
       } else if (errorType == ErrorType.Fatal) {
           LOGGER.log(Level.SEVERE, errorKind.toString() + " error occurred: " + errorDescription);
           errorLog += "Error: " + errorKind.toString() + " error occurred: " + errorDescription + "\n";
           fatalErrorOccurred = true;
       }
    }

    public static boolean hasFatalErrorOccurred() { //Allowed to keep going if there is no fatal errors.
        return fatalErrorOccurred;
    }

    public static void displayErrorSummary() {
        if (totalWarnings > 0)
            LOGGER.log(Level.WARNING, "Total Warnings occurred: " + totalWarnings);
        else
            LOGGER.log(Level.INFO, "No Warnings occurred during this objective.");
    }

    public static String getErrorLog() {
        return errorLog;
    }

    public static String outputErrorLog() {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n\n**************\nError Summary\n**************\n");
        outputBuilder.append("Fatal Error Occurred: " + fatalErrorOccurred + "\n");
        outputBuilder.append("Total Warnings: " + totalWarnings + "\n");
        outputBuilder.append("****************** Error Log *******************\n");
        outputBuilder.append(errorLog);
        return  outputBuilder.toString();
    }

    public static void resetErrorManager() {
        errorLog = "";
        totalWarnings = 0;
        fatalErrorOccurred = false;
        LOGGER.log(Level.INFO, "Error Manager has been reset!");
    }

    public static void testing_PauseProgram()
    {
        System.out.println("Press Enter key to continue..."); //Simple Pause Script.
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
