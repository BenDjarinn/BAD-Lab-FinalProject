package View;

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
	
	
	Label registerLink = new Label("Register here");
	
//	private void initialize() {
//		bp = new BorderPane();
//		fp = new FlowPane();
//		gp = new GridPane();
//		bp.setCenter(gp);
//		loginLbl = new Label("Login");
//		loginLbl.setFont(new Font("Arial", 40));
//		usernameLbl = new Label("Username :");
//		usernameTF = new TextField();
//		passwordLbl = new Label("Password :");
//		passwordTF = new PasswordField();
//		loginBtn = new Button("Login");
//		descLbl = new Label("Don't have an account yet?");
//		registerLbl = new Label(" Register here");
//		gp.add(loginLbl, 0, 0);
//		gp.add(usernameLbl, 0, 1);
//		gp.add(usernameTF, 1, 1);
//		gp.add(passwordLbl, 0, 2);
//		gp.add(passwordTF, 1, 2);
//		loginBtn = new Button("Login");
//		gp.add(descLbl, 1, 3);
//		gp.add(registerLbl, 2, 3);
//		gp.add(loginBtn, 1, 4);
//		scene = new Scene(bp, 620, 500);
//		
//	
//		
//	}
	
	public void initialize() {
	    bp = new BorderPane();
	    fp = new FlowPane();
	    gp = new GridPane();
	    bp.setCenter(gp);
	    loginLbl = new Label("Login");
	    loginLbl.setFont(new Font("Arial", 40));
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
	    scene = new Scene(bp, 720, 500);
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
        String username = usernameTF.getText().trim();
        String password = passwordTF.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("All fields must be filled");
        } else {
            // TODO: Perform authentication logic here
            // For now, just show a success message
            showAlert("Login successful");
        }
    }

	
	private void layouting() {
		
		loginBtn.setOnMouseClicked(event -> {
            try {
            	validateLogin();
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        });
		
		
		
		
		
		  
		
//		loginBtn.setOnMouseClicked(event-> {
//			try {
//				
//			}catch(Exception e) {
//				Alert alert = new Alert(Alert.AlertType.ERROR);
//				alert.setContentText("All fields must be filled");
//				alert.show();
//			}
//		});
//		
//		loginBtn.setOnMouseClicked(event->{
//			try {
//				
//			}catch(Exception e) {
//				Alert alert = new Alert(Alert.AlertType.ERROR);
//				alert.setContentText("invalid credential");
//				alert.show();
//			}
//		});
		
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		setPosition();
		layouting();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.show();
		changePage(primaryStage);
	
	}
	
	




	
	
	

}