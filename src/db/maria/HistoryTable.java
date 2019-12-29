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

        String query = String.format("SELECT * FROM `history` WHERE `loan_id` IN (%s)", String.join(",", idList));
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
