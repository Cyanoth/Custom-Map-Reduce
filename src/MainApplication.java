import java.util.logging.*;

/**
 * Created by Charlie on 09/11/2017.
 */
public class MainApplication
{
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());

    //TODO: Change ask user to select data file?
    public static void main(String[] args){
        LOGGER.log(Level.INFO, "Application Started");
        Objective1.startObjective1();
//        Objective2.startObjective2();
        //Objective3.startObjective3(null, true); //Uncomment to run Objective3 as standalone (it is called in Objective 2 to add to information list)
        LOGGER.log(Level.INFO, "Application Ended");
    }
}
