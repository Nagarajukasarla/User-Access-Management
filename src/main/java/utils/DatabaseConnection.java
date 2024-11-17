package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/uam";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Database driver not found. Please make sure you have installed.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(PreparedStatement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.info("Unable to close sql statement: " + e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.info("Unable to close Database connection: " + e);
            }
        }
    }
}
