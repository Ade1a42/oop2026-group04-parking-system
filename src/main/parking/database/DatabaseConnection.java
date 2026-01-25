package parking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.nvikiacxeckhymdasttx";
    private static final String PASSWORD = "Smart_parking_2026";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require", "postgres.nvikiacxeckhymdasttx", "Smart_parking_2026");
    }
}
