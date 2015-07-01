package store.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

import store.model.Product;

public class ManagerConnection implements Runnable {
	private DataInputStream input;
	private DataOutputStream output;
	private ManagerStoreController storeController;
	
	public ManagerConnection(Socket serverClientSocket, ManagerStoreController storeController) {
		this.storeController = storeController;
		
		try {
			this.input = new DataInputStream((serverClientSocket.getInputStream()));
			this.output = new DataOutputStream(serverClientSocket.getOutputStream());
		} catch(IOException e) {
			System.out.println("Error while creating IO on ServerClientConnection");
		}
	}
	
	public void run() {
		JSONObject commandBlock;
		String commandString;
		int errorCode;
		
		try {
			
			while(!(commandString = this.input.readUTF()).equals("quit")) {
				commandBlock = new JSONObject(commandString);
				
				if(commandBlock.get("id").equals("add_product")) {
					String name = commandBlock.getString("name");
					String provider = commandBlock.getString("provider");
					float price = (float) commandBlock.getDouble("price");
					int day = commandBlock.getInt("day");
					int month = commandBlock.getInt("month");
					int year = commandBlock.getInt("year");
					int quantity = commandBlock.getInt("quantity");
					int productId = commandBlock.getInt("product_id");
					
					errorCode = this.storeController.addProduct(productId, name, provider, price, day, month, year, quantity);
					output.writeInt(errorCode);
					
				} else if(commandBlock.get("id").equals("show_products")) {
					ArrayList<Product> products = this.storeController.getProducts();
					JSONObject productBlock = new JSONObject();
					
					//sending the size of the arraylist
					output.writeInt(products.size());
					
					for(Product product : products) {
						productBlock.put("name", product.getName());
						productBlock.put("provider", product.getProvider());
						productBlock.put("price", product.getPrice());
						productBlock.put("id", product.getId());
						productBlock.put("quantity", product.getQuantity());
						
						output.writeUTF(productBlock.toString());
					}
					
				} else if(commandBlock.get("id").equals("set_quantity")) {
					int id = commandBlock.getInt("prod_id");
					int quantity = commandBlock.getInt("quantity");
					
					errorCode = this.storeController.setProductQuantity(id, quantity);
					
					output.writeInt(errorCode);
					
				} else if(commandBlock.get("id").equals("generate_pdf")) {
					String dayOrMonth = commandBlock.getString("dayOrMonth");
					int day = commandBlock.getInt("day");
					int month = commandBlock.getInt("month");
					int year = commandBlock.getInt("year");

					this.storeController.generatePDF(dayOrMonth, day, month, year);
				}
				
			}
			
		} catch (Exception e) {
			System.err.println("Erro recebendo objetos no ManagerConnection.");
		}
		
	}
}
