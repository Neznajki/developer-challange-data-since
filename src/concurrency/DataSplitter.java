package concurrency;

import db.value.object.LoanEntity;
import staticAccess.Settings;

import java.util.ArrayList;
import java.util.List;

public class DataSplitter {
//    protected List<?> data;
    protected List<List<LoanEntity>> threadDataCollection;

    public DataSplitter(List<LoanEntity> data)
    {
        this.threadDataCollection = new ArrayList<>();
        int maxThreads = Settings.getMaxThreads();

        for (int i=0; i< maxThreads; i++) {
            this.threadDataCollection.add(new ArrayList<>());
        }

        int i = 0;

        for (LoanEntity loanEntity: data) {
            this.threadDataCollection.get(i).add(loanEntity);

            i++;
            if (i == maxThreads) {
                i = 0;
            }
        }
    }

    public List<List<LoanEntity>> getThreadDataCollection() {
        return threadDataCollection;
    }
}
