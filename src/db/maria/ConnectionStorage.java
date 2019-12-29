package db.maria;

import org.mariadb.jdbc.MariaDbPoolDataSource;
import staticAccess.Settings;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionStorage {
    private static MariaDbPoolDataSource _connectionPool;
    private static boolean _isTrainDatabase = false;

    public static void setDatabaseTest()
    {
        _isTrainDatabase = false;
    }

    public static Connection getConnection() throws SQLException {
        if (_connectionPool == null) {
            String database = "data-science-challenge-test";

            if (_isTrainDatabase) {
                database = "data-science-challenge";
            }

            _connectionPool = new MariaDbPoolDataSource(
                    "mysql",
                    3306,
                    database
            );

            _connectionPool.setUser("root");
            _connectionPool.setPassword("p1assword");
            _connectionPool.setMaxPoolSize(Settings.getMaxThreads());
//            _connectionPool.setUser();

            _connectionPool.initialize();
        }

        return _connectionPool.getConnection();
    }
}
