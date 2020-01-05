package db.maria;

import db.value.object.HistoryEntity;
import db.value.object.LoanEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryTable {

    public List<LoanEntity> fillLoanData(List<LoanEntity> loanEntities) throws SQLException {

        List<String> idList = new ArrayList<>(loanEntities.size());
        HashMap<Integer, LoanEntity> mappedLoans = new HashMap<>();

        for (LoanEntity loan: loanEntities) {
            idList.add(loan.getId().toString());
            mappedLoans.put(loan.getId(), loan);
        }

//        String query = String.format("SELECT * FROM `history` WHERE `loan_id` IN (%s)", String.join(",", idList));
        String query = String.format("SELECT " +
                "loan_id," +
                "GROUP_CONCAT(DISTINCT `status` ORDER BY `status` SEPARATOR \",\") as `status`," +
                "AVG(delay5) as delay5," +
                "AVG(delay30) as delay30," +
                "AVG(delay60) as delay60," +
                "AVG(delay90) as delay90," +
                "AVG(delay_more) as delay_more," +
                "AVG(overdue_days) as overdue_days," +
                "AVG(loan_amount) as loan_amount," +
                "AVG(n_prolongations) as n_prolongations," +
                "AVG(unused_limit) as unused_limit," +
                "AVG(current_debt) as current_debt," +
                "AVG(overdue_sum) as overdue_sum," +
                "AVG(overdue_max) as overdue_max," +
                "AVG(total_amount_paid) as total_amount_paid," +
                "COUNT(*) as `count`" +
                " FROM `history` WHERE `loan_id` IN (%s) GROUP BY loan_id", String.join(",", idList));
        Connection connection = ConnectionStorage.getConnection();
        ResultSet rs = connection.createStatement().executeQuery(query);

        while(rs.next()) {
            int id = rs.getInt("loan_id");
            LoanEntity loan = mappedLoans.get(id);
            loan.addHistoryEntity(new HistoryEntity(rs, loan));
        }

        rs.close();
        connection.close();

        return loanEntities;
    }
}
