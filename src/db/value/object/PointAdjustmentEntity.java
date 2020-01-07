package db.value.object;

public class PointAdjustmentEntity {
    private Integer existingFileId;
    private Integer loanId;
    private Integer index;
    private Integer change;
    private boolean knownSuccess;

    public PointAdjustmentEntity(Integer existingFileId, Integer loanId, Integer index, Integer change, boolean knownSuccess) {
        this.existingFileId = existingFileId;
        this.loanId = loanId;
        this.index = index;
        this.change = change;
        this.knownSuccess = knownSuccess;
    }

    public Integer getExistingFileId() {
        return existingFileId;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getChange()
    {
        return change;
    }

    public boolean getKnownSuccess() {
        return knownSuccess;
    }
}
