package db.maria;

import db.value.object.LoanEntity;
import staticAccess.Settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanTable {

    public List<LoanEntity> getTableDataForTerm(int term) throws SQLException {
        ArrayList<LoanEntity> result = new ArrayList<>();

        String query = String.format("SELECT * FROM `loan` WHERE `term` >= %d", term);
        Connection connection = ConnectionStorage.getConnection();
        ResultSet rs = connection.createStatement().executeQuery(query);

        while(rs.next()) {
            result.add(new LoanEntity(rs));
        }

        rs.close();
        connection.close();

        return result;
    }
}
