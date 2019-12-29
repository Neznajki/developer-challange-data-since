package contract;

import db.value.object.TableColumnMetaData;

import java.lang.reflect.InvocationTargetException;

public interface MetaDataEntity {
    Integer getValueByFieldName(String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    TableColumnMetaData getMetaData(String field);
}
