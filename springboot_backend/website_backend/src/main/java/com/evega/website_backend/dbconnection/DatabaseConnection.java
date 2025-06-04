package com.evega.website_backend.dbconnection;
import java.sql.Connection;

public interface DatabaseConnection {
    Connection connect();
    void disconnect(Connection connection);
}
