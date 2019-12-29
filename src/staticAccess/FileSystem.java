package staticAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    public static List<File> getAllDirectoryFiles(String directoryPath)
    {
        List<File> result = new ArrayList<>();

        File folder = new File(directoryPath);
        for (File file :  folder.listFiles()) {
            if (file.isDirectory()) {
                result.addAll(getAllDirectoryFiles(file.getAbsolutePath()));
            } else if (file.getName().matches(".*\\.json")) {
                result.add(file);
            }
        }

        return result;
    }
}
