package job.data.holder;

import json.KnownData;

import java.util.HashMap;

public class MappedRiskDataCollection {
    private HashMap<Integer, MappedRiskDataCollection> childCollection = null;
    private KnownData knownData = null;

    public MappedRiskDataCollection(HashMap<Integer, MappedRiskDataCollection> childCollection)
    {
        this.childCollection = childCollection;
    }

    public MappedRiskDataCollection(KnownData knownData, Integer position)
    {
        this.knownData = knownData;
        this.knownData.index = position;
    }

    public boolean isKnownData()
    {
        return this.knownData != null;
    }

    public HashMap<Integer, MappedRiskDataCollection> getChildCollection() {
        return childCollection;
    }

    public MappedRiskDataCollection findItemByIndex(SearchData searchData)
    {
        MappedRiskDataCollection result = this.getChildCollection().get(searchData.index);

        if (result == null && searchData.allowApprox) {
            Integer closestIndex = null, indexApproxValue = null;
            for (int possibleIndexValue: this.getChildCollection().keySet()) {
                if (closestIndex == null) {
                    closestIndex = possibleIndexValue;
                    indexApproxValue = getApproxValue(searchData, possibleIndexValue);
                }

                int tmpApprox = this.getApproxValue(searchData, possibleIndexValue);

                if (tmpApprox < indexApproxValue) {
                    indexApproxValue = tmpApprox;
                    closestIndex = possibleIndexValue;
                }
            }

            result = this.getChildCollection().get(closestIndex);
        }

        return result;
    }

    private Integer getApproxValue(SearchData searchData, Integer closestIndex) {
        int indexApproxValue;
        indexApproxValue = searchData.index - closestIndex;
        if (indexApproxValue < 0) {
            indexApproxValue *= -1;
        }

        return indexApproxValue;
    }

    public KnownData getKnownData() {
        return knownData;
    }
}
