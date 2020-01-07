package db.maria;

import db.value.object.LoanEntity;
import staticAccess.Settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanTable {

    public List<LoanEntity> getTableDataForTerm() throws SQLException {
        ArrayList<LoanEntity> result = new ArrayList<>();

        String query = String.format(
            "SELECT l.* FROM `loan` as l JOIN `train_data_set` as tds on tds.loan_id = l.id WHERE tds.train_number = %d",
            Settings.trainNumber
        );
        Connection connection = ConnectionStorage.getConnection();
        ResultSet rs = connection.createStatement().executeQuery(query);

        while(rs.next()) {
            result.add(new LoanEntity(rs));
        }

        rs.close();
        connection.close();

        return result;
    }

    public List<LoanEntity> getTableDataForTerm(int term) throws SQLException {
        ArrayList<LoanEntity> result = new ArrayList<>();

        String query = String.format(
                "SELECT * FROM `loan` as l WHERE term >= %d\n" +
                        "#ORDER BY RAND()\n" +
                        "#LIMIT 1000" +
                        "", term);

        if (ConnectionStorage.isTrainDatabase()) {
            query = String.format(
                    "SELECT l.* FROM `loan` as l JOIN `train_data_set` as tds on tds.loan_id = l.id WHERE tds.train_number = %d AND term >= %d",
                    Settings.trainNumber, term
            );
        }

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
