package Model;

public class Cart {

	private String productID;
	private String userID;
	private int quantity;
	
	public Cart(String productID, String userID, int quantity) {
		super();
		this.productID = productID;
		this.userID = userID;
		this.quantity = quantity;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
	

}
