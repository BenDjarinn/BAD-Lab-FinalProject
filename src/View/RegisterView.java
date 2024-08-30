package View;

import Controller.RegisterController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RegisterView extends Application {
	
	String userRole = "Customer";
	private String selectedGender;
	

    public RegisterView(Stage stage) {
        try {
            this.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Register");

        VBox root = new VBox();
        
        root.setAlignment(Pos.CENTER);  

        AnchorPane anchorPane = new AnchorPane();
        root.getChildren().add(anchorPane);

        Button registerButton = new Button("Register");
        registerButton.setLayoutX(225);
        registerButton.setLayoutY(470);
        registerButton.setMinWidth(100);

        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(226);
        passwordField.setLayoutY(112);
        passwordField.setPrefWidth(400);

        ToggleGroup genderToggleGroup = new ToggleGroup();

        RadioButton maleRadioButton = new RadioButton("Male");
        maleRadioButton.setLayoutX(224);
        maleRadioButton.setLayoutY(390);
        maleRadioButton.setToggleGroup(genderToggleGroup);

        RadioButton femaleRadioButton = new RadioButton("Female");
        femaleRadioButton.setLayoutX(300);
        femaleRadioButton.setLayoutY(390);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
       

        TextArea addressTextArea = new TextArea();
        addressTextArea.setLayoutX(225);
        addressTextArea.setLayoutY(224);

        TextField usernameTextField = new TextField();
        usernameTextField.setLayoutX(226);
        usernameTextField.setLayoutY(82);
        usernameTextField.setPrefWidth(400);

        Label usernameLabel = new Label("Username : ");
        usernameLabel.setLayoutX(95);
        usernameLabel.setLayoutY(88);

        Label passwordLabel = new Label("Password : ");
        passwordLabel.setLayoutX(95);
        passwordLabel.setLayoutY(119);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setLayoutX(226);
        confirmPasswordField.setLayoutY(145);
        confirmPasswordField.setPrefWidth(400);

        Label confirmPasswordLabel = new Label("Confirm Password : ");
        confirmPasswordLabel.setLayoutX(95);
        confirmPasswordLabel.setLayoutY(154);

        Label phoneNumberLabel = new Label("Phone Number : ");
        phoneNumberLabel.setLayoutX(95);
        phoneNumberLabel.setLayoutY(180);

        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.setLayoutX(226);
        phoneNumberTextField.setLayoutY(180);
        phoneNumberTextField.setPrefWidth(400);

        CheckBox termsCheckBox = new CheckBox("I agree to all terms and conditions");
        termsCheckBox.setLayoutX(226);
        termsCheckBox.setLayoutY(415);

        Label addressLabel = new Label("Address : ");
        addressLabel.setLayoutX(96);
        addressLabel.setLayoutY(225);
        addressTextArea.setPrefHeight(150);
        addressTextArea.setPrefWidth(400);

        Label genderLabel = new Label("Gender : ");
        genderLabel.setLayoutX(95);
        genderLabel.setLayoutY(390);

        Label haveAccountLabel = new Label("Have an account?");
        haveAccountLabel.setLayoutX(226);
        haveAccountLabel.setLayoutY(440);

        Label loginLabel = new Label("Login here");
        loginLabel.setLayoutX(335);
        loginLabel.setLayoutY(440);
        loginLabel.setTextFill(javafx.scene.paint.Color.valueOf("#002fff"));
        loginLabel.setUnderline(true);
        
        loginLabel.setOnMouseClicked(event -> {
	        if (event.getButton() == MouseButton.PRIMARY) { 
	        	 try {
	 	            new LoginView(primaryStage);
	 	        } catch (Exception e) {
	 	            e.printStackTrace();
	 	        }
	        }
	    });
        
        registerButton.setOnAction(event -> {
        	String username = usernameTextField.getText().trim();
        	String password = passwordField.getText().trim();
        	String confirmPassword = confirmPasswordField.getText().trim();
        	String phoneNumber = phoneNumberTextField.getText().trim();
        	String address = addressTextArea.getText().trim();
        	String userID = RegisterController.getInstance().generateNextUserID();
        	RadioButton selectedRadioButton;
            
            if (maleRadioButton.isSelected()) {
                selectedRadioButton = maleRadioButton;
            } else {
                selectedRadioButton = femaleRadioButton;
            }
            
            selectedGender = selectedRadioButton.getText();
            

        	
            if (username.isEmpty() || password.isEmpty() ||
            		confirmPassword.isEmpty() || phoneNumber.isEmpty() ||
            		address.isEmpty() || (!maleRadioButton.isSelected() && !femaleRadioButton.isSelected()) || !termsCheckBox.isSelected()) {
            	showAlert("All fields must be filled");
            } 
            else if(RegisterController.getInstance().isUsernameUnique(username) == false) {
            	showAlert("Username must be unique");
            }
            else if(username.length() < 5 || username.length() > 20) {
            	showAlert("Username must be 5-20 characters");
            }
            else if (!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$")) {
                showAlert("Password must be alphanumeric");
            }
            else if(password.length() < 5) {
            	showAlert("Password must be at least 5 characters");
            }
            else if(!password.equals(confirmPassword)) {
            	showAlert("Confirm password must equals to password.");
            }
            else if (phoneNumber.matches("^[a-zA-Z0-9]*$")) {
                showAlert("Phone Number must be numeric.");
            }
            else if(!phoneNumber.matches("\\+62[0-9]+")) {
            	showAlert("Phone number must start with ‘+62’.");
            }
            else {
            	showSuccessAlert("Registered Successfully!");
            	RegisterController.getInstance().saveUserToDatabase(userID, username, password, userRole, address, phoneNumber, selectedGender);
            	openLoginScene(primaryStage);
            }
            
            
        });

        Label titleLabel = new Label("Register");
        titleLabel.setLayoutX(320);
        titleLabel.setLayoutY(20);
        titleLabel.setFont(new Font("System Bold", 30));

        BorderPane borderPane = new BorderPane();
        anchorPane.getChildren().addAll(
                registerButton, passwordField, maleRadioButton, femaleRadioButton,
                addressTextArea, usernameTextField, usernameLabel, passwordLabel,
                confirmPasswordField, confirmPasswordLabel,phoneNumberLabel,
                phoneNumberTextField, termsCheckBox, addressLabel, genderLabel, haveAccountLabel,
                loginLabel, titleLabel, borderPane
        );

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
	}
    
    private void openLoginScene(Stage stage) {
        try {
            new LoginView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
