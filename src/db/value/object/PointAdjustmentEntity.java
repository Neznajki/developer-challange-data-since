package db.value.object;

public class PointAdjustmentEntity {
    private Integer existingFileId;
    private Integer loanId;
    private Integer index;
    private Integer change;

    public PointAdjustmentEntity(Integer existingFileId, Integer loanId, Integer index, Integer change) {
        this.existingFileId = existingFileId;
        this.loanId = loanId;
        this.index = index;
        this.change = change;
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
}
