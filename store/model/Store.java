package store.model;

import java.util.ArrayList;

public class Store {
	private ArrayList<Product> products;
	private ArrayList<User> users;
	private static Store instance;
	
	private Store() {
		this.products = new ArrayList<Product>();
		this.users = new ArrayList<User>();
	}
	
	public static Store getInstance() {
		if(instance == null) 
			instance = new Store();
		
		return instance;
	}
	
	public void addProduct(Product product) {
		instance.products.add(product);
	}
	
	public void addUser(User user) {
		instance.users.add(user);
		for(User us : instance.users)
			System.out.println(us.getId());
	}

	public ArrayList<Product> getProducts() {
		return instance.products;
	}

	public ArrayList<User> getUsers() {
		return instance.users;
	}
}
