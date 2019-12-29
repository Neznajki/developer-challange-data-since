package job.data.holder;

import json.CheckRule;
import json.KnownData;
import json.KnownHistoryFileContent;

import java.util.HashMap;
import java.util.List;

public class RiskDataHolder {
    private List<CheckRule> metaData;
    private MappedRiskDataCollection mappedRiskDataCollection;

    public RiskDataHolder(KnownHistoryFileContent fileContent)
    {
        this.metaData = fileContent.metaData;

        this.mappedRiskDataCollection = new MappedRiskDataCollection(new HashMap<>());
        int dataLength = this.metaData.size();
        for (KnownData knownData: fileContent.collection) {
            MappedRiskDataCollection tmpCollection = this.mappedRiskDataCollection;

            for (int i = 0; i< dataLength; i++) {
                Integer index = knownData.data[i];
                MappedRiskDataCollection existing = tmpCollection.getChildCollection().get(index);
                if (existing == null && i < dataLength - 1) {
                    existing = new MappedRiskDataCollection(new HashMap<>());
                    tmpCollection.getChildCollection().put(index, existing);
                }

                if (i < dataLength - 1) {
                    tmpCollection = existing;
                } else {
                    tmpCollection.getChildCollection().put(index, new MappedRiskDataCollection(knownData));
                }
            }
        }
    }

    public List<CheckRule> getMetaData() {
        return metaData;
    }

    public KnownData getKnownData(SearchData[] entityData)
    {
        KnownData result = null;
        MappedRiskDataCollection current = this.mappedRiskDataCollection;
        for (SearchData indexData: entityData) {
            MappedRiskDataCollection next = current.findItemByIndex(indexData);
            if (next == null) {
                break;
            }

            if (next.isKnownData()) {
                result = next.getKnownData();
            } else {
                current = next;
            }
        }

        return result;
    }
}
