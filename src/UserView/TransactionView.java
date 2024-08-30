package UserView;

import Controller.CartSceneViewController;
import Controller.HomeViewController;
import Model.Cart;
import Model.Product;
import View.LoginView;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class TransactionView extends Application {
	
	private Label selectTransactionLabel;
	private Label reminderLabel;
	String loggedInUser = LoginView.getLoggedInUsername();
	String loggedInUserID = CartSceneViewController.getInstance().getUserID(loggedInUser);
	ObservableList<String> transactionItems = new HomeViewController().fetchTransactionID(loggedInUserID);
	private ListView<String> listView;
	private ListView<String> cartList;
	private Label transactionIDLabel, username, phoneNumberLabel, addressLabel, totalLabel;
    
    int total = 0;
	

    public TransactionView(Stage stage) {
        try {
            this.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        VBox informationBox = new VBox(10);
        vbox.setPrefSize(640, 400);

        MenuBar menuBar = new MenuBar();
       
        AnchorPane.setLeftAnchor(informationBox, 220.0);
        AnchorPane.setTopAnchor(informationBox, 95.0);
        
        transactionIDLabel = new Label("Transaction ID:");
        transactionIDLabel.setFont(new Font("System Bold", 15));
        
        username = new Label("Username:");
        username.setFont(new Font("System Bold", 15));
        
        phoneNumberLabel = new Label("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", 15));
        
        addressLabel = new Label("Address:");
        addressLabel.setFont(new Font("Arial", 15));
        
        totalLabel = new Label("Total:");
        totalLabel.setFont(new Font("Arial", 15));
        
        informationBox.setVisible(false);

        Menu homeMenu = new Menu("Home");
        MenuItem homeMenuItem = new MenuItem("Home Page");
        homeMenuItem.setOnAction(event -> openHomeView(primaryStage));
        
        homeMenu.getItems().add(homeMenuItem);

        Menu cartMenu = new Menu("Cart");
        MenuItem myCartMenuItem = new MenuItem("My Cart");
        myCartMenuItem.setOnAction(event -> openCartScene(primaryStage));

        cartMenu.getItems().add(myCartMenuItem);

        Menu accountMenu = new Menu("Account");
        MenuItem logoutMenuItem = new MenuItem("Log out");
        logoutMenuItem.setOnAction(event -> openLoginScene(primaryStage));
        

        MenuItem purchaseHistoryMenuItem = new MenuItem("Purchase History");
        purchaseHistoryMenuItem.setOnAction(e -> {
        });

        accountMenu.getItems().addAll(purchaseHistoryMenuItem, logoutMenuItem);

        menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);

        vbox.getChildren().add(menuBar);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE);

        selectTransactionLabel = new Label("");
        selectTransactionLabel.setFont(new Font("System Bold", 15));
        AnchorPane.setLeftAnchor(selectTransactionLabel, 220.0);
        AnchorPane.setTopAnchor(selectTransactionLabel, 95.0);
        
        reminderLabel = new Label("");
        
        
        
        

        Label titleLabel = new Label(loggedInUser + "'s Purchase History");
        titleLabel.setFont(new Font("System Bold", 30));
        AnchorPane.setLeftAnchor(titleLabel, 56.0);
        AnchorPane.setTopAnchor(titleLabel, 26.0);

        listView = new ListView<>();
        cartList = new ListView<>();
        cartList.setVisible(false);

        AnchorPane.setLeftAnchor(listView, 56.0);
        AnchorPane.setTopAnchor(listView, 95.0);
        listView.setPrefSize(150.0, 350.0);
        
        
        AnchorPane.setLeftAnchor(cartList, 220.0);
        AnchorPane.setTopAnchor(cartList, 265.0);
        cartList.setPrefSize(350.0, 180.0);

        listView.setItems(transactionItems);
        
        
        
        if (listView.getItems().isEmpty()) {
            selectTransactionLabel.setText("There's No History");
            reminderLabel = new Label("Consider Purchasing Our Product");
            AnchorPane.setLeftAnchor(reminderLabel, 220.0);
            AnchorPane.setTopAnchor(reminderLabel, 120.0);
        } else {
            selectTransactionLabel.setText("Select a Transaction to view Details");
        }

        HomeViewController handleProductValidation = new HomeViewController();
        
        informationBox.getChildren().addAll(transactionIDLabel, username, phoneNumberLabel, addressLabel, totalLabel);
          
        anchorPane.getChildren().addAll(selectTransactionLabel,titleLabel, listView,reminderLabel,informationBox,cartList);

        vbox.getChildren().add(anchorPane);
        
        listView.setOnMouseClicked(event -> {
            String selectedTransaction = listView.getSelectionModel().getSelectedItem();
            String userAddress = CartSceneViewController.getInstance().getUserAddress(loggedInUser);
            String userNumber = CartSceneViewController.getInstance().getUserNumber(loggedInUser);
            int itemQty = CartSceneViewController.getInstance().getTransactionQty(selectedTransaction);

            informationBox.setVisible(true);
            cartList.setVisible(true);
            selectTransactionLabel.setVisible(false);
            transactionIDLabel.setText("Transaction ID : " + selectedTransaction);
            username.setText("Username : " + loggedInUser);
            phoneNumberLabel.setText("Phone Number: " + userNumber);
            addressLabel.setText("Address: " + userAddress);

            ObservableList<String> itemsInTransaction = CartSceneViewController.getInstance().fetchItemsInTransaction(selectedTransaction);
            cartList.setItems(itemsInTransaction);

            int total = 0; 
            
            for (String item : itemsInTransaction) {
                String productName = CartSceneViewController.getInstance().extractProductName(item);
                
                int productPrice = CartSceneViewController.getInstance().getProductPriceFromName(productName);
                char firstChar = item.charAt(0);
                int quantity = Integer.parseInt(String.valueOf(firstChar));
                
                total += productPrice * quantity;
                
            }
            
            
            
            totalLabel.setText("Total: Rp." + total);
        });


        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setTitle("History");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
       

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openHomeView(Stage stage) {
        try {
        	new HomeView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCartScene(Stage stage) {
        try {
            new CartSceneView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTransactionScene(Stage stage) {
        try {
            new TransactionView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLoginScene(Stage stage) {
        try {
            new LoginView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
