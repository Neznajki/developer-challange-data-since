package concurrency;

import db.maria.HistoryTable;
import db.value.object.LoanEntity;

import java.util.List;
import java.util.concurrent.*;

public class HistoryGatherTask implements Callable<List<LoanEntity>> {
    private List<LoanEntity> loanEntities;

    public HistoryGatherTask(List<LoanEntity> loanEntities)
    {
        this.loanEntities = loanEntities;
    }

    @Override
    public List<LoanEntity> call() throws Exception {
        HistoryTable historyTable = new HistoryTable();
        historyTable.fillLoanData(this.loanEntities);

        return this.loanEntities;
    }
}
