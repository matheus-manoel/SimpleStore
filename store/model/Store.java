package store.model;

import java.util.ArrayList;

public class Store {
	private ArrayList<Product> products;
	private ArrayList<User> users;
	private ArrayList<Purchase> purchases;
	private static Store instance;
	
	private Store() {
		this.products = new ArrayList<Product>();
		this.users = new ArrayList<User>();
		this.purchases = new ArrayList<Purchase>();
	}
	
	public static Store getInstance() {
		if(instance == null) 
			instance = new Store();
		
		return instance;
	}
	
	public void addProduct(Product product) {
		instance.products.add(product);
	}
	
	public void addPurchase(Purchase purchase) {
		instance.purchases.add(purchase);
	}
	
	public void addUser(User user) {
		instance.users.add(user);
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public void setPurchases(ArrayList<Purchase> purchases) {
		this.purchases = purchases;
	}

	public ArrayList<Product> getProducts() {
		return instance.products;
	}

	public ArrayList<User> getUsers() {
		return instance.users;
	}
	
	public ArrayList<Purchase> getPurchases() {
		return instance.purchases;
	}
}
