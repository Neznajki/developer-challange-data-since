package db.value.object;

import contract.MetaDataEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanEntity implements MetaDataEntity {
    private static HashMap<String, TableColumnMetaData> metaData;

    public static HashMap<String, TableColumnMetaData> getMetaData() {
        if (metaData == null) {
            HashMap<String, TableColumnMetaData> collection = new HashMap<>();

            collection.put("id", new TableColumnMetaData("id", "int"));
            collection.put("gender_id", new TableColumnMetaData("gender_id", "object"));
            collection.put("guarantor_id", new TableColumnMetaData("guarantor_id", "object"));
            collection.put("employment_type_id", new TableColumnMetaData("employment_type_id", "object"));
            collection.put("employment_area_id", new TableColumnMetaData("employment_area_id", "object"));
            collection.put("employment_position_id", new TableColumnMetaData("employment_position_id", "object"));
            collection.put("marital_status_id", new TableColumnMetaData("marital_status_id", "object"));
            collection.put("loan_purpose_id", new TableColumnMetaData("loan_purpose_id", "object"));
            collection.put("income_source_id", new TableColumnMetaData("income_source_id", "object"));
            collection.put("payout_way_id", new TableColumnMetaData("payout_way_id", "object"));
            collection.put("living_address_term_id", new TableColumnMetaData("living_address_term_id", "object"));
            collection.put("client_age", new TableColumnMetaData("client_age", "byte"));
            collection.put("children_count", new TableColumnMetaData("children_count", "byte"));

            collection.put("term", new TableColumnMetaData("term", "int"));
            collection.put("income_per_month", new TableColumnMetaData("income_per_month", "int"));
            collection.put("amount", new TableColumnMetaData("amount", "float"));
            collection.put("known_success", new TableColumnMetaData("known_success", "boolean"));

            metaData = collection;
        }

        return metaData;
    }

    protected Integer id;
    protected Byte genderId;
    protected Byte guarantorId;
    protected Byte employmentTypeId;
    protected Byte employmentAreaId;
    protected Byte employmentPositionId;
    protected Byte maritalStatusId;
    protected Byte loanPurposeId;
    protected Byte incomeSourceId;
    protected Byte payoutWayId;
    protected Byte livingAddressTermId;
    protected Byte clientAge;
    protected Byte childrenCount;
    protected Integer term;
    protected Integer incomePerMonth;
    protected Float amount;
    protected boolean knownSuccess;

    protected List<HistoryEntity> historyEntityList = new ArrayList<>();

    public LoanEntity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.genderId = resultSet.getByte("gender_id");
        this.guarantorId = resultSet.getByte("guarantor_id");
        this.employmentTypeId = resultSet.getByte("employment_type_id");
        this.employmentAreaId = resultSet.getByte("employment_area_id");
        this.employmentPositionId = resultSet.getByte("employment_position_id");
        this.maritalStatusId = resultSet.getByte("marital_status_id");
        this.loanPurposeId = resultSet.getByte("loan_purpose_id");
        this.incomeSourceId = resultSet.getByte("income_source_id");
        this.payoutWayId = resultSet.getByte("payout_way_id");
        this.livingAddressTermId = resultSet.getByte("living_address_term_id");
        this.clientAge = resultSet.getByte("client_age");
        this.childrenCount = resultSet.getByte("children_count");
        this.term = resultSet.getInt("term");
        this.incomePerMonth = resultSet.getInt("income_per_month");
        this.amount = resultSet.getFloat("amount");
        this.knownSuccess = resultSet.getBoolean("known_success");
    }

    public Integer getId() {
        return id;
    }

    public Byte getGenderId() {
        return genderId;
    }

    public Byte getGuarantorId() {
        return guarantorId;
    }

    public Byte getEmploymentTypeId() {
        return employmentTypeId;
    }

    public Byte getEmploymentAreaId() {
        return employmentAreaId;
    }

    public Byte getEmploymentPositionId() {
        return employmentPositionId;
    }

    public Byte getMaritalStatusId() {
        return maritalStatusId;
    }

    public Byte getLoanPurposeId() {
        return loanPurposeId;
    }

    public Byte getIncomeSourceId() {
        return incomeSourceId;
    }

    public Byte getPayoutWayId() {
        return payoutWayId;
    }

    public Byte getLivingAddressTermId() {
        return livingAddressTermId;
    }

    public Byte getClientAge() {
        return clientAge;
    }

    public Byte getChildrenCount() {
        return childrenCount;
    }

    public Integer getTerm() {
        return term;
    }

    public Integer getIncomePerMonth() {
        return incomePerMonth;
    }

    public Float getAmount() {
        return amount;
    }

    public boolean isKnownSuccess() {
        return knownSuccess;
    }

    public List<HistoryEntity> getHistoryEntityList() {
        return historyEntityList;
    }

    public void addHistoryEntity(HistoryEntity historyEntity) {
        this.historyEntityList.add(historyEntity);
    }

    @Override
    public TableColumnMetaData getMetaData(String field)
    {
        return getMetaData().get(field);
    }

    @Override
    public Integer getValueByFieldName(String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TableColumnMetaData metaData = this.getMetaData(fieldName);
        try {
            Method method = LoanEntity.class.getMethod(metaData.getGetter());

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
        } catch (Exception e) {
            e.printStackTrace();
            getMetaData();
            throw e;
        }

    }
}
