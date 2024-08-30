package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Database;
import Model.Product;
import Model.TransactionDetail;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class CartSceneViewController {
    private static final CartSceneViewController instance = new CartSceneViewController();
    private ObservableList<String> cartItems;

    private CartSceneViewController() {
        cartItems = FXCollections.observableArrayList();
    }

    public static CartSceneViewController getInstance() {
        return instance;
    }

    public ObservableList<String> getCartItems() {
        return cartItems;
    }
    
    public void addToCart(String item) {
        boolean productExists = false;
        for (int i = 0; i < cartItems.size(); i++) {
            String cartItem = cartItems.get(i);
            if (cartItem.contains(item)) {
                productExists = true;
                updateQuantityInCartItem(i, item);
                break;
            }
        }


        if (!productExists) {
            cartItems.add(item);
        }
    }
    
    public String extractProductName(String cartItem) {
        try {
        	int startIndex = cartItem.indexOf(" ") + 1;
            int endIndex = cartItem.indexOf("(") - 1;

            if (startIndex >= 0 && endIndex > startIndex) {
                return cartItem.substring(startIndex, endIndex).trim();
            } else {
                return "set";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public void moveCartToTransaction(String userID) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");

            connection.setAutoCommit(false);

            String transactionID = getNewTransactionID(connection);
 
            moveDataTotransaction_header(connection, transactionID, userID);
            moveDataTotransaction_detail(connection, transactionID);

            clearCart(connection, userID);

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  

	// Fungsi untuk mendapatkan transactionID baru
    public String getNewTransactionID(Connection connection) throws SQLException {
        String newTransactionID = "TR001"; // Default value if no record in the database

        String maxTransactionIDQuery = "SELECT MAX(transactionID) FROM transaction_header";
        try (PreparedStatement maxTransactionIDStatement = connection.prepareStatement(maxTransactionIDQuery);
             ResultSet resultSet = maxTransactionIDStatement.executeQuery()) {

            if (resultSet.next()) {
                String maxTransactionID = resultSet.getString(1);
                if (maxTransactionID != null) {
                    int numericPart = Integer.parseInt(maxTransactionID.substring(2)) + 1;
                    newTransactionID = String.format("TR%03d", numericPart);
                }
            }
        }

        return newTransactionID;
    }


    // Fungsi untuk memindahkan data dari cart ke transaction_header
    private void moveDataTotransaction_header(Connection connection, String transactionID, String userID) throws SQLException {
        String insertQuery = "INSERT INTO transaction_header (transactionID, userID) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, transactionID);
            insertStatement.setString(2, userID);
            insertStatement.executeUpdate();
        }
    }

    // Fungsi untuk memindahkan data dari cart ke transaction_detail
    private void moveDataTotransaction_detail(Connection connection, String transactionID) throws SQLException {
        String selectQuery = "SELECT * FROM cart";
        String insertQuery = "INSERT INTO transaction_detail (transactionID, productID, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = selectStatement.executeQuery();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                int quantity = resultSet.getInt("quantity");

                insertStatement.setString(1, transactionID);
                insertStatement.setString(2, productID);
                insertStatement.setInt(3, quantity);
                insertStatement.executeUpdate();
            }
        }
    }

    // Fungsi untuk menghapus data dari cart
    private void clearCart(Connection connection, String userID) throws SQLException {
        String deleteQuery = "DELETE FROM cart WHERE userID = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setString(1, userID);
            deleteStatement.executeUpdate();
        }
    }

    // Fungsi untuk melakukan rollback transaksi
    private void rollbackTransaction(Connection connection) {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

      
    
    private void updateQuantityInCartItem(int index, String newItem) {
        String existingItem = cartItems.get(index);
        int existingQuantity = extractQuantity(existingItem);
        int newQuantity = extractQuantity(newItem);
        int totalQuantity = existingQuantity + newQuantity;


        String updatedItem = newItem.replaceFirst("\\d+x", totalQuantity + "x");
        cartItems.set(index, updatedItem);
    }

    private int extractQuantity(String cartItem) {
        String[] parts = cartItem.split("x");
        return Integer.parseInt(parts[0].trim());
    }
    
    public boolean isProductInCart(String productName) {
        return cartItems.stream().anyMatch(item -> item.contains(productName));
    }

    public void updateCartItem(String productName, int newQuantity, int hargaItem) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).contains(productName)) {
                String[] parts = cartItems.get(i).split("x", 2);
                int existingQuantity = extractQuantity(cartItems.get(i));
                int totalQuantity = existingQuantity + newQuantity;
                
                String updatedItem = totalQuantity + "x " + productName + " (Rp." + totalQuantity * hargaItem + ")";
                cartItems.set(i, updatedItem);
                
                String productID = getProductIDFromCartItem(cartItems.get(i));
                String userID = "CU001"; 
                
                break;
            }
        }
    }
    
    
    private boolean isPurchaseExists(Connection connection, String productID, String userID) throws SQLException {
        String checkQuery = "SELECT * FROM cart WHERE productID = ? AND userID = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, productID);
            checkStatement.setString(2, userID);
            ResultSet resultSet = checkStatement.executeQuery();
            return resultSet.next();
        }
    }
    
    public String getProductIDFromCartItem(String cartItem) {
   
        String[] parts = cartItem.split("x", 2);
        return parts[1].trim();
    }
    
    
    
    public void savePurchaseToDatabase(String productID, String userID, int quantity) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            
            if (!isPurchaseExists(connection, productID, userID)) {
                String insertQuery = "INSERT INTO cart (productID, userID, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, productID);
                    insertStatement.setString(2, userID);
                    insertStatement.setInt(3, quantity);
                    insertStatement.executeUpdate();
                }
            } else {
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updatePurchaseToDatabase(String productID, String userID, int newQuantity) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            String selectQuery = "SELECT * FROM cart WHERE productID = ? AND userID = ?";
            String updateQuery = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                selectStatement.setString(1, productID);
                selectStatement.setString(2, userID);

                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int existingQuantity = resultSet.getInt("quantity");
                    int totalQuantity = existingQuantity + newQuantity;

                    updateStatement.setInt(1, totalQuantity);
                    updateStatement.setString(2, productID);
                    updateStatement.setString(3, userID);

                    updateStatement.executeUpdate();
                } else {
                    System.out.println("Item not found in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                String userID = resultSet.getString("userID");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String address = resultSet.getString("address");
                String phone_num = resultSet.getString("phone_num");
                String gender = resultSet.getString("gender");
                User user = new User(userID, username, password, role, address, phone_num, gender);
                userData.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }
    
    public ObservableList<TransactionDetail> fetchTransactionDetailData() {
        ObservableList<TransactionDetail> transactionDetailData = FXCollections.observableArrayList();
        try {
            Database database = Database.getInstance();
            ResultSet resultSet = database.executeQuery("SELECT * FROM transaction_detail");
            while (resultSet.next()) {
                String transactionID = resultSet.getString("transactionID");
                String productID = resultSet.getString("productID");
                int quantity = resultSet.getInt("quantity");
                TransactionDetail transactionDetail = new TransactionDetail(transactionID,productID,quantity);
                
                transactionDetailData.add(transactionDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionDetailData;
    }
    
    
    
    
    public void deleteCartFromDatabase(String productID) {
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
    
    
    public String getProductDesc(String productName) {
        ObservableList<Product> productList = fetchProductData();
        for (Product product : productList) {
            if (product.getProduct_name().equals(productName)) {
                return product.getProduct_des();
            }
        }
        return null; 
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
    
    public Integer getProductPrice(String productName) {
        ObservableList<Product> productList = fetchProductData();
        for (Product product : productList) {
            if (product.getProduct_name().equals(productName)) {
                return product.getProduct_price();
            }
        }
        return 0; 
    }
    
    public String getUserAddress(String username) {
        ObservableList<User> userList = fetchUserData();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user.getAddress();
            }
        }
        return null; 
    }
    
    public String getUserNumber(String username) {
        ObservableList<User> userList = fetchUserData();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user.getPhone_num();
            }
        }
        return null; 
    }
    
    
    public String getUserID(String username) {
        ObservableList<User> userList = fetchUserData();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user.getUserID();
            }
        }
        return null; 
    }
    
    public int getTransactionQty(String transactionID) {
    	 ObservableList<TransactionDetail> transactionList = fetchTransactionDetailData();
         for (TransactionDetail td : transactionList) {
             if (td.getTransactionID().equals(transactionID)) {
                 return td.getQuantity();
             }
         }
         return 0;
    }
    
    public ObservableList<String> fetchItemsInTransaction(String transactionID) {
        ObservableList<String> itemsInTransaction = FXCollections.observableArrayList();
        try {
            String query = "SELECT productID, quantity FROM transaction_detail WHERE transactionID = ?";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seruput_teh_db", "root", "");
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, transactionID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String productID = resultSet.getString("productID");
                    int quantity = resultSet.getInt("quantity");
                    String productName = getProductFromID(productID);
                    int productPrice = CartSceneViewController.getInstance().getProductPrice(productName);
                    int totalPrice = productPrice * quantity;
                    itemsInTransaction.add(quantity + "x " + productName + " (" + totalPrice + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsInTransaction;
    }
    
    private String getProductFromID(String productID) {
        ObservableList<Product> productList = CartSceneViewController.getInstance().fetchProductData();
        for (Product product : productList) {
            if (product.getProductID().equals(productID)) {
                return product.getProduct_name();
            }
        }
        return "";
    }
    
    public int getProductPriceFromName(String productName) {
        ObservableList<Product> productList = fetchProductData();
        for (Product product : productList) {
            if (product.getProduct_name().equals(productName)) {
                return product.getProduct_price();
            }
        }
        return -1; // Atau nilai yang tidak mungkin terjadi jika produk tidak ditemukan
    }


 
    private int calculateTotalFromListView(ListView<String> listView) {
        int total = 0;
        ObservableList<String> items = listView.getItems();

        for (String item : items) {

            String[] parts = item.split(" ");
            int quantity = Integer.parseInt(parts[0].replace("x", ""));
            String productName = parts[1];
            int productPrice = Integer.parseInt(parts[3].replace(")", "").replace("Rp.", ""));
            total += productPrice * quantity;
        }

        return total;
    }
    
    
    
    



}
