package store.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
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
	public int addProduct(int productId, String name, String provider, float price, int day, int month, int year, int quantity) {
		int errorCode = ErrorConstants.SUCCESS;	
		ArrayList<Product> products = this.store.getProducts();
		
		if(products.stream().anyMatch(p -> p.getId() == productId))
			errorCode = ErrorConstants.SAME_ID;
		else if(products.stream().anyMatch(p -> p.getName().equals(name)))
			errorCode = ErrorConstants.SAME_NAME;
		else if(price < 0)
			errorCode = ErrorConstants.PRICE_LESS_0;
		else {
			Product product = new Product(productId, name, provider, price, day, month, year, quantity);
			this.store.addProduct(product);
			this.csvStoreManager.addProduct(product);
		}
		
		return errorCode;
	}
	
	public void generatePDF(String dayOrMonth, int day, int month, int year) {
		ArrayList<Purchase> purchases = this.store.getPurchases();
		String text = new String();
		Document document = new Document();
		
		if(dayOrMonth.equals("day")) {
			text = "Relatorio de vendas do dia " + day + "/" + month + "/" + year + ".\n";
			text += "ID do comprador, ID do produto, quantidade comprada, preço unitário, preço total, dia, mês, ano\n";
					

			for(Purchase purchase : purchases) {
				if(purchase.getDay() == day && purchase.getMonth() == month && purchase.getYear() == year) {
					text += purchase.toString() + "\n";
				}
			}
			
		} else {
			
			text = "Relatorio de vendas de qualquer dia do mês " + month + " e ano " + year + ".\n\n";
			text += "ID do comprador, ID do produto, quantidade comprada, preço unitário, preço total, dia, mês, ano\n";
			
			for(Purchase purchase : purchases) {
				if(purchase.getMonth() == month && purchase.getYear() == year) {
					text += purchase.toString() + "\n";
				}
			}
			
		}
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream("relatorio_"+day+"_"+month+"_"+year+".pdf"));
			
			document.open();
			Paragraph parag = new Paragraph();
			parag.add(text);
			document.add(parag);
			document.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}

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
