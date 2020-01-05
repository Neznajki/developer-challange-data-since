package concurrency;

import db.value.object.LoanEntity;
import db.value.object.PointAdjustmentEntity;

import java.util.ArrayList;
import java.util.List;

public class ResultRisk {
    private LoanEntity loan;
    private Integer points = 0;
    private List<PointAdjustmentEntity> pointAdjustmentEntities = new ArrayList<>();

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

    public void addPointAdjustment(PointAdjustmentEntity entity)
    {
        this.pointAdjustmentEntities.add(entity);
    }

    public List<PointAdjustmentEntity> getPointAdjustmentEntities() {
        return pointAdjustmentEntities;
    }
}
