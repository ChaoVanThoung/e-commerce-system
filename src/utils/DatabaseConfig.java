package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/ecommerce_system";
    private static final String username = "postgres";
    private static final String password = "6229";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, username,password);
        } catch (Exception exception){
            System.err.println("Error connecting to database."+exception.getMessage());
        }
        return null;
    }
}
