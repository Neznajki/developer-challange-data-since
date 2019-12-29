package db.value.object;

public class FinalResultEntity {
    public static int pointsRequired;

    private Integer id;
    private boolean isGood;
    private Integer riskPoints;

    public FinalResultEntity(Integer id, Integer riskPoints) {
        this.id = id;
        this.riskPoints = riskPoints;
    }

    public Integer getId() {
        return id;
    }

    public void setIsGood()
    {
        this.isGood = this.riskPoints < pointsRequired;
    }

    public boolean isGood() {
        this.setIsGood();

        return isGood;
    }

    public void addPoints(int points)
    {
        this.riskPoints += points;
    }

    public Integer getRiskPoints() {
        return riskPoints;
    }
}
