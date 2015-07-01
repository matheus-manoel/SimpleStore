package store.controller;

import java.util.ArrayList;

import helper.ErrorConstants;
import store.model.*;

public class ClientStoreController {
	private Store store;
	
	public ClientStoreController() {
		this.store = Store.getInstance();
	}
	
	/*	Returns:
	 * SUCCESS -> user added.
	 * SAME_NAME -> there already is a user with this name.
	 * SAME_ID -> there already is a user with this id. 
	 */
	public int registerUser(String name, String email, String id, String password,
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
	
	/*
	 * Returns a copy of products.
	 */
	public ArrayList<Product> getProducts() {
		return new ArrayList<Product>(this.store.getProducts());
	}
}
