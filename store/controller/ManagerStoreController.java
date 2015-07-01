package store.controller;

import java.util.ArrayList;

import Helper.ErrorConstants;
import store.model.*;

public class ManagerStoreController {
	private Store store;
	
	public ManagerStoreController() {
		this.store = Store.getInstance();
	}
	
	/*	Returns:
	 * SUCCESS -> product added.
	 * SAME_NAME -> there is already a product with this name. 
	 * PRICE_LESS_0 -> price is less than 0.
	 */
	public int addProduct(String name, String provider, float price, int day, int month, int year, int quantity) {
		int errorCode = ErrorConstants.SUCCESS;	
		ArrayList<Product> products = this.store.getProducts();
		
		if(products.stream().anyMatch(p -> p.getName().equals(name)))
			errorCode = ErrorConstants.SAME_NAME;
		else if(price < 0)
			errorCode = ErrorConstants.PRICE_LESS_0;
		else 
			this.store.addProduct(new Product(name, provider, price, day, month, year, quantity));
		
		return errorCode;
	}
	
	/*	Returns:
	 * SUCCESS -> user added.
	 * SAME_NAME -> there already is a user with this name.
	 * SAME_ID -> there already is a user with this id. 
	 */
	public int addUser(String name, String email, String id, String password,
			String address, String telephoneNumber) {
		int errorCode = ErrorConstants.SUCCESS;	
		ArrayList<User> users = this.store.getUsers();
		
		if(users.stream().anyMatch(u -> u.getName().equals(name)))
			errorCode = ErrorConstants.SAME_NAME;
		else if(users.stream().anyMatch(u -> u.getEmail().equals(email)))
			errorCode = ErrorConstants.SAME_EMAIL;
		else if(users.stream().anyMatch(u -> u.getId().equals(id)))
			errorCode = ErrorConstants.SAME_ID;
		else
			this.store.addUser(new User(name, email, id, password, address, telephoneNumber));
		
		return errorCode;
	}
	
	/*	Returns:
	 * SUCCESS -> product found and quantity updated.
	 * NOT_FOUND -> product with this id was not found.
	 * QUANTITY_LESS_0 -> quantity is less than 0.
	 */
	public int setProductQuantity(int id, int quantity) {
		int errorCode = ErrorConstants.NOT_FOUND;	
		ArrayList<Product> products = this.store.getProducts();
		
		if(quantity < 0)
			errorCode = ErrorConstants.QUANTITY_LESS_0;
		else {
			
			for(Product product : products) {
				if(product.getId() == id) {
					product.setQuantity(quantity);
					errorCode = ErrorConstants.SUCCESS;
					break;
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
