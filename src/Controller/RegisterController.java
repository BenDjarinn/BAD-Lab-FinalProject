package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

	private static final RegisterController instance = new RegisterController();
	
	public static RegisterController getInstance() {
        return instance;
    }
	
	
	public boolean isUsernameUnique(String username) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                return !resultSet.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public String generateNextUserID() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "")) {
            String sql = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String lastUserID = resultSet.getString("userID");
                    int lastUserNumber = Integer.parseInt(lastUserID.substring(2));
                    int nextUserNumber = lastUserNumber + 1;
                    
                    return String.format("CU%03d", nextUserNumber);
                } else {
                    return "CU001";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; 
        }
    }
	
	
  public void saveUserToDatabase(String userID, String username, String password, String role, String address, String phone_num, String gender) {
    	
    	try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "INSERT INTO user (userID, username, password, role, address, phone_num, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, role);
                preparedStatement.setString(5, address);
                preparedStatement.setString(6, phone_num);
                preparedStatement.setString(7, gender);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	

}
