package concurrency;

import db.value.object.LoanEntity;
import job.RiskCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CalculateHistoryRiskTask implements Callable<List<ResultRisk>> {
    private final List<LoanEntity> loanEntities;
    private final RiskCalculator riskCalculator;
    private List<ResultRisk> resultSet = new ArrayList<>();

    public CalculateHistoryRiskTask(List<LoanEntity> loanEntities, RiskCalculator riskCalculator)
    {
        this.loanEntities = loanEntities;
        this.riskCalculator = riskCalculator;
    }

    @Override
    public List<ResultRisk> call() {
        try {
            for (LoanEntity loan: this.loanEntities) {
                ResultRisk loanRisk = new ResultRisk(loan);
                this.resultSet.add(loanRisk);

                loanRisk.addPoints(riskCalculator.getHistoryRiskPoints(loan, loanRisk));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.resultSet;
    }
}
