package concurrency;

import db.value.object.LoanEntity;

public class ResultRisk {
    private LoanEntity loan;
    private Integer points = 0;

    public ResultRisk(LoanEntity loan)
    {
        this.loan = loan;
    }

    public LoanEntity getLoan() {
        return loan;
    }

    public void addPoints(Integer points)
    {
        this.points += points;
    }

    public Integer getPoints() {
        return points;
    }
}
