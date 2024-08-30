package Main;
import Model.Database;
import javafx.application.Application;
import javafx.stage.Stage;
import View.LoginView;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	
    	LoginView loginApp = new LoginView(primaryStage); 
        
    	try {
            loginApp.start(primaryStage); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	
    }
}


