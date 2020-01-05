package db.maria;

import db.value.object.ExistingFileEntity;
import job.data.holder.RiskDataHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ExistingFilesTable {
    public void truncateData() throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        PreparedStatement ps = connection.prepareStatement("TRUNCATE `existing_files`");
        ps.execute();
        ps.close();
        connection.close();
    }

    public void updateCreateFileEntity(List<RiskDataHolder> riskDataHolders) throws SQLException {
        Connection connection = ConnectionStorage.getConnection();
        HashMap<String, RiskDataHolder> holderHashMap = new HashMap<>();
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `existing_files` (`file_name`) VALUES (?)");
        for (RiskDataHolder riskDataHolder: riskDataHolders) {
            ps.setString(1, riskDataHolder.getFileName());
            ps.addBatch();

            holderHashMap.put(riskDataHolder.getFileName(), riskDataHolder);
        }

        ps.executeBatch();
        connection.commit();
        ps.close();
        connection.setAutoCommit(true);

        String query = "SELECT * FROM `existing_files`";
        ResultSet rs = connection.createStatement().executeQuery(query);

        while(rs.next()) {
            ExistingFileEntity entity = new ExistingFileEntity(rs.getInt("id"), rs.getString("file_name").intern());

            holderHashMap.get(entity.getFileName()).setExistingFileEntity(entity);
        }

        connection.close();
    }
}
