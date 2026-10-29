package Model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database.java
 *
 * Singleton class to manage a single shared MySQL database connection.
 *
 * REVAMP NOTES:
 * - All fields changed from public to private to enforce proper encapsulation.
 * - Previously, multiple View and Controller classes had their own hardcoded
 *   DriverManager.getConnection() calls (17 instances across 6 files).
 *   All connections now go through Database.getInstance().getConnection().
 * - Database credentials are now loaded from db.properties (excluded from
 *   version control via .gitignore). Copy db.properties.example to
 *   db.properties and fill in your credentials before running.
 */

public class Database {

    private static Database database;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData resultMeta;
    private PreparedStatement preparedStatement;

    public Database() {
        try {
            Properties props = new Properties();
            InputStream input = Database.class.getClassLoader().getResourceAsStream("db.properties");

            if (input == null) {
                throw new IOException(
                    "db.properties not found. " +
                    "Copy db.properties.example to db.properties and fill in your credentials."
                );
            }

            props.load(input);

            String url      = props.getProperty("db.url");
            String user     = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement  = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public ResultSet executeQuery(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}