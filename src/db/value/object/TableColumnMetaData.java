package db.value.object;

public class TableColumnMetaData {
    private static String toCamelCase(String value)
    {
        String[] parts = value.split("_");
        StringBuilder camelCaseString = new StringBuilder();
        for (String part : parts){
            camelCaseString.append(toProperCase(part));
        }
        return camelCaseString.toString();
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    private String name;
    private String type;
    private String getter;

    public TableColumnMetaData(String name, String type)
    {
        this.name = name;
        this.type = type;
        this.getter = "get" + toCamelCase(name);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getGetter() {
        return getter;
    }
}
