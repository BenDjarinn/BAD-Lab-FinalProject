package View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import AdminView.AdminHomeView;
import Model.Database;
import UserView.HomeView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class LoginView extends Application{
	
	//Import Component
	
	private Scene scene;
	private BorderPane bp;
	private FlowPane fp;
	private GridPane gp;
	private Label loginLbl;
	private Label usernameLbl;
	private Label passwordLbl;
	private Label descLbl;
	private Label registerLbl;	
	private TextField usernameTF;
	private PasswordField passwordTF;
	private Button loginBtn;
	private static String username;
	private String password;
	private static String loggedInUsername;

	
	 public LoginView(Stage stage) {
			try {
				this.start(stage);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	 
	
	
	
	Label registerLink = new Label("Register here");

	public void initialize() {
	    bp = new BorderPane();
	    fp = new FlowPane();
	    gp = new GridPane();
	    bp.setCenter(gp);
	    loginLbl = new Label("Login");
	    loginLbl.setFont(new Font("System Bold", 40));
	    usernameLbl = new Label("Username :");
	    usernameTF = new TextField();
	    passwordLbl = new Label("Password :");
	    passwordTF = new PasswordField();
	    loginBtn = new Button("Login");
	    loginBtn.setMinWidth(120);
	    Label descLbl = new Label("Don't have an account yet? ");
	    
	    
	    
	    registerLink.setStyle("-fx-text-fill: blue;");
	    
	    
	    registerLink.setOnMouseEntered(event -> {
	        registerLink.setUnderline(true);
	    });

	    
	    registerLink.setOnMouseExited(event -> {
	        registerLink.setUnderline(false);
	    });
	    
	   

	    gp.add(loginLbl, 0, 0);
	    gp.add(usernameLbl, 0, 1);
	    gp.add(usernameTF, 1, 1);
	    gp.add(passwordLbl, 0, 2);
	    gp.add(passwordTF, 1, 2);
	    gp.add(loginBtn, 1,4);
	    gp.add(descLbl, 0, 3);
	    gp.add(registerLink, 1, 3); 
	    scene = new Scene(bp, 800, 600);
	}
	
	private void changePage(Stage stage) {
		registerLink.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY) { 
	        	 try {
	 	            new RegisterView(stage);
	 	        } catch (Exception e) {
	 	            e.printStackTrace();
	 	        }
	        }
	    });
	}
	
	 private void switchToHomeView(Stage stage) {
	        try {
	            HomeView homeView = new HomeView(stage);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	 private void switchToUserView(Stage stage) {
		 try {
	            HomeView homeView = new HomeView(stage);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	 private void switchToAdminView(Stage stage) {
	        try {
	            AdminHomeView adminHomeView = new AdminHomeView(stage);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	

	private void setPosition() {
		bp.setTop(loginLbl);
		BorderPane.setAlignment(loginLbl,Pos.CENTER);
		gp.setVgap(10);
		gp.setHgap(20);
		BorderPane.setAlignment(loginBtn,Pos.CENTER);
		usernameTF.setMinWidth(280);
		passwordTF.setMinWidth(280);
		gp.setPadding(new Insets(50));
		bp.setPadding(new Insets(70));
		
	}
	
	private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
	}
	
	
	
	private void validateLogin() {
        username = usernameTF.getText().trim();
        password = passwordTF.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("All fields must be filled");
        } 
    }
	
	public String getUserRole(String username) {
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "")) {
	        String query = "SELECT role FROM user WHERE username = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, username);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    return resultSet.getString("role");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	
	private void validateCredential(Stage stage) {
		username = usernameTF.getText().trim();
        password = passwordTF.getText().trim();
		
        if (isValidCredentials(username, password)) {
            String userRole = getUserRole(username);

            if (userRole != null) {
                switch (userRole) {
                    case "Admin":
                        switchToAdminView(stage);
                        break;
                    case "User":
                        switchToUserView(stage);
                        break;
                    case "Customer":
                        switchToUserView(stage);
                        break;
                    default:
                        showAlert("Unknown role for the user");
                }
            } else {
                showAlert("Unknown role for the user");
            }
        } else {
            showAlert("Invalid credential");
        }
    }
	
	
	 private boolean isValidCredentials(String username, String password) {
	        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "")) {
	            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setString(1, username);
	                preparedStatement.setString(2, password);

	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    return resultSet.next(); 
	                }
	            }
	            
	            
	         } catch (SQLException e) {
	            e.printStackTrace(); 
	            return false;
	        }
	    }
	 
	 

	 public static String getLoggedInUsername() {
	    	loggedInUsername = username;
	    	
	        return loggedInUsername;
	 }
	
	private void layouting(Stage primaryStage) {
		
		loginBtn.setOnMouseClicked(event -> {
            try {
            	validateLogin();
            	getLoggedInUsername();
            } catch (Exception e) {
                e.printStackTrace(); 
            }
            
            
            if (!(usernameTF.getText().isEmpty()) && !(passwordTF.getText().isEmpty())) {
                validateCredential(primaryStage);
            }
        });
		
	}
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		setPosition();
		layouting(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.show();
		changePage(primaryStage);
	
	}
	
	




	
	
	

}