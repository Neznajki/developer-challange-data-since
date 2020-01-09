package db.value.object;

import staticAccess.Settings;

public class ExistingFileEntity {
    private Integer id;
    private String fileName;
    private Integer positiveCoefficient;
    private Integer positiveFailureCoefficient;
    private Integer negativeCoefficient;
    private Integer negativeSuccessCoefficient;
    private final Integer positiveFiles;
    private final Integer positiveSuccessFiles;
    private final Integer negativeFiles;
    private final Integer negativeSuccessFiles;
    private final Integer positiveTotalFiles;
    private final Integer negativeTotalFiles;

    public ExistingFileEntity(
            Integer id,
            String fileName,
            Integer positiveCoefficient,
            Integer positiveFailureCoefficient,
            Integer negativeCoefficient,
            Integer negativeSuccessCoefficient,
            Integer positiveFiles,
            Integer positiveSuccessFiles,
            Integer negativeFiles,
            Integer negativeSuccessFiles,
            Integer positiveTotalFiles,
            Integer negativeTotalFiles
    ) {
        this.id = id;
        this.fileName = fileName;
        this.positiveCoefficient = positiveCoefficient;
        this.positiveFailureCoefficient = positiveFailureCoefficient;
        this.negativeCoefficient = negativeCoefficient;
        this.negativeSuccessCoefficient = negativeSuccessCoefficient;
        this.positiveFiles = positiveFiles;
        this.positiveSuccessFiles = positiveSuccessFiles;
        this.negativeFiles = negativeFiles;
        this.negativeSuccessFiles = negativeSuccessFiles;
        this.positiveTotalFiles = positiveTotalFiles;
        this.negativeTotalFiles = negativeTotalFiles;
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

    public Integer getPositiveFiles() {
        return positiveFiles;
    }

    public Integer getPositiveSuccessFiles() {
        return positiveSuccessFiles;
    }


    public Integer getNegativeFiles() {
        return negativeFiles;
    }


    public Integer getNegativeSuccessFiles() {
        return negativeSuccessFiles;
    }

    public Integer getPositiveTotalFiles() {
        return positiveTotalFiles;
    }

    public Integer getNegativeTotalFiles() {
        return negativeTotalFiles;
    }

    public Integer getPositiveMultiplyCoefficient() {
        int result = 0;

        if (this.getPositiveTotalFiles() > 10) {
            int positiveFiles = (int) (100.0 / this.getNegativeTotalFiles() * this.getPositiveFiles());
            int positiveSuccessFiles = (int) (100.0 / this.getPositiveTotalFiles() * this.getPositiveSuccessFiles());
            if (positiveSuccessFiles != 0) {
                result += this.getPositiveFailureCoefficient() / positiveSuccessFiles / Settings.accuracyCorrector;
            }
            if (positiveFiles != 0) {
                result += this.getPositiveCoefficient() / positiveFiles / Settings.accuracyCorrector;
            }
        }

        return result;
    }

    public Integer getNegativeMultiplyCoefficient() {
        int result = 0;

        if (this.getNegativeTotalFiles() > 10) {
//            Integer negativeFiles = this.getNegativeFiles();
//            if (negativeFiles == 0) {
//                negativeFiles = 1;
//            }
//            Integer negativeSuccessFiles = this.getNegativeSuccessFiles();
//            if (negativeSuccessFiles == 0) {
//                return 0;
//            }
//
//            int negativeFileDiff = (int) (100.0 / negativeFiles * this.getNegativeAllFiles());
//            int negativeFailureFileDiff = (int) (100.0 / negativeSuccessFiles * this.getNegativeSuccessAllFiles());
//
//            if (negativeFailureFileDiff != 0) {
//                result -= this.getPositiveFailureCoefficient() / negativeFailureFileDiff;
//            }
//
//            if (negativeFileDiff != 0) {
//                result +=this.getPositiveCoefficient() / negativeFileDiff;
//            }

            int negativeFiles = (int) (100.0 / this.getPositiveTotalFiles() * this.getNegativeFiles());
            int negativeSuccessFiles = (int) (100.0 / this.getNegativeTotalFiles() * this.getNegativeSuccessFiles());
            if (negativeSuccessFiles != 0) {
                result -= this.getNegativeSuccessCoefficient() / negativeSuccessFiles / Settings.accuracyCorrector;
            }
            if (negativeFiles != 0) {
                result += this.getNegativeCoefficient() / negativeFiles / Settings.accuracyCorrector;
            }
        }

//        System.out.println(String.format(
//            "%d\n%d\n%d\n%d\n%d\n%s",
//                result,
//                this.getNegativeSuccessCoefficient(),
//                this.getNegativeSuccessFiles(),
//                this.getNegativeTotalFiles(),
//                "------"
//        ));

        return result;
    }
}
