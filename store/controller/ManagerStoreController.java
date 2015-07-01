package store.controller;

import java.util.ArrayList;

import helper.ErrorConstants;
import store.model.*;

public class ManagerStoreController {
	private Store store;
	private CSVStoreManager csvStoreManager;
	
	public ManagerStoreController(CSVStoreManager csvStoreManager) {
		this.store = Store.getInstance();
		this.csvStoreManager = csvStoreManager;
		this.store.setProducts(csvStoreManager.loadProducts());
		this.store.setUsers(csvStoreManager.loadUsers());
		this.store.setPurchases(csvStoreManager.loadPurchases());
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
		else {
			Product product = new Product(name, provider, price, day, month, year, quantity);
			this.store.addProduct(product);
			this.csvStoreManager.addProduct(product);
		}
		
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
					csvStoreManager.clearFile("product");
					for(Product prod : products)
						csvStoreManager.addProduct(prod);
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
