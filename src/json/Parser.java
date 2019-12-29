package json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Parser {

    public KnownHistoryFileContent readFile(String filePath) throws IOException {
        return this.readFile(new File(filePath));
    }

    public KnownHistoryFileContent readFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        KnownHistoryFileContent result = mapper.readValue(file, KnownHistoryFileContent.class);

        for (CheckRule checkRule: result.metaData) {
            checkRule.primaryField.target = checkRule.primaryField.target.intern();
            checkRule.primaryField.field = checkRule.primaryField.field.intern();

            if (checkRule.compareAgainst != null) {
                checkRule.compareAgainst.target = checkRule.compareAgainst.target.intern();
                checkRule.compareAgainst.field = checkRule.compareAgainst.field.intern();
            }
        }

        return result;
    }
}
