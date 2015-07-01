package store.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

import store.model.Product;

public class ClientConnection implements Runnable {
	private DataInputStream input;
	private DataOutputStream output;
	private ClientStoreController clientStoreController;
	
	public ClientConnection(Socket serverClientSocket, ClientStoreController clientStoreController) {
		this.clientStoreController = clientStoreController;
		
		try {
			this.input = new DataInputStream((serverClientSocket.getInputStream()));
			this.output = new DataOutputStream(serverClientSocket.getOutputStream());
		} catch(IOException e) {
			System.out.println("Error while creating IO on ServerClientConnection");
		}
	}
	
	@Override
	public void run() {
		JSONObject commandBlock;
		String commandString;
		int errorCode;
		
		try {
			
			while(!(commandString = this.input.readUTF()).equals("quit")) {
				commandBlock = new JSONObject(commandString);
				
				if(commandBlock.get("id").equals("register_user")) {
					String name = commandBlock.getString("name");
					String email = commandBlock.getString("email");
					String id = commandBlock.getString("user_id");
					String password = commandBlock.getString("password");
					String address = commandBlock.getString("address");
					String telephoneNumber = commandBlock.getString("telephoneNumber");
					
					errorCode = this.clientStoreController.registerUser(name, email, id, password, 
																address, telephoneNumber);
					
					output.writeInt(errorCode);
					
				} else if(commandBlock.get("id").equals("login")) {
					String id = commandBlock.getString("user_id");
					String password = commandBlock.getString("password");
					
					errorCode = this.clientStoreController.login(id, password);
					
					output.writeInt(errorCode);
					
				} else if(commandBlock.get("id").equals("buy_product")) {
					String userId = commandBlock.getString("user_id");
					int productId = commandBlock.getInt("product_id");
					int quantity = commandBlock.getInt("quantity");
					
					errorCode = this.clientStoreController.buyProduct(userId, productId, quantity);
					
					output.writeInt(errorCode);
				} else if(commandBlock.get("id").equals("show_products")) {
					ArrayList<Product> products = this.clientStoreController.getProducts();
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
					
				}
				
			}
			
		} catch (Exception e) {
			System.err.println("Erro recebendo objetos no ClientConnection.");
		}
		
	}

}
