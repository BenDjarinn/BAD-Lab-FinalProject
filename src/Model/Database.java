package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Database {

	private static Database database;
	public Connection connection;
	public Statement statement;
	public ResultSet resultSet;
	public ResultSetMetaData resultMeta;
	public PreparedStatement preparedStatement;

	
	public Database() {
	
		try {
			String url = "jdbc:mysql://localhost:3306/seruput_teh_db";
			String user = "root";
			String password = "";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
 
			statement = connection.createStatement();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static Database getInstance() {
		if(database == null) {
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
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	

//	public ResultSet getData() {
//		try {	
//			preparedStatement = connection.prepareStatement("SELECT * FROM product;");
//			
//			resultSet = preparedStatement.executeQuery();
//			resultMeta = resultSet.getMetaData();
//
//		}catch (Exception e) {
//			//TODO: handle exception
//			System.out.println("failed to load to get data");
//		}
//		
//		return resultSet;
//
//	}

	public Connection getConnection() {
        return connection;
    }


	
	
	
	
	

	
	
}	


//public ResultSet getData() {
//try {
//	statement = connection.createStatement();
//	String sql = "SELECT * FROM product";
//	
//	resultSet = statement.executeQuery(sql);
//	this.productData = FXCollections.observableArrayList();
//	
//	
//
//}catch (Exception e) {
//	//TODO: handle exception
//	System.out.println("failed to load to get data");
//}
//
//return resultSet;
//
//
//}
	
