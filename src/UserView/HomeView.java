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

public class HomeView extends Application {

    private int hargaPerUnit = 0;
    public int nilaiSpinner;
    String loggedInUser = LoginView.getLoggedInUsername();

    
    public HomeView(Stage stage) {
        try {
            this.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void start(Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setPrefSize(640, 400);

        MenuBar menuBar = new MenuBar();

        Menu homeMenu = new Menu("Home");
        MenuItem homePageItem = new MenuItem("HomePage");
        homeMenu.getItems().add(homePageItem);

        Menu cartMenu = new Menu("Cart");
        MenuItem myCartItem = new MenuItem("My Cart");

        myCartItem.setOnAction(event -> openCartScene(primaryStage));

        cartMenu.getItems().add(myCartItem);

        Menu accountMenu = new Menu("Account");
        MenuItem purchaseHistoryItem = new MenuItem("Purchase History");

        purchaseHistoryItem.setOnAction(event -> openTransactionScene(primaryStage));

        MenuItem logOutItem = new MenuItem("Log out");
        accountMenu.getItems().addAll(purchaseHistoryItem, logOutItem);
        logOutItem.setOnAction(event -> openLoginScene(primaryStage));

        menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);

        vbox.getChildren().add(menuBar);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE);

        Label welcomeLabel = new Label("Welcome, " + loggedInUser);
        welcomeLabel.setFont(new Font("System Bold", 15));
        AnchorPane.setLeftAnchor(welcomeLabel, 375.0);
        AnchorPane.setTopAnchor(welcomeLabel, 75.0);

        Label selectProductLabel = new Label("Select a product to view");
        AnchorPane.setLeftAnchor(selectProductLabel, 375.0);
        AnchorPane.setTopAnchor(selectProductLabel, 100.0);

        Label titleLabel = new Label("SeRuput Teh");
        titleLabel.setFont(new Font("System Bold", 30));
        AnchorPane.setLeftAnchor(titleLabel, 56.0);
        AnchorPane.setTopAnchor(titleLabel, 26.0);

        ListView<String> listView = new ListView<>();

        Label descriptionLabel = new Label("");
        descriptionLabel.setPrefWidth(350);
        descriptionLabel.setWrapText(true);

        AnchorPane.setLeftAnchor(descriptionLabel, 375.0);
        AnchorPane.setTopAnchor(descriptionLabel, 100.0);

        Label priceLabel = new Label("");
        AnchorPane.setLeftAnchor(priceLabel, 375.0);
        AnchorPane.setTopAnchor(priceLabel, 155.0);
        priceLabel.setFont(new Font(16));

        Label quantityLabel = new Label("");
        AnchorPane.setLeftAnchor(quantityLabel, 375.0);
        AnchorPane.setTopAnchor(quantityLabel, 195.0);

        AnchorPane.setLeftAnchor(listView, 56.0);
        AnchorPane.setTopAnchor(listView, 80.0);
        listView.setPrefSize(300.0, 300.0);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 10, 1);
        quantitySpinner.setEditable(true);
        AnchorPane.setLeftAnchor(quantitySpinner, 430.0);
        AnchorPane.setTopAnchor(quantitySpinner, 190.0);

        quantitySpinner.setVisible(false);

        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setMinWidth(120);
        AnchorPane.setLeftAnchor(addToCartBtn, 375.0);
        AnchorPane.setTopAnchor(addToCartBtn, 230.0);
        addToCartBtn.setVisible(false);

        Label priceTotal = new Label("");
        AnchorPane.setLeftAnchor(priceTotal, 590.0);
        AnchorPane.setTopAnchor(priceTotal, 195.0);
       
        ObservableList<String> items = new HomeViewController().fetchProductName();

        listView.setItems(items);

        HomeViewController handleProductValidation = new HomeViewController();
          

        listView.setOnMouseClicked(event -> {
            String selectedProduct = listView.getSelectionModel().getSelectedItem();
            
    
     handleProductValidation.handleProductSelection(selectedProduct, welcomeLabel, selectProductLabel, descriptionLabel, priceLabel);
            
     		
            
            hargaPerUnit = handleProductValidation.getProductPrice(selectedProduct);
            
            nilaiSpinner = quantitySpinner.getValue();
            
            quantityLabel.setText("Quantity : ");
            
            priceTotal.setText("Total : Rp." + nilaiSpinner * hargaPerUnit);
            quantitySpinner.setVisible(true);
            addToCartBtn.setVisible(true);
            
            quantitySpinner.getValueFactory().setValue(1);
        });
        
        quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            nilaiSpinner = newValue;
            priceTotal.setText("Total : Rp." + nilaiSpinner * hargaPerUnit);
             	
        });


        addToCartBtn.setOnAction(event -> {
        	String selectedProduct = listView.getSelectionModel().getSelectedItem();
            Integer quantity = quantitySpinner.getValue();
            String cartItem = quantity + "x " + selectedProduct + " (Rp." + nilaiSpinner * hargaPerUnit + ")";
            int hargaItem = nilaiSpinner * hargaPerUnit;
            String loggedInUser = LoginView.getLoggedInUsername();
       
            String productID = handleProductValidation.getProductID(selectedProduct);
            String userID = CartSceneViewController.getInstance().getUserID(loggedInUser);
            
            CartSceneViewController.getInstance().savePurchaseToDatabase(productID, userID, quantity);
            
            Cart cartItema = new Cart(productID, userID, quantity);
          

            
            
            if (CartSceneViewController.getInstance().isProductInCart(selectedProduct)) {
               
            	CartSceneViewController.getInstance().updateCartItem(selectedProduct, quantity, hargaPerUnit);
            	CartSceneViewController.getInstance().updatePurchaseToDatabase(productID, userID, nilaiSpinner);
            	
            	
            } else {
                CartSceneViewController.getInstance().addToCart(cartItem);
            }
            

            showSuccessAlert("Added to Cart");

         
        });

        anchorPane.getChildren().addAll(welcomeLabel, selectProductLabel, titleLabel, listView, descriptionLabel, priceLabel, quantityLabel, quantitySpinner, addToCartBtn, priceTotal);

        vbox.getChildren().add(anchorPane);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setTitle("Home");
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
