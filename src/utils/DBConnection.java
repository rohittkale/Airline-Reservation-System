package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Database Connection Utility Class
 * Handles MySQL database connectivity for Airline Reservation System
 */
public class DBConnection {

    // Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/airline";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    // Singleton connection instance
    private static Connection connection = null;

    /**
     * Private constructor to prevent instantiation
     */
    private DBConnection() {
        // Private constructor for singleton pattern
    }

    /**
     * Get database connection
     * @return Connection object
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load MySQL JDBC driver
                Class.forName(DRIVER_CLASS);

                // Establish connection
                connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully!");

            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Test database connection
     * @return true if connection is valid, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Execute SELECT query and return ResultSet
     * @param query SQL SELECT query
     * @param params Query parameters
     * @return ResultSet containing query results
     */
    public static ResultSet executeQuery(String query, Object... params) {
        try {
            PreparedStatement pstmt = getConnection().prepareStatement(query);

            // Set parameters if provided
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeQuery();

        } catch (SQLException e) {
            System.err.println("Query execution failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Execute INSERT, UPDATE, or DELETE query
     * @param query SQL query
     * @param params Query parameters
     * @return Number of affected rows
     */
    public static int executeUpdate(String query, Object... params) {
        try {
            PreparedStatement pstmt = getConnection().prepareStatement(query);

            // Set parameters if provided
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Update execution failed: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Close ResultSet safely
     * @param rs ResultSet to close
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * Close PreparedStatement safely
     * @param pstmt PreparedStatement to close
     */
    public static void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}