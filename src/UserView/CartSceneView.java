package UserView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Controller.CartSceneViewController;
import View.LoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CartSceneView extends Application {
	
	private ListView<String> cartList;
    private Label productNameLabel;
    private Label productDetailLabel;
    private Label productPriceLabel;
    private Label quantityLabel;
    private Spinner<Integer> quantitySpinner;
    private Label totalLabel;
    private Button updateCartButton;
    private Button removeFromCartButton;
    private Label totalPriceLabel;
    private Label orderInfoLabel;
    private Label usernameLabel;
    private Label phoneNumberLabel;
    private Label addressLabel;
    private Button makePurchaseButton;
    private MenuBar menuBar;
    private Label selectProductLabel;
    private Label welcomeLabel;
    private Label nameCart;
    private Label priceLabel;
    private Label spinnerLabel;
    private Button updateCartBtn;
    private Button removeCartBtn;
    private int nilaiSpinner;
    private int hargaPerUnit;
    private int itemQty;
    private int totalHarga = 0;
    String loggedInUser = LoginView.getLoggedInUsername();
    String userID = CartSceneViewController.getInstance().getUserID(loggedInUser);
    private Stage primaryStage;
    private Stage popupStage;

    public CartSceneView(Stage stage) {
    	 this.primaryStage = stage;
    	    try {
    	        this.start(stage);
    	    } 
    	    catch (Exception e) {
    	        e.printStackTrace();
    	    }
    }

    private ObservableList<String> cartItems = CartSceneViewController.getInstance().getCartItems();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cart Scene");

        initUI(primaryStage);

        BorderPane root = new BorderPane();
        root.setLeft(createcartListPane());
        root.setCenter(createProductDetailsPane());
        root.setTop(createMenuBarPane());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();

        cartItems.addListener((ListChangeListener<String>) change -> {
            Platform.runLater(() -> cartList.setItems(cartItems));
            updateWelcomeLabelAndSelectProductLabel();
        });
    }
    
    private class PopUpView {

        public void showPopup() {
            popupStage = new Stage();
            initializePopup(primaryStage);
            popupStage.showAndWait();
        }

        private void initializePopup(Stage ownerStage) {
            BorderPane root = new BorderPane();
            root.setPrefSize(472, 270);
            root.setStyle("-fx-background-color: grey;");

            Label titleLabel = new Label("Order Confirmation");
            titleLabel.setPrefSize(158, 27);
            titleLabel.setFont(Font.font("System Bold", 17));

            // Set alignment to center for the top element
            BorderPane.setAlignment(titleLabel, Pos.CENTER);
            root.setTop(titleLabel);

            AnchorPane bottomPane = new AnchorPane();
            bottomPane.setPrefSize(459, 241);
            bottomPane.setStyle("-fx-background-color: cadetblue;");

            Button yesButton = new Button("Yes");
            yesButton.setLayoutX(168);
            yesButton.setLayoutY(109);
            yesButton.setFont(Font.font("System Bold", 14));
            yesButton.setPrefWidth(100);
            yesButton.setPrefWidth(60);

            Button noButton = new Button("No");
            noButton.setLayoutX(245);
            noButton.setLayoutY(109);
            noButton.setFont(Font.font("System Bold", 14));
            noButton.setPrefWidth(60);

            yesButton.setOnAction(event -> {
                boolean transactionSuccessful = makePurchase();
                if (!transactionSuccessful) {
                    showFailedTransactionAlert("Failed to Make Transaction");
                } else {
                	CartSceneViewController.getInstance().moveCartToTransaction(userID);
                }
                popupStage.close();
            });

            noButton.setOnAction(event -> popupStage.close());

            Label questionLabel = new Label("Are you sure you want to make a purchase?");
            questionLabel.setLayoutX(90);
            questionLabel.setLayoutY(67);
            questionLabel.setFont(Font.font("System Bold", 16));

            bottomPane.getChildren().addAll(yesButton, noButton, questionLabel);

            root.setBottom(bottomPane);

            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.setTitle("Order Confirmation");
        }
    }

    private void initUI(Stage primaryStage) {
        menuBar = new MenuBar();

        Menu homeMenu = new Menu("Home");
        MenuItem homePageItem = new MenuItem("HomePage");
        homeMenu.getItems().add(homePageItem);
        homePageItem.setOnAction(event -> openHomeView(primaryStage));

        Menu cartMenu = new Menu("Cart");
        MenuItem myCartItem = new MenuItem("My Cart");
        cartMenu.getItems().add(myCartItem);

        Menu accountMenu = new Menu("Account");
        MenuItem purchaseHistoryItem = new MenuItem("Purchase History");
        purchaseHistoryItem.setOnAction(event -> openTransactionScene(primaryStage));

        MenuItem logOutItem = new MenuItem("Log out");
        accountMenu.getItems().addAll(purchaseHistoryItem, logOutItem);
        logOutItem.setOnAction(event -> openLoginScene(primaryStage));

        menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);

        cartList = new ListView<>(cartItems);
        cartList.setPrefHeight(200.0);
        cartList.setPrefWidth(350.0);

        productNameLabel = new Label();
        productDetailLabel = new Label();
        productPriceLabel = new Label();

        quantityLabel = new Label("Quantity:");
        quantityLabel.setPadding(new Insets(85,0,0,0));

        String userAddress = CartSceneViewController.getInstance().getUserAddress(loggedInUser);
        String userNumber = CartSceneViewController.getInstance().getUserNumber(loggedInUser);

        for (String cartItem : cartItems) {
            String extractedProduct = CartSceneViewController.getInstance().extractProductName(cartItem);
            int productPrice = CartSceneViewController.getInstance().getProductPrice(extractedProduct);

            char firstChar = cartItem.charAt(0);
            itemQty =Integer.parseInt(String.valueOf(firstChar));

            totalHarga += productPrice * itemQty;
        }

        quantitySpinner = new Spinner<>();

        int minValue = -100;
        int maxValue = 100;

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                minValue, maxValue, 0
        );

        quantitySpinner.setValueFactory(valueFactory);

        updateCartButton = new Button("Update Cart");
        removeFromCartButton = new Button("Remove From Cart");
        totalPriceLabel = new Label("Total: Rp." + totalHarga);
        orderInfoLabel = new Label("Order Information:");
        orderInfoLabel.setFont(new Font("System Bold", 12));
        usernameLabel = new Label("Username: " + loggedInUser);
        phoneNumberLabel = new Label("Phone Number: " + userNumber);
        addressLabel = new Label("Address: " + userAddress);
        makePurchaseButton = new Button("Make Purchase");
        makePurchaseButton.setOnAction(e -> showPurchaseConfirmationPopup());
        

        nameCart = new Label(loggedInUser + "'s cart");
        nameCart.setFont(new Font("System Bold", 30));

        welcomeLabel = new Label("Welcome, " + loggedInUser);
        welcomeLabel.setFont(new Font("System Bold", 15));

        selectProductLabel = new Label("Select a product to add and remove");

        priceLabel = new Label("");
        spinnerLabel = new Label("");

        updateCartBtn = new Button("Update Cart");
        updateCartBtn.setMinWidth(120);
        updateCartBtn.setVisible(false);

        removeCartBtn = new Button("Delete Cart");
        removeCartBtn.setMinWidth(120);
        removeCartBtn.setVisible(false);

        quantitySpinner.setVisible(false);

        updateWelcomeLabelAndSelectProductLabel();

        totalLabel = new Label(" ");

       

        cartList.setOnMouseClicked(event -> {
            String selectedCart = cartList.getSelectionModel().getSelectedItem();
            
            char firstChar = selectedCart.charAt(0);
            int itemQty = Integer.parseInt(String.valueOf(firstChar));

            String extractedSelectedCart = CartSceneViewController.getInstance().extractProductName(selectedCart);

            String productDesc = CartSceneViewController.getInstance().getProductDesc(extractedSelectedCart);

            Integer productPrice = CartSceneViewController.getInstance().getProductPrice(extractedSelectedCart);
            
            String productID = CartSceneViewController.getInstance().getProductID(extractedSelectedCart);

            totalLabel.setText("Total : Rp." + nilaiSpinner * productPrice);

            quantitySpinner.getValueFactory().setValue(1);
            

            welcomeLabel.setText(extractedSelectedCart);
            selectProductLabel.setText(productDesc);
            selectProductLabel.setPrefWidth(350);
            selectProductLabel.setWrapText(true);
            priceLabel.setText("Price: Rp." + productPrice);
            spinnerLabel.setText("Quantity : ");

            quantitySpinner.setVisible(true);
            removeCartBtn.setVisible(true);
            updateCartBtn.setVisible(true);
            totalLabel.setVisible(true);
            
            
            removeCartBtn.setOnAction(e -> {
            	removeFromCart(selectedCart);
            	deleteCartFromDatabase(productID);
            }
            	
            	
            );

            quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                nilaiSpinner = newValue;
                totalLabel.setText("Total : Rp." + nilaiSpinner * productPrice);
                
                
                String productName = CartSceneViewController.getInstance().extractProductName(selectedCart);
                
                updateCartBtn.setOnAction(e -> {
                	
                	String changedSelectedCart = cartList.getSelectionModel().getSelectedItem();
                    
                    char changedSelectedCartQty = changedSelectedCart.charAt(0);
                    int oldQty = Integer.parseInt(String.valueOf(changedSelectedCartQty));

                	
                	int newQty = nilaiSpinner + oldQty;

                	if(newQty == 0) {
                		removeFromCart(selectedCart);
                		deleteCartFromDatabase(productID);

                	} else if(newQty <= -1) {
                		showFailedTransactionAlert("Not a Valid Amount");
                	
                	} else if(nilaiSpinner == 0) {
                		showFailedTransactionAlert("Not a Valid Amount");
                	}
                	else {
                		CartSceneViewController.getInstance().updateCartItem(productName, nilaiSpinner, productPrice);
                		CartSceneViewController.getInstance().updatePurchaseToDatabase(productID, userID, nilaiSpinner);
                		showSuccessAlert("Updated Cart");
                		
                		totalHarga += nilaiSpinner * productPrice;
                    	totalPriceLabel.setText("Total: Rp." + totalHarga);
                	}

                });
                
            });
                     
        });
    }
     

    private VBox createMenuBarPane() {
        VBox MenuBarPane = new VBox(10);
        MenuBarPane.getChildren().add(menuBar);
        return MenuBarPane;
    }

    private VBox createcartListPane() {
        VBox cartListPane = new VBox(10);
        cartListPane.setPadding(new Insets(30,15,30,30));
        cartListPane.getChildren().addAll(nameCart, cartList,totalPriceLabel,
                orderInfoLabel,
                usernameLabel,
                phoneNumberLabel,
                addressLabel,
                makePurchaseButton);
        return cartListPane;
    }

    VBox productDetailsPane = new VBox(10);

    private VBox createProductDetailsPane() {
        HBox quantityBox = new HBox(10);
        quantityBox.getChildren().addAll(spinnerLabel, quantitySpinner, totalLabel);
        HBox updateDeleteBox = new HBox(5);
        updateDeleteBox.getChildren().addAll(updateCartBtn, removeCartBtn);

        productDetailsPane.setPadding(new Insets(85,0,0,0));
        productDetailsPane.getChildren().addAll(welcomeLabel,selectProductLabel,priceLabel,quantityBox,updateDeleteBox);
        return productDetailsPane;
    }

    private void updateWelcomeLabelAndSelectProductLabel() {
        if (cartItems.isEmpty()) {
            welcomeLabel.setText("No Item in Cart");
            selectProductLabel.setText("Consider adding one!");
        } else {
            welcomeLabel.setText("Welcome, " + loggedInUser);
            selectProductLabel.setText("Select a product to add and remove");
        }
    }

    private void removeFromCart(String selectedCart) {
    	if (selectedCart != null) {
    		String extractedSelectedCart = CartSceneViewController.getInstance().extractProductName(selectedCart);
    		String selectedProductID = CartSceneViewController.getInstance().getProductID(extractedSelectedCart);
    		
            Integer productPrice = CartSceneViewController.getInstance().getProductPrice(extractedSelectedCart);
            char firstChar = selectedCart.charAt(0);
            int itemQty = Integer.parseInt(String.valueOf(firstChar));
	        
            cartItems.remove(selectedCart);
            totalHarga -= productPrice * itemQty;
            totalPriceLabel.setText("Total: Rp." + totalHarga);
            showSuccessAlert("Deleted from Cart");
            CartSceneViewController.getInstance().deleteCartFromDatabase(selectedProductID);
    	}
    	
    	welcomeLabel.setText("");
        selectProductLabel.setText("");
        priceLabel.setText("");
        spinnerLabel.setText("");
        quantitySpinner.setVisible(false);
        removeCartBtn.setVisible(false);
        updateCartBtn.setVisible(false);
        totalLabel.setVisible(false);
    }
    
    
    public boolean makePurchase() {
        if (!cartItems.isEmpty()) {
            cartItems.clear(); 
            totalHarga = 0;
            totalPriceLabel.setText("Total: Rp." + totalHarga);
            showSuccessAlert("Transaction successful!");
            return true; 
        } else {
            return false; 
        }
    }
    
    private void deleteCartFromDatabase(String productID) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "DELETE FROM cart WHERE productID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void showFailedTransactionAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showPurchaseConfirmationPopup() {
    	PopUpView popUpView = new PopUpView();
        popUpView.showPopup();
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
    
   
    
    
    
}
