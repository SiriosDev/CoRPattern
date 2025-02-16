package it.unicalingsw.issuereportcorpattern.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    private static final String url = "jdbc:sqlite:";

    private static Connection connection;
    private static ConnectionManager instance;

    private ConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection(String database)  {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url + database);
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
