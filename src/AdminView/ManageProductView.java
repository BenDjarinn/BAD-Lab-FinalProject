package AdminView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.CartSceneViewController;
import Controller.HomeViewController;
import Model.Cart;
import Model.Database;
import Model.Product;
import UserView.CartSceneView;
import UserView.TransactionView;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ManageProductView extends Application {

    private int hargaPerUnit = 0;
    public int nilaiSpinner;
    String loggedInUser = LoginView.getLoggedInUsername();

    public ManageProductView(Stage stage) {
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
         
         homePageItem.setOnAction(event -> openHomeView(primaryStage));
         
         Menu manageMenu = new Menu("Manage Products");
         MenuItem manageprodItem = new MenuItem("Manage Products");
         manageMenu.getItems().add(manageprodItem);
         
         manageprodItem.setOnAction(event -> openMPView(primaryStage));
         
         Menu accountMenu = new Menu("Account");
         MenuItem logOutItem = new MenuItem("Log out");
         accountMenu.getItems().addAll(logOutItem);
         
         logOutItem.setOnAction(event -> openLoginScene(primaryStage));

         menuBar.getMenus().addAll(homeMenu, manageMenu, accountMenu);

         vbox.getChildren().add(menuBar);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE);

        Label welcomeLabel = new Label("Welcome, " + loggedInUser);
        welcomeLabel.setFont(new Font("System Bold", 15));
        AnchorPane.setLeftAnchor(welcomeLabel, 375.0);
        AnchorPane.setTopAnchor(welcomeLabel, 75.0);

        Label selectProductLabel = new Label("Select a Product to Update");
        AnchorPane.setLeftAnchor(selectProductLabel, 375.0);
        AnchorPane.setTopAnchor(selectProductLabel, 100.0);

        Label titleLabel = new Label("Manage Products");
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

        Button addProductBtn = new Button("Add Product");
        addProductBtn.setMinWidth(120);
        AnchorPane.setLeftAnchor(addProductBtn, 375.0);
        AnchorPane.setTopAnchor(addProductBtn, 190.0);
        
        Button updateProductBtn = new Button("Update Product");
        updateProductBtn.setMinWidth(120);
        AnchorPane.setLeftAnchor(updateProductBtn, 375.0);
        AnchorPane.setTopAnchor(updateProductBtn, 235.0);
        updateProductBtn.setVisible(false);
        
        Button removeProductBtn = new Button("Remove Product");
        removeProductBtn.setMinWidth(120);
        AnchorPane.setLeftAnchor(removeProductBtn, 505.0);
        AnchorPane.setTopAnchor(removeProductBtn, 235.0);
        removeProductBtn.setVisible(false);


        Label priceTotal = new Label("");
        AnchorPane.setLeftAnchor(priceTotal, 590.0);
        AnchorPane.setTopAnchor(priceTotal, 195.0);
        
        VBox addProductForm = new VBox(10);
        
        Label inputProductNameLabel = new Label("Input product name");
        inputProductNameLabel.setFont(new Font("System Bold", 15));
        
        Label inputProductPriceLabel = new Label("Input product price");
        inputProductPriceLabel.setFont(new Font("System Bold", 15));
        
        Label inputProductDescLabel = new Label("Input product description...");
        inputProductDescLabel.setFont(new Font("System Bold", 15));
        
        TextField inputProductName = new TextField();
        inputProductName.setPromptText("Input product name..");
        inputProductName.setPrefWidth(350);
        
        TextField inputProductPrice = new TextField();
        inputProductPrice.setPromptText("Input product price..");
        inputProductPrice.setPrefWidth(350);
        
        TextArea inputProductDesc = new TextArea();
        inputProductDesc.setPromptText("Input product description..");
        inputProductDesc.setPrefWidth(350);
        inputProductDesc.setPrefHeight(100);
        inputProductDesc.setWrapText(true);
        
        HBox addProductbuttonBox = new HBox(10);
        HBox updateProductbuttonBox = new HBox(10);
        HBox removeProductbuttonBox = new HBox(10);
        
        Button addProductBtnInForm = new Button("Add Product");
        addProductBtnInForm.setPrefWidth(125);
        
        Button updateProductBtnInForm = new Button("Update Product");
        updateProductBtnInForm.setPrefWidth(125);
        
        Button deleteProductBtnInForm = new Button("Delete Product");
        deleteProductBtnInForm.setPrefWidth(125);
        
        VBox updateProductForm = new VBox(10);
        
        Label updateProductLabel = new Label("Update Product");
        updateProductLabel.setFont(new Font("System Bold", 15));
        
        TextField updateProductPriceInput = new TextField();
        updateProductPriceInput.setPromptText("Input new price..");
        updateProductPriceInput.setPrefWidth(350);
        
        VBox deleteProductForm = new VBox(10);
        
        Label removeProductConfirmation = new Label("Are you sure, you want to remove this product?");
        removeProductConfirmation.setFont(new Font("System Bold", 15));
        removeProductConfirmation.setPrefWidth(400);
        
        
        
        
        addProductBtnInForm.setOnAction(event -> {
        	 Database database = Database.getInstance();
        	
        	 String productName = inputProductName.getText().trim();
        	 String productPriceStr = inputProductPrice.getText().trim();
        	 String productDesc = inputProductDesc.getText().trim();
        	 String productID = generateNextProductID();
        	   
        	 int productPrice = Integer.parseInt(productPriceStr);
        	 
        	 
        	 if (productName.isEmpty() || productPriceStr.isEmpty() || productDesc.isEmpty()) {
        	    showAlert("All fields must be filled out");
        	    return; 
        	 
        	 } else if(isProductNameUnique(productName) == false) {
        		 showAlert("Product name must be unique");
        	 } else if(productPrice <= 0) {
        		 showAlert("Product price must be more than 0");
        	 }
        	 else {
        		 saveProductToDatabase(productID, productName, productPrice, productDesc);
            	 showSuccessAlert("Product added successfully");
        	 }
        	   
        	 
        	 inputProductName.clear();
        	 inputProductPrice.clear();
        	 inputProductDesc.clear();
        	 
        	 openMPView(primaryStage);
        	    
        });
        
        updateProductBtnInForm.setOnAction(event -> {
            String selectedProduct = listView.getSelectionModel().getSelectedItem();
            
            HomeViewController handleProductValidation = new HomeViewController();
            
            String productID = handleProductValidation.getProductID(selectedProduct);

            String newPriceStr = updateProductPriceInput.getText().trim();
            int newPrice = Integer.parseInt(newPriceStr);

            if(newPrice <= 0) {
            	showAlert("Product price must be more than 0");
            } else {
            	updateProductPrice(productID, newPrice);
            	showSuccessAlert("Product updated successfully");
            }
            
            openMPView(primaryStage);
        });

        
        
        deleteProductBtnInForm.setOnAction(event -> {
        	 String selectedProduct = listView.getSelectionModel().getSelectedItem();
 
        	 deleteProductFromDatabase(selectedProduct);
             showSuccessAlert("Product deleted successfully");
             openMPView(primaryStage);
        });
        
        
        
        
        Button backButton1 = new Button("Back");
        backButton1.setPrefWidth(125); //Back button for add product form
        
        Button backButton2 = new Button("Back");
        backButton2.setPrefWidth(125); //Back button for update product form
        
        Button backButton3 = new Button("Back");
        backButton3.setPrefWidth(125); //Back button for remove product
        
        backButton1.setOnAction(event -> {
            openMPView(primaryStage);
        });
        
        backButton2.setOnAction(event -> {
            openMPView(primaryStage);
        });
        
        backButton3.setOnAction(event -> {
        	openMPView(primaryStage);
        });
        
        addProductbuttonBox.getChildren().addAll(addProductBtnInForm,backButton1);
        updateProductbuttonBox.getChildren().addAll(updateProductBtnInForm,backButton2);
        removeProductbuttonBox.getChildren().addAll(deleteProductBtnInForm,backButton3);
        
        
        addProductForm.getChildren().addAll(inputProductNameLabel,inputProductName,inputProductPriceLabel,inputProductPrice,inputProductDescLabel,inputProductDesc,addProductbuttonBox);
        
        updateProductForm.getChildren().addAll(updateProductLabel,updateProductPriceInput,updateProductbuttonBox);
        
        deleteProductForm.getChildren().addAll(removeProductConfirmation,removeProductbuttonBox);
        
        AnchorPane.setLeftAnchor(addProductForm, 375.0);
        AnchorPane.setTopAnchor(addProductForm, 195.0);
        
        addProductForm.setVisible(false);
        
        AnchorPane.setLeftAnchor(updateProductForm, 375.0);
        AnchorPane.setTopAnchor(updateProductForm, 190.0);
        
        updateProductForm.setVisible(false);
        
        AnchorPane.setLeftAnchor(deleteProductForm, 375.0);
        AnchorPane.setTopAnchor(deleteProductForm, 190.0);
        
        deleteProductForm.setVisible(false);
       
        ObservableList<String> items = new HomeViewController().fetchProductName();

        listView.setItems(items);

        HomeViewController handleProductValidation = new HomeViewController();
          

        listView.setOnMouseClicked(event -> {
            String selectedProduct = listView.getSelectionModel().getSelectedItem();
            
    
     handleProductValidation.handleProductSelection(selectedProduct, welcomeLabel, selectProductLabel, descriptionLabel, priceLabel);
            
     		
            
            hargaPerUnit = handleProductValidation.getProductPrice(selectedProduct);
            
            nilaiSpinner = quantitySpinner.getValue();

            updateProductBtn.setVisible(true);
        	removeProductBtn.setVisible(true);
            quantityLabel.setVisible(false);
            priceTotal.setVisible(false);
                      
            if(addProductForm.isVisible()) {
            	updateProductBtn.setVisible(false);
            	removeProductBtn.setVisible(false);
            	updateProductForm.setVisible(false);
            }
            
            if(updateProductForm.isVisible()) {
            	updateProductBtn.setVisible(false);
            	removeProductBtn.setVisible(false);
            }
            
            if(deleteProductForm.isVisible()) {
            	updateProductBtn.setVisible(false);
            	removeProductBtn.setVisible(false);
            }
            
            
           
        });
        
        quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            nilaiSpinner = newValue;
            priceTotal.setText("Total : Rp." + nilaiSpinner * hargaPerUnit);
             	
        });
        
        addProductBtn.setOnAction(event -> {
        	addProductForm.setVisible(true);
        	addProductBtn.setVisible(false);
        	updateProductBtn.setVisible(false);
        	removeProductBtn.setVisible(false);
        	listView.setPrefHeight(420);
        });
        
        updateProductBtn.setOnAction(event -> {
        	addProductBtn.setVisible(false);
        	updateProductForm.setVisible(true);
        	updateProductBtn.setVisible(false);
        	removeProductBtn.setVisible(false);
        });
        
        removeProductBtn.setOnAction(event -> {
        	deleteProductForm.setVisible(true);
        	removeProductBtn.setVisible(false);
        	updateProductBtn.setVisible(false);
        	addProductBtn.setVisible(false);
        });
        
        anchorPane.getChildren().addAll(welcomeLabel, selectProductLabel, titleLabel, listView, descriptionLabel, priceLabel, quantityLabel, quantitySpinner,priceTotal, addProductBtn,removeProductBtn,updateProductBtn,addProductForm,updateProductForm,deleteProductForm);

        vbox.getChildren().add(anchorPane);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private String generateNextProductID() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "")) {
            String sql = "SELECT productID FROM product ORDER BY productID DESC LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String lastProductID = resultSet.getString("productID");
                    int lastProductNumber = Integer.parseInt(lastProductID.substring(2));
                    int nextProductNumber = lastProductNumber + 1;
                    
                    return String.format("TE%03d", nextProductNumber);
                } else {
                    return "TE001";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; 
        }
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
            new AdminHomeView(stage);
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
    
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
	}
    
    private void openMPView(Stage stage) {
    	try {
            new ManageProductView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saveProductToDatabase(String productID, String productName, int productPrice, String productDesc) {
    	
    	try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "INSERT INTO product (productID, product_name, product_price, product_des) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productID);
                preparedStatement.setString(2, productName);
                preparedStatement.setInt(3, productPrice);
                preparedStatement.setString(4, productDesc);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateProductPrice(String productID, int newPrice) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "UPDATE product SET product_price = ? WHERE productID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, newPrice);
                preparedStatement.setString(2, productID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteProductFromDatabase(String productName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "DELETE FROM product WHERE product_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isProductNameUnique(String productName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String query = "SELECT * FROM product WHERE product_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productName);
                ResultSet resultSet = preparedStatement.executeQuery();
                return !resultSet.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
   
    public static void main(String[] args) {
        launch(args);
    }
}