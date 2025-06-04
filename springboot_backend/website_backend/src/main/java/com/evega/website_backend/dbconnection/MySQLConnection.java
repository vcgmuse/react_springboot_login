package com.evega.website_backend.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements DatabaseConnection {
    private String url = "";// System.getenv("DB_URL");
    private String username = "root"; // System.getenv("DB_USERNAME");
//    private String password = ""; // System.getenv("DB_PASSWORD");
    private String password = "QzBIsVHHjMGJhmsWhYPvQiUSDMUXsLEJ"; // System.getenv("DB_PASSWORD");

    @Override
    public Connection connect() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to MySQL", e);
        }
    }

    @Override
    public void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to disconnect from MySQL", e);
        }
    }
}
