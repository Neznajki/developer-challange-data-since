package db.maria;

import db.value.object.ExistingFileEntity;
import job.data.holder.RiskDataHolder;
import staticAccess.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExistingFilesTable {
    public void truncateData() throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE `data-science-challenge`.`existing_files`");
        ps.execute();
        ps.close();
        connection.close();
    }

    public void updateCreateFileEntity(List<RiskDataHolder> riskDataHolders) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        HashMap<String, RiskDataHolder> holderHashMap = new HashMap<>();
//        connection.setAutoCommit(false);
//        PreparedStatement ps = connection.prepareStatement("INSERT INTO `existing_files` (`file_name`) VALUES (?) ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id)");
        for (RiskDataHolder riskDataHolder : riskDataHolders) {
//            ps.setString(1, riskDataHolder.getFileName());
//            ps.addBatch();

            holderHashMap.put(riskDataHolder.getFileName(), riskDataHolder);
        }

//        ps.executeBatch();
//        connection.commit();
//        ps.close();
//        connection.setAutoCommit(true);

        String query = "SELECT * FROM `data-science-challenge`.`existing_files`";
        ResultSet rs = connection.createStatement().executeQuery(query);

        while (rs.next()) {
            ExistingFileEntity entity = new ExistingFileEntity(
                    rs.getInt("id"),
                    rs.getString("file_name").intern(),
                    rs.getInt("positive_ef_coef"),
                    rs.getInt("positive_success_coef"),
                    rs.getInt("negative_ef_coef"),
                    rs.getInt("negative_success_coef"),
                    rs.getInt("positive_files"),
                    rs.getInt("positive_success_files"),
                    rs.getInt("negative_files"),
                    rs.getInt("negative_success_files"),
                    rs.getInt("positive_total_files"),
                    rs.getInt("negative_total_files")
            );

            if (holderHashMap.get(entity.getFileName()) == null) {
                continue;
            }

            holderHashMap.get(entity.getFileName()).setExistingFileEntity(entity);
        }

        connection.close();
    }

    public List<String> ignorableFiles() throws SQLException {
        List<String> result = new ArrayList<>();
        Connection connection = ConnectionStorage.getConnection();
        String query = String.format("SELECT * FROM `data-science-challenge`.`existing_files` " +
                "WHERE (positive_ef_coef > %d AND negative_ef_coef < %d) OR " +
                "(positive_success_coef < %d AND negative_success_coef > %d)",
                Settings.positiveRequired,
                Settings.negativeRequired,
                Settings.positiveSuccessRequired,
                Settings.negativeSuccessRequired
        );
//        System.out.println(query);
        ResultSet rs = connection.createStatement().executeQuery(query);

        while (rs.next()) {
            result.add(rs.getString("file_name").intern());
        }

        connection.close();

        return result;
    }
}
