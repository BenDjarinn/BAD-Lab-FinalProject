package Main;
import javax.swing.text.View;
import javafx.application.Application;
import javafx.stage.Stage;
import View.LoginView;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginView loginApp = new LoginView(); // Membuat objek dari kelas Login
        try {
            loginApp.start(primaryStage); // Memanggil metode start di objek Login
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
