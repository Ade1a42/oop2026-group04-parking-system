package edu.aitu.oop3.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection{
    private static final String URL =
            "jdbc:postgresql://postgres:[YOUR-PASSWORD]@db.nvikiacxeckhymdasttx.supabase.co:5432/postgres";
    private static final String USER = "postgres.nvikiacxeckhymdasttx";
    private static final String PASSWORD = "Smart_parking_2026" ;
    private DatabaseConnection() {
// no instances
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
