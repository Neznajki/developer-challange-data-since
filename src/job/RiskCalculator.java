package job;

import concurrency.ResultRisk;
import contract.MetaDataEntity;
import db.maria.ConnectionStorage;
import db.value.object.ExistingFileEntity;
import db.value.object.HistoryEntity;
import db.value.object.LoanEntity;
import db.value.object.PointAdjustmentEntity;
import job.data.holder.RiskDataHolder;
import job.data.holder.SearchData;
import json.*;
import staticAccess.Helper;
import staticAccess.Settings;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class RiskCalculator {

    private List<RiskDataHolder> riskDataHolders;

    public RiskCalculator(List<RiskDataHolder> riskDataHolders) {
        this.riskDataHolders = riskDataHolders;
    }

    public Integer getHistoryRiskPoints(LoanEntity loanEntity, ResultRisk loanRisk) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer result = 0;

        for (HistoryEntity historyEntity : loanEntity.getHistoryEntityList()) {
            result += this.calculateSingleHistoryRecord(loanEntity, historyEntity, loanRisk);
        }

        return result;
    }

    protected Integer calculateSingleHistoryRecord(LoanEntity loanEntity, HistoryEntity historyEntity, ResultRisk loanRisk) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int result = 0;
        int allowedSuccessPercent = 80, failPercent = 40;
        float delimiterStep = 1;

        for (RiskDataHolder knownHistoryFileContent : this.riskDataHolders) {
            KnownData knownData = this.getRiskByEntityData(loanEntity, historyEntity, knownHistoryFileContent);

            if (knownData == null) {
                continue;
            }

            if (knownData.total < 4) {
                continue;
            }

            ExistingFileEntity existingFileEntity = knownHistoryFileContent.getExistingFileEntity();
            int minCoef = 10;
            if (knownData.successRate >= allowedSuccessPercent) {
                if (
                        existingFileEntity.getNegativeCoefficient() < Settings.negativeRequired ||
                        existingFileEntity.getNegativeSuccessCoefficient() > Settings.negativeSuccessRequired
                ) {

                    continue;
                }

                int sumCoef = (existingFileEntity.getNegativeSuccessCoefficient() - existingFileEntity.getNegativeCoefficient()) * -1;
                if (sumCoef < minCoef) {
                    continue;
                }
                int coef = sumCoef ;

                int ceil = (int) Math.ceil((knownData.successRate - allowedSuccessPercent) / delimiterStep * knownData.total * coef);
                result -= ceil;
                Helper.successFound++;

                addTrace(loanEntity, loanRisk, knownHistoryFileContent, knownData, -ceil);
            } else if (knownData.successRate <= failPercent) {
                if (
                        existingFileEntity.getPositiveCoefficient() > Settings.positiveRequired ||
                        existingFileEntity.getPositiveFailureCoefficient() < Settings.positiveSuccessRequired
                ) {
                    continue;
                }
                int coef = existingFileEntity.getPositiveFailureCoefficient() - existingFileEntity.getPositiveCoefficient();
                if (coef < minCoef) {
                    continue;
                }

                int ceil = (int) Math.ceil((failPercent - knownData.successRate) * knownData.total * coef);
                result += ceil;
                Helper.failedFound++;
                addTrace(loanEntity, loanRisk, knownHistoryFileContent, knownData, ceil);
            }
        }

        return result;
    }

    private void addTrace(LoanEntity loanEntity, ResultRisk loanRisk, RiskDataHolder knownHistoryFileContent, KnownData knownData, int ceil) {
        if (ConnectionStorage.isTrainDatabase()) {
            loanRisk.addPointAdjustment(
                    new PointAdjustmentEntity(
                            knownHistoryFileContent.getExistingFileEntity().getId(),
                            loanEntity.getId(),
                            knownData.index,
                            ceil,
                            loanEntity.isKnownSuccess()
                    ));
        }
    }

    protected KnownData getRiskByEntityData(LoanEntity loanEntity, HistoryEntity historyEntity, RiskDataHolder riskDataHolder) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SearchData[] entityData = new SearchData[riskDataHolder.getMetaData().size()];
        int i = 0;

        for (CheckRule metaData : riskDataHolder.getMetaData()) {
            MetaDataEntity getTargetEntity = this.getTargetEntity(metaData.primaryField, loanEntity, historyEntity);
            int value = getTargetEntity.getValueByFieldName(metaData.primaryField.field);

            if (metaData.divideBy != null) {
                value = (int) Math.ceil((float) value / metaData.divideBy);
            }

            if (metaData.compareAgainst != null) {
                getTargetEntity = this.getTargetEntity(metaData.primaryField, loanEntity, historyEntity);
                Integer compareValue = getTargetEntity.getValueByFieldName(metaData.compareAgainst.field);

                if (value == 0) {
                    value = -1;
                } else {
                    value = (int) Math.ceil(100.0 / value * compareValue);
                }
            }

            SearchData currentSearch = new SearchData(value, getTargetEntity.getMetaData(metaData.primaryField.field).getType() != "object");
            entityData[i] = currentSearch;
            i++;
        }

        KnownData result = riskDataHolder.getKnownData(entityData);

//        if (result.successRate >= 80) {
//            this.printDebug(entityData, result);
//        }

        return result;
    }

    protected void printDebug(SearchData[] searchDataList, KnownData knownData) {
        List<String> searchValues = new ArrayList<>();

        for (SearchData searchData : searchDataList) {
            searchValues.add(String.valueOf(searchData.index));
        }


        System.out.println(String.join(",", searchValues));
        System.out.println(String.join(",", knownData.data));
        System.out.println("");
    }

    protected MetaDataEntity getTargetEntity(FieldMapping metaData, LoanEntity loanEntity, HistoryEntity historyEntity) {
        if (metaData.target == "loan") {
            return loanEntity;
        } else if (metaData.target == "history") {
            return historyEntity;
        } else {
            throw new InvalidParameterException("field target should be loan or history");
        }
    }
}
