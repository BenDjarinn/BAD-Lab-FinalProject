package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class RegisterView extends Application{

	private Scene scene;
	private BorderPane bp;
	private FlowPane fp;
	private GridPane gp;
	private Label registerLbl;
	private Label usernameLbl;
	private Label emailLbl;
	private Label passwordLbl;
	private Label confirmLbl;
	private Label pnumbLbl;	
	private Label addressLbl;
	private Label genderLbl;	
	private Label descLbl;	
	private Label loginLbl;	
	private RadioButton maleBtn;	
	private RadioButton femaleBtn;	
	private TextField usernameTF;
	private TextField emailTF;
	private PasswordField passwordTF;
	private PasswordField confirmTF;
	private TextField pnumbTF;
	private TextArea addressTA;
	private CheckBox alltermsBtn;
	private Button registerBtn;
	
	

	public static void main1(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public RegisterView(Stage stage) {
		try {
			this.start(stage);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		bp = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		bp.setCenter(gp);
		registerLbl = new Label("Register");
		usernameLbl = new Label("Username");
		emailLbl = new Label("Email");
		passwordLbl = new Label("Password");
		confirmLbl = new Label("Confirm Password");
		pnumbLbl = new Label("Phone Number");
		addressLbl = new Label("Address");
		genderLbl = new Label("Gender");
		descLbl = new Label("Have an account?");
		loginLbl = new Label(" login here");
		usernameTF = new TextField();
		emailTF = new TextField();
		passwordTF = new PasswordField();
		confirmTF = new PasswordField();
		pnumbTF = new TextField();
		addressTA = new TextArea();
		maleBtn = new RadioButton("male");
		femaleBtn = new RadioButton("female");
		alltermsBtn = new CheckBox("i agree to all terms and condition");
		gp.add(registerLbl, 0, 0);
		gp.add(usernameLbl, 0, 1);
		gp.add(usernameTF, 1, 1);
		gp.add(emailLbl, 0, 2);
		gp.add(emailTF, 1, 2);
		gp.add(passwordLbl, 0, 3);
		gp.add(passwordTF, 1, 3);
		gp.add(confirmLbl, 0, 4);
		gp.add(confirmTF, 1, 4);
		gp.add(pnumbLbl, 0, 5);
		gp.add(pnumbTF, 1, 5);
		gp.add(addressLbl, 0, 6);
		gp.add(addressTA, 1, 6);
		gp.add(genderLbl, 0, 7);
		gp.add(maleBtn, 1, 7);
		gp.add(femaleBtn, 2, 7);
		gp.add(alltermsBtn, 0, 8);
		gp.add(descLbl, 0, 9);
		gp.add(loginLbl, 1, 9);	
		
		scene = new Scene(bp, 720, 500);
	}
	
	private void layouting() {
		
	}
	
	public void start(Stage registerStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		layouting();
		registerStage.setScene(scene);
		registerStage.setTitle("Register");
		registerStage.show();
	}


	
	

}