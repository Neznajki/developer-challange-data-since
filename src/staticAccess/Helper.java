package staticAccess;

public class Helper {
    public static int successFound = 0;
    public static int failedFound = 0;

    public static Integer convertStringComaToInteger(String stringValue)
    {
        int result = 0;

        String[] values = stringValue.split(",");

        for (String value: values) {
            result |= 1 << Integer.valueOf(value);
        }

        return result;
    }
}
