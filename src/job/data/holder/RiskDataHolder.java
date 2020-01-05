package job.data.holder;

import db.value.object.ExistingFileEntity;
import json.CheckRule;
import json.KnownData;
import json.KnownHistoryFileContent;
import staticAccess.Helper;

import java.util.HashMap;
import java.util.List;

public class RiskDataHolder {
    private String fileName;
    private ExistingFileEntity existingFileEntity;
    private List<CheckRule> metaData;
    private MappedRiskDataCollection mappedRiskDataCollection;

    public RiskDataHolder(String fileName, KnownHistoryFileContent fileContent)
    {
        this.fileName = fileName.intern();
        this.metaData = fileContent.metaData;

        this.mappedRiskDataCollection = new MappedRiskDataCollection(new HashMap<>());
        int dataLength = this.metaData.size();
        for(int k = 0; k < fileContent.collection.size(); k++) {
            KnownData knownData = fileContent.collection.get(k);

            MappedRiskDataCollection tmpCollection = this.mappedRiskDataCollection;

            for (int i = 0; i< dataLength; i++) {
                String stringData = knownData.data[i];
                Integer index;
                if (stringData.matches("[0-9]+")) {
                    index = Integer.parseInt(stringData);
                } else {
                    index = Helper.convertStringComaToInteger(stringData);
                }

                MappedRiskDataCollection existing = tmpCollection.getChildCollection().get(index);
                if (existing == null && i < dataLength - 1) {
                    existing = new MappedRiskDataCollection(new HashMap<>());
                    tmpCollection.getChildCollection().put(index, existing);
                }

                if (i < dataLength - 1) {
                    tmpCollection = existing;
                } else {
                    tmpCollection.getChildCollection().put(index, new MappedRiskDataCollection(knownData, k));
                }
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public ExistingFileEntity getExistingFileEntity() {
        return existingFileEntity;
    }

    public void setExistingFileEntity(ExistingFileEntity existingFileEntity) {
        this.existingFileEntity = existingFileEntity;
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
