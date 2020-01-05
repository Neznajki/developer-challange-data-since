package db.maria;

import db.value.object.FinalResultEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FinalResultTable {
    public void saveResults(List<FinalResultEntity> finalResultEntities) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE `final_result` SET `is_calculated` = 0, `is_good` = 0");
        ps.execute();
        ps.close();

        connection.setAutoCommit(false);
        ps = connection.prepareStatement("REPLACE INTO `final_result` (`id`,`is_good`,`risk_points`,`is_calculated`) VALUES (?,?,?,?)");
        for (FinalResultEntity finalResultEntity: finalResultEntities) {
            ps.setInt(1, finalResultEntity.getId());
            ps.setBoolean(2, finalResultEntity.isGood());
            ps.setInt(3, finalResultEntity.getRiskPoints());
            ps.setBoolean(4, true);
            ps.addBatch();
        }

        ps.executeBatch();
        connection.commit();
        ps.close();
        connection.setAutoCommit(true);
        connection.close();
    }

    public void displayFinalResults() throws SQLException {
        String query = "SELECT *, 100 / (successFound + failedFound) * failedFound as failPercent\n" +
                "FROM (SELECT COUNT(*)  as failedFound\n" +
                "FROM loan as l \n" +
                "JOIN final_result as fr on l.id = fr.id AND l.known_success != fr.is_good\n" +
                "WHERE known_success = 0) as a\n" +
                "JOIN (SELECT COUNT(*) as successFound\n" +
                "FROM loan as l \n" +
                "JOIN final_result as fr on l.id = fr.id AND l.known_success = fr.is_good\n" +
                "WHERE known_success = 1) as b";
        Connection connection = ConnectionStorage.getConnection();
        ResultSet rs = connection.createStatement().executeQuery(query);

        while(rs.next()) {
            System.out.println(String.format("failedFound %d, successFound %d, failPercent %s", rs.getInt("failedFound"), rs.getInt("successFound"), rs.getString("failPercent")));
        }

        rs.close();
        connection.close();
    }
}
