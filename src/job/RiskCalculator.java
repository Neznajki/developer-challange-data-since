package job;

import contract.MetaDataEntity;
import db.value.object.HistoryEntity;
import db.value.object.LoanEntity;
import job.data.holder.RiskDataHolder;
import job.data.holder.SearchData;
import json.*;
import staticAccess.Helper;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.List;

public class RiskCalculator {

    private List<RiskDataHolder> riskDataHolders;

    public RiskCalculator(List<RiskDataHolder> riskDataHolders)
    {
        this.riskDataHolders = riskDataHolders;
    }

    public Integer getHistoryRiskPoints(LoanEntity loanEntity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer result = 0;

        for (HistoryEntity historyEntity: loanEntity.getHistoryEntityList()) {
            result += this.calculateSingleHistoryRecord(loanEntity, historyEntity);
        }

        return result;
    }

    protected Integer calculateSingleHistoryRecord(LoanEntity loanEntity, HistoryEntity historyEntity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int result = 0;
        int allowedSuccessPercent = 80, failPercent = 40;
        float delimiterStep = 5;

        for (RiskDataHolder knownHistoryFileContent: this.riskDataHolders) {
            KnownData knownData = this.getRiskByEntityData(loanEntity, historyEntity, knownHistoryFileContent);

            if (knownData == null) {
                continue;
            }

            if (knownData.total < 5) {
                continue;
            }

            if (knownData.successRate >= allowedSuccessPercent) {
                int ceil = (int) Math.ceil((knownData.successRate - allowedSuccessPercent) / delimiterStep * knownData.total / loanEntity.getHistoryEntityList().size());
                result -= ceil;
                Helper.successFound++;
            } else if (knownData.successRate <= failPercent) {
                int ceil = (int) Math.ceil((failPercent - knownData.successRate) * knownData.total / delimiterStep * loanEntity.getHistoryEntityList().size());
                result += ceil;
                Helper.failedFound++;
            }
            //            if ($knownRisk->getTotals() > 5) {
            //                $allowedSuccessPercent = 25;
            //
            //                if ($knownRisk->getRiskChance() >= 100-$allowedSuccessPercent) {
            //                    return (int)ceil((1+$allowedSuccessPercent - 80 + $knownRisk->getRiskChance()) / 5 * -$knownRisk->getSuccess());
            //                } elseif ($knownRisk->getRiskChance() <= $allowedSuccessPercent) {
            //                    return (int)ceil(1+$allowedSuccessPercent - $knownRisk->getRiskChance() / 5 * $knownRisk->getSuccess());
            //                }
            //            }
        }

        return result;
    }

    protected KnownData getRiskByEntityData(LoanEntity loanEntity, HistoryEntity historyEntity, RiskDataHolder riskDataHolder) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SearchData[] entityData = new SearchData[riskDataHolder.getMetaData().size()];
        int i =0;

        for (CheckRule metaData: riskDataHolder.getMetaData()) {
            MetaDataEntity getTargetEntity = this.getTargetEntity(metaData.primaryField, loanEntity, historyEntity);
            int value = getTargetEntity.getValueByFieldName(metaData.primaryField.field);

            if (metaData.divideBy != null) {
                value = (int) Math.ceil((float)value / metaData.divideBy);
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

        return riskDataHolder.getKnownData(entityData);
    }

    protected MetaDataEntity getTargetEntity(FieldMapping metaData, LoanEntity loanEntity, HistoryEntity historyEntity)
    {
        if (metaData.target == "loan") {
            return loanEntity;
        } else if (metaData.target == "history") {
            return historyEntity;
        } else {
            throw new InvalidParameterException("field target should be loan or history");
        }
    }
}
