package db.value.object;

import contract.MetaDataEntity;
import staticAccess.Helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class HistoryEntity implements MetaDataEntity {
    private static HashMap<String, TableColumnMetaData> metaData;

    public static HashMap<String, TableColumnMetaData> getMetaData() {
        if (metaData == null) {
            HashMap<String, TableColumnMetaData> collection = new HashMap<>();

            collection.put("id", new TableColumnMetaData("id", "int"));
            collection.put("loan_id", new TableColumnMetaData("loan_id", "object"));
            collection.put("status", new TableColumnMetaData("status", "object"));
            collection.put("delay5", new TableColumnMetaData("delay5", "smallint"));
            collection.put("delay30", new TableColumnMetaData("delay30", "smallint"));
            collection.put("delay60", new TableColumnMetaData("delay60", "smallint"));
            collection.put("delay90", new TableColumnMetaData("delay90", "smallint"));
            collection.put("delay_more", new TableColumnMetaData("delay_more", "smallint"));
            collection.put("overdue_days", new TableColumnMetaData("overdue_days", "smallint"));
            collection.put("count", new TableColumnMetaData("count", "smallint"));

            collection.put("loan_amount", new TableColumnMetaData("loan_amount", "float"));
            collection.put("n_prolongations", new TableColumnMetaData("n_prolongations", "float"));
            collection.put("unused_limit", new TableColumnMetaData("unused_limit", "float"));
            collection.put("current_debt", new TableColumnMetaData("current_debt", "float"));
            collection.put("overdue_sum", new TableColumnMetaData("overdue_sum", "float"));
            collection.put("overdue_max", new TableColumnMetaData("overdue_max", "float"));
            collection.put("total_amount_paid", new TableColumnMetaData("total_amount_paid", "float"));

            collection.put("is_data_trash", new TableColumnMetaData("is_data_trash", "boolean"));

            metaData = collection;
        }

        return metaData;
    }

    protected LoanEntity loanEntity;

    protected Integer id;
    protected Integer loanId;
    protected Integer status;
    protected Short delay5;
    protected Short delay30;
    protected Short delay60;
    protected Short delay90;
    protected Short delayMore;
    protected Short overdueDays;
    protected Short count;

    protected Float loanAmount;
    protected Float nProlongations;
    protected Float unusedLimit;
    protected Float currentDebt;
    protected Float overdueSum;
    protected Float overdueMax;
    protected Float totalAmountPaid;

    protected boolean isDataTrash;

    public HistoryEntity(ResultSet resultSet, LoanEntity loanEntity) throws SQLException {
        this.loanEntity = loanEntity;
        setDataFromResultSet(resultSet);
    }

    public HistoryEntity(ResultSet resultSet) throws SQLException {
        setDataFromResultSet(resultSet);
    }

    private void setDataFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            this.id = resultSet.getInt("id");
            this.isDataTrash = resultSet.getBoolean("is_data_trash");
        }catch (Exception e) {}
        try {
            this.count = resultSet.getShort("count");
        } catch (Exception e) {}
        this.loanId = resultSet.getInt("loan_id");
        this.status = Helper.convertStringComaToInteger(resultSet.getString("status"));
        this.delay5 = resultSet.getShort("delay5");
        this.delay30 = resultSet.getShort("delay30");
        this.delay60 = resultSet.getShort("delay60");
        this.delay90 = resultSet.getShort("delay90");
        this.delayMore = resultSet.getShort("delay_more");
        this.overdueDays = resultSet.getShort("overdue_days");

        this.loanAmount = resultSet.getFloat("loan_amount");
        this.nProlongations = resultSet.getFloat("n_prolongations");
        this.unusedLimit = resultSet.getFloat("unused_limit");
        this.currentDebt = resultSet.getFloat("current_debt");
        this.overdueSum = resultSet.getFloat("overdue_sum");
        this.overdueMax = resultSet.getFloat("overdue_max");
        this.totalAmountPaid = resultSet.getFloat("total_amount_paid");


    }

    public LoanEntity getLoanEntity() {
        return loanEntity;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public Integer getStatus() {
        return status;
    }

    public Short getDelay5() {
        return delay5;
    }

    public Short getDelay30() {
        return delay30;
    }

    public Short getDelay60() {
        return delay60;
    }

    public Short getDelay90() {
        return delay90;
    }

    public Short getDelayMore() {
        return delayMore;
    }

    public Short getOverdueDays() {
        return overdueDays;
    }

    public Short getCount() {
        return count;
    }

    public Float getLoanAmount() {
        return loanAmount;
    }

    public Float getNProlongations() {
        return nProlongations;
    }

    public Float getUnusedLimit() {
        return unusedLimit;
    }

    public Float getCurrentDebt() {
        return currentDebt;
    }

    public Float getOverdueSum() {
        return overdueSum;
    }

    public Float getOverdueMax() {
        return overdueMax;
    }

    public Float getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public boolean isDataTrash() {
        return isDataTrash;
    }

    @Override
    public Integer getValueByFieldName(String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TableColumnMetaData metaData = this.getMetaData(fieldName);
        Method method = HistoryEntity.class.getMethod(metaData.getGetter());

        Object data = method.invoke(this);

        if (data instanceof Integer) {
            return (int) data;
        } else if (data instanceof Float) {
            return ((Float) data).intValue();
        } else if (data instanceof Byte) {
            return ((Byte) data).intValue();
        } else if (data instanceof Short) {
            return ((Short) data).intValue();
        } else {
            return (int) data;
        }
    }

    @Override
    public TableColumnMetaData getMetaData(String field)
    {
        return getMetaData().get(field);
    }
}
