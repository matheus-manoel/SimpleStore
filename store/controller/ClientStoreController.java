package store.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import helper.ErrorConstants;
import store.model.*;

public class ClientStoreController {
	private Store store;
	private CSVStoreManager csvStoreManager;
	
	public ClientStoreController(CSVStoreManager csvStoreManager) {
		this.store = Store.getInstance();
		this.csvStoreManager = csvStoreManager;
	}
	
	/*	Returns:
	 * SUCCESS -> user added.
	 * SAME_NAME -> there already is a user with this name.
	 * SAME_ID -> there already is a user with this id. 
	 * SAME_EMAIL -> there already is a user with this email. 
	 */
	public synchronized int registerUser(String name, String email, String id, String password,
			String address, String telephoneNumber) {
		int errorCode = ErrorConstants.SUCCESS;	
		ArrayList<User> users = this.store.getUsers();
		
		if(users.stream().anyMatch(u -> u.getName().equals(name)))
			errorCode = ErrorConstants.SAME_NAME;
		else if(users.stream().anyMatch(u -> u.getEmail().equals(email)))
			errorCode = ErrorConstants.SAME_EMAIL;
		else if(users.stream().anyMatch(u -> u.getId().equals(id)))
			errorCode = ErrorConstants.SAME_ID;
		else {
			User user = new User(name, email, id, password, address, telephoneNumber);
			this.store.addUser(user);
			this.csvStoreManager.addUser(user);
		}
			
		return errorCode;
	}
	
	/*	Returns:
	 * SUCCESS -> user found and password matched.
	 * NOT_FOUND -> user with this id not found.
	 * WRONG_PASSWORD -> user found, but wrong password. 
	 */
	public int login(String id, String password) {
		int errorCode = ErrorConstants.NOT_FOUND;
		ArrayList<User> users = this.store.getUsers();
		
		for(User user : users) {
			if(user.getId().equals(id)) {
				
				if(user.getPassword().equals(password)) {
					errorCode = ErrorConstants.SUCCESS;
				} else {
					errorCode = ErrorConstants.WRONG_PASSWORD;
				}
				
			}
		}
		
		return errorCode;
	}
	
	/*	Returns:
	 * SUCCESS -> purchase made.
	 * NOT_FOUND -> product with this id wasnt found.
	 * QUANTITY_NOT_ENOUGH -> not enough units of this product to complete the purchase. 
	 */
	public synchronized int buyProduct(String buyerId, int productId, int quantity) {
		int errorCode = ErrorConstants.NOT_FOUND;
		ArrayList<Product> products = this.store.getProducts();
		
		for(Product product : products) {
			if(product.getId() == productId) {
				
				if(product.getQuantity() < quantity) {
					errorCode = ErrorConstants.QUANTITY_NOT_ENOUGH;
				} else {
					//creating the purchase
					GregorianCalendar today = new GregorianCalendar();
					today.setTime(new Date());
					Purchase purchase = new Purchase(buyerId, productId, quantity, product.getPrice(), product.getPrice()*quantity,
								today.get(GregorianCalendar.DAY_OF_MONTH), today.get(GregorianCalendar.MONTH), 
								today.get(GregorianCalendar.YEAR));
					
					//taking out the quantity of bought products
					product.setQuantity(product.getQuantity()-quantity);
					
					this.store.addPurchase(purchase);
					this.csvStoreManager.addPurchase(purchase);
					errorCode = ErrorConstants.SUCCESS;
				}
				
			}
		}
		
		return errorCode;
	}
	
	/*
	 * Returns a copy of products.
	 */
	public ArrayList<Product> getProducts() {
		return new ArrayList<Product>(this.store.getProducts());
	}
}
