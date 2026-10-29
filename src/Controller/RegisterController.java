package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Database;

/**
 * RegisterController.java
 *
 * Handles user registration logic including validation and database operations.
 *
 * REVAMP NOTES:
 * - 3 methods had hardcoded DriverManager.getConnection() pointing to wrong
 *   database (seruput_teh_db) with empty password.
 * - Fixed methods: isUsernameUnique(), generateNextUserID(), saveUserToDatabase().
 * - All now use Database.getInstance().getConnection().
 * - Removed unused import: java.sql.DriverManager.
 */

public class RegisterController {

    private static final RegisterController instance = new RegisterController();

    public static RegisterController getInstance() {
        return instance;
    }

    public boolean isUsernameUnique(String username) {
        try {
            Connection connection = Database.getInstance().getConnection();
            String query = "SELECT * FROM user WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateNextUserID() {
        try {
            Connection connection = Database.getInstance().getConnection();
            String sql = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastUserID = rs.getString("userID");
                int lastNum = Integer.parseInt(lastUserID.substring(2));
                return String.format("CU%03d", lastNum + 1);
            } else {
                return "CU001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUserToDatabase(String userID, String username, String password, String role, String address, String phone_num, String gender) {
        try {
            Connection connection = Database.getInstance().getConnection();
            String query = "INSERT INTO user (userID, username, password, role, address, phone_num, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userID);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setString(5, address);
            ps.setString(6, phone_num);
            ps.setString(7, gender);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}