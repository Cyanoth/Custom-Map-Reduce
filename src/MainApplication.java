import java.util.logging.*;

/**
 * Created by Charlie on 09/11/2017.
 */
public class    MainApplication
{
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());

    private ParsedData parsedEntries;


    //TODO: Change hardcoded paths into command line arguments and test if they exist/not null
    public static void main(String[] args){
        LOGGER.log(Level.INFO, "Application Started");



//        Objective1 testObjective1 = new Objective1();
//        testObjective1.startObjective1();
        Objective2 testObjective2 = new Objective2(); //does this need parameters? //TODO: Add 'Select data file paths', validate they are correctly formatted.
        testObjective2.startObjective2();
        TestFunctions.testing_PauseProgram();



    }

}
