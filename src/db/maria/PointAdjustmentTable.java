package db.maria;

import concurrency.ResultRisk;
import db.value.object.PointAdjustmentEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PointAdjustmentTable {
    public void truncateData() throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE `data-science-challenge`.`point_adjustment`");
        ps.execute();
        ps.close();
        connection.close();
    }

    public void addAdjustmentData(List<ResultRisk> resultRisks) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `data-science-challenge`.`point_adjustment` (`existing_file_id`, `loan_id`, `index`, `change`, `known_success`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `index`= VALUES(`index`), `change` = VALUES(`change`)");
        for (ResultRisk resultRisk : resultRisks) {
            for (PointAdjustmentEntity pointAdjustmentEntity: resultRisk.getPointAdjustmentEntities()) {
                ps.setInt(1, pointAdjustmentEntity.getExistingFileId());
                ps.setInt(2, pointAdjustmentEntity.getLoanId());
                ps.setInt(3, pointAdjustmentEntity.getIndex());
                ps.setInt(4, pointAdjustmentEntity.getChange());
                ps.setBoolean(5, pointAdjustmentEntity.getKnownSuccess());

                ps.addBatch();
            }
        }

        ps.executeBatch();
        connection.commit();
        ps.close();
        connection.setAutoCommit(true);
        connection.close();
    }
}
