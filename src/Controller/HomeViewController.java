package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Model.Database;
import Model.Product;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class HomeViewController {
	
	
	private static final HomeViewController instance = new HomeViewController();

	public static HomeViewController getInstance() {
        return instance;
    }
	
	
    public ObservableList<String> fetchProductName() {
        ObservableList<String> productNameList = FXCollections.observableArrayList();
        try {
            Database database = Database.getInstance();
            ResultSet resultSet = database.executeQuery("SELECT product_name FROM product");
            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                productNameList.add(productName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productNameList;
    }
    
    
    
    public ObservableList<String> fetchTransactionID(String userID) {
        ObservableList<String> transactionList = FXCollections.observableArrayList();
        try {
            String query = "SELECT transactionID FROM transaction_header WHERE userID = ?";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            	preparedStatement.setString(1, userID);
            	ResultSet resultSet = preparedStatement.executeQuery();
            	
            	while (resultSet.next()) {
                    String transactionID = resultSet.getString("transactionID");
                    transactionList.add(transactionID);
                }
            
           
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionList;
    }

    
    

    public ObservableList<Product> fetchProductData() {
        ObservableList<Product> productData = FXCollections.observableArrayList();
        try {
            Database database = Database.getInstance();
            ResultSet resultSet = database.executeQuery("SELECT productID, product_name, product_price, product_des FROM product");
            while (resultSet.next()) {
                String productid = resultSet.getString("productID");
                String productName = resultSet.getString("product_name");
                String productDesc = resultSet.getString("product_des");
                Integer productPrice = resultSet.getInt("product_price");
                Product product = new Product(productid, productName, productPrice, productDesc);
                productData.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productData;
    }
    
    
    public ObservableList<User> fetchUserData() {
        ObservableList<User> userData = FXCollections.observableArrayList();
        try {
            Database database = Database.getInstance();
            ResultSet resultSet = database.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                String userid = resultSet.getString("userID");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String address = resultSet.getString("address");
                String phone_num = resultSet.getString("phone_num");
                String gender = resultSet.getString("gender");
               
                User user = new User(userid, username, password, role,address,phone_num,gender);
                userData.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    
    public void handleProductSelection(String selectedProduct, Label welcomeLabel, Label selectProductLabel, Label descriptionLabel, Label priceLabel) {
        if (selectedProduct != null) {
            welcomeLabel.setText(selectedProduct);
            String description = null;
            int price = 0;
            String productID = null;
            ObservableList<Product> productList = fetchProductData();
            for (Product product : productList) {
                if (product.getProduct_name().equals(selectedProduct)) {
                    description = product.getProduct_des();
                    price = product.getProduct_price();
                    productID = product.getProductID();

                    break;
                }
            }
            selectProductLabel.setText("");
            descriptionLabel.setText(description);
            priceLabel.setText("Price: Rp." + price);
        } else {
            welcomeLabel.setText("Welcome, Stefanie");
            selectProductLabel.setText("Select a product to view");
            descriptionLabel.setText("");
            priceLabel.setText("");
        }
    }
    
    public int getProductPrice(String productName) {
        ObservableList<Product> productList = fetchProductData();
        for (Product product : productList) {
            if (product.getProduct_name().equals(productName)) {
                return product.getProduct_price();
            }
        }
        return 0;
    }
   

    public void addToCart(String productName, int quantity) {

        System.out.println("Added to cart: " + quantity + " x " + productName);
    }
    
    
    public String getProductID(String productName) {
        ObservableList<Product> productList = fetchProductData();
        for (Product product : productList) {
            if (product.getProduct_name().equals(productName)) {
                return product.getProductID();
            }
        }
        return null; 
    }
    
    
}
