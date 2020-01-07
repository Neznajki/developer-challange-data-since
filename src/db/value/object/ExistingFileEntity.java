package db.value.object;

public class ExistingFileEntity {
    private Integer id;
    private String fileName;
    private Integer positiveCoefficient;
    private Integer positiveFailureCoefficient;
    private Integer negativeCoefficient;
    private Integer negativeSuccessCoefficient;

    public ExistingFileEntity(Integer id, String fileName, Integer positiveCoefficient, Integer positiveFailureCoefficient, Integer negativeCoefficient, Integer negativeSuccessCoefficient) {
        this.id = id;
        this.fileName = fileName;
        this.positiveCoefficient = positiveCoefficient;
        this.positiveFailureCoefficient = positiveFailureCoefficient;
        this.negativeCoefficient = negativeCoefficient;
        this.negativeSuccessCoefficient = negativeSuccessCoefficient;
    }

    public Integer getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getPositiveCoefficient() {
        return positiveCoefficient;
    }

    public Integer getNegativeCoefficient() {
        return negativeCoefficient;
    }

    public Integer getPositiveFailureCoefficient() {
        return positiveFailureCoefficient;
    }

    public Integer getNegativeSuccessCoefficient() {
        return negativeSuccessCoefficient;
    }
}
