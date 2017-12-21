import java.io.IOException;

class TestFunctions {
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
