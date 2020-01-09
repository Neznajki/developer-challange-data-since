package staticAccess;

public class Settings {
    public static int trainNumber = 1;
    public static int positiveRequired = 5000;
    public static int positiveSuccessRequired = 0;
    public static int negativeRequired = -5000;
    public static int negativeSuccessRequired = -0;
    public static int accuracyCorrector = 5;


    public static int getMaxThreads()
    {
        return Runtime.getRuntime().availableProcessors() - 2;
    }
}
