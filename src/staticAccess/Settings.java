package staticAccess;

public class Settings {
    public static int getMaxThreads()
    {
        return Runtime.getRuntime().availableProcessors() - 2;
    }
}
