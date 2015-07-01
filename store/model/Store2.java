package store.model;

import java.util.ArrayList;

public class Store2 {
	private ArrayList<Product> products;
	private ArrayList<User> users;
	
	public Store2() {
		this.products = new ArrayList<Product>();
		this.users = new ArrayList<User>();
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public void addUser(User user) {
		this.users.add(user);
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public ArrayList<User> getUsers() {
		return users;
	}
}
