package store.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.json.JSONObject;

public class ServerClientConnection implements Runnable {
	private DataInputStream input;
	private DataOutputStream output;
	private StoreController storeController;
	
	public ServerClientConnection(Socket serverClientSocket, StoreController storeController) {
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
			
			/*while((*/commandString = this.input.readUTF();/*) != null) {*/
				commandBlock = new JSONObject(commandString);
				
				if(commandBlock.get("id").equals("add_p")) {
					String name = commandBlock.getString("name");
					String provider = commandBlock.getString("provider");
					float price = (float) commandBlock.getDouble("price");
					int day = commandBlock.getInt("day");
					int month = commandBlock.getInt("month");
					int year = commandBlock.getInt("year");
					int quantity = commandBlock.getInt("quantity");
					
					System.out.println(name + " " + provider + " " + price + " " + day + " " +
							month + " " + year + " " + quantity);
					
					this.storeController.addProduct(name, provider, price, day, month, year, quantity);
					//output.writeInt(errorCode);
				}
			//}
			
		} catch (Exception e) {
			System.err.println("Erro recebendo objetos no ServerClientConnection.");
		}
		
	}
}
