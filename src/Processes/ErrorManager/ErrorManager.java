import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  A static class that can be called anywhere in the program that records
 *  custom errors that have occurred during the execution of the program.
 */
public class ErrorManager {
    private static final Logger LOGGER = Logger.getLogger(ErrorType.class.getName());

    private static String errorLog = "";
    private static int totalWarnings = 0;
    private static int parsingErrors = 0;
    private static int logicalErrors = 0;
    private static boolean fatalErrorOccurred = false;


    /**
     * Called anywhere within the program to display (generate) an error. Adds to error log and can be just a warning or a fatal error.
     * @param errorDescription A string which describes what caused the error. No need to tag Warning/Fatal this is prefixed.
     * @param errorType Specify if the error is a Warning (continue execution) or a Fatal Error (stop execution)
     * @param errorKind Specifies the type of error, such as a parsing or logical error.
     */
    public static void generateError(String errorDescription, ErrorType errorType, ErrorKind errorKind) {
       if (errorType == ErrorType.Warning) {
           LOGGER.log(Level.WARNING, errorKind.toString() + " error occurred: " + errorDescription);
           errorLog += "Warning: " + errorKind.toString() + " error occurred: " + errorDescription + "\n";

           if (errorKind == ErrorKind.Parsing)
               parsingErrors++;
           else if (errorKind == ErrorKind.Logical)
               logicalErrors++;
           totalWarnings++;
       } else if (errorType == ErrorType.Fatal) {
           LOGGER.log(Level.SEVERE, errorKind.toString() + " error occurred: " + errorDescription);
           errorLog += "Error: " + errorKind.toString() + " error occurred: " + errorDescription + "\n";
           fatalErrorOccurred = true; //Stop execution by flagging a fatal error occurred.
       }
    }

    /**
     * @return Flag check if a fatal error has occurred. Program should not continue if this true.
     */
    public static boolean hasFatalErrorOccurred() { //Allowed to keep going if there is no fatal errors.
        return fatalErrorOccurred;
    }

    /**
     * Outputs to console how many warnings have occurred during an objective.
     */
    public static void displayErrorSummary() {
        if (totalWarnings > 0)
            LOGGER.log(Level.WARNING, "Total Warnings occurred: " + totalWarnings);
        else
            LOGGER.log(Level.INFO, "No Warnings occurred during this objective.");
    }

    /**
     * Output a list of errors that have occurred and the counters for warnings or fatal errors.
     * @return Formatted String containing list of errors and warnings and their respective counters.
     */
    public static String outputErrorLog() {
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("\n\n**************\nError Summary\n**************\n");
        outputBuilder.append("Fatal Error Occurred: " + fatalErrorOccurred + "\n");
        outputBuilder.append("Total Warnings: " + totalWarnings + "\n");
        outputBuilder.append("Parsing Errors: " + parsingErrors + " Logical Errors: " + logicalErrors + "\n");
        outputBuilder.append("****************** Error Log *******************\n");
        outputBuilder.append(errorLog);
        return  outputBuilder.toString();
    }

    /**
     * Reset the ErrorManager including counter and logs.
     */
    public static void resetErrorManager() {
        errorLog = "";
        totalWarnings = 0;
        parsingErrors = 0;
        logicalErrors = 0;
        fatalErrorOccurred = false;
        LOGGER.log(Level.INFO, "Error Manager has been reset!");
    }

    /**
     * No longer used, but during development this would pause the program until and key was pressed.
     */
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
