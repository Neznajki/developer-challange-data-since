package db.maria;

import db.value.object.FinalResultEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FinalResultTable {
    public void saveResults(List<FinalResultEntity> finalResultEntities) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE `final_result` SET `is_calculated` = 0");
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
}
