package com.evega.website_backend.dbconnection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    @Bean
    public DatabaseConnection databaseConnection() {
        String dbType = "MYSQL"; //System.getenv("DB_TYPE"); // e.g., "MYSQL" or "POSTGRES"
        if ("MYSQL".equalsIgnoreCase(dbType)) {
            return (DatabaseConnection) new MySQLConnection();
        } 
        	// Needs Implementation
        	// else if ("POSTGRES".equalsIgnoreCase(dbType)) {
            //return new PostgresConnection(); // Example of a different DB implementation
        //}
        
        throw new IllegalArgumentException("Unsupported database type: " + dbType);
    }
}
