package store.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import org.json.*;


public class Manager {
	private DataOutputStream output;
	private DataInputStream input;
	private Scanner sysIn;
	
	public Manager(Socket socket) {
		sysIn = new Scanner(System.in);
		try {
			this.output = new DataOutputStream(socket.getOutputStream());
			this.input = new DataInputStream(socket.getInputStream());
		} catch(IOException e) {
			System.out.println("Error while creating output on ServerClient.");
		}
	}
	
	/* Description of the add product command block:
	 * id: "add_product"
	 * fields: String name
	 * 		   String provider
	 * 		   float price
	 * 		   int day
	 * 		   int month
	 * 		   int year
	 * 		   int quantity
	 */
	public int addProduct() {
		String name, provider;
		float price;
		int day, month, year, quantity;
		int codeError = -100;
		JSONObject commandBlock = new JSONObject();
		
		System.out.println("Adicione um produto");
		System.out.println();
		
		System.out.print("Nome: ");
		name = sysIn.nextLine();
		
		System.out.print("Fornecedor: ");
		provider = sysIn.nextLine();
		
		System.out.print("Preco: ");
		price = sysIn.nextFloat();
		
		System.out.print("Dia da validade: ");
		day = sysIn.nextInt();
		
		System.out.print("Mes da validade: ");
		month = sysIn.nextInt();
		
		System.out.print("Ano da validade: ");
		year = sysIn.nextInt();
		
		System.out.print("Quantidade: ");
		quantity = sysIn.nextInt();
		
		//flush
		sysIn.nextLine();
		
		try {
			commandBlock.put("id", "add_product");
			commandBlock.put("name", name);
			commandBlock.put("provider", provider);
			commandBlock.put("price", price);
			commandBlock.put("day", day);
			commandBlock.put("month", month);
			commandBlock.put("year", year);
			commandBlock.put("quantity", quantity);
		} catch (JSONException e1) {
			System.err.println("Erro no momento de criar o commandBlock em addProduct de Manager.");
		}

		
		try {
			this.output.writeUTF(commandBlock.toString());
		} catch (IOException e) {
			System.err.println("Erro de envio pro servidor. AddProduct do Manager.");
		}
		
		try {
			codeError = this.input.readInt();
		} catch (IOException e) {
			System.err.println("Erro de recebimento do servidor. AddProduct do Manager.");
		}
		
		return codeError;
	}

	/* Description of the show products command block:
	 * fields: none 
	 */	
	public void ShowProducts() {
		JSONObject commandBlock = new JSONObject();
		JSONObject productBlock;
		String productBlockString;
		
		int productsListSize;
		
		try {
			//creating the JSON commandBlock
			commandBlock.put("id", "show_products");

		
			//sending the JSON commandBlock
			this.output.writeUTF(commandBlock.toString());

			//receiving the number of products and printing all of it
			productsListSize = this.input.readInt();
			
			System.out.println("Nome | ID | Preco | Quantidade | Fornecedor");
			
			for(int i=0; i<productsListSize; i++) {
				productBlockString = this.input.readUTF();
				productBlock = new JSONObject(productBlockString);
				
				System.out.println(productBlock.getString("name") + " | " + 
						productBlock.getInt("id") + " | " + productBlock.getDouble("price") + 
						" | " + productBlock.getInt("quantity") + " | " + 
						productBlock.getString("provider"));
			}
			
		} catch (IOException e) {
			System.err.println("IOException no recebimento do servidor. AddProduct do Manager.");
		} catch (JSONException e) {
			System.err.println("JSONException no recebimento do servidor. AddProduct do Manager.");
		}
	}
	
	public int setProductQuantity() {
		int errorCode = -100;
		JSONObject commandBlock = new JSONObject();
		int id, quantity;
		
		System.out.println("Mude o estoque de um produto");
		System.out.println();
		
		System.out.print("ID do produto: ");
		id = sysIn.nextInt();
		
		System.out.print("Quantidade nova: ");
		quantity = sysIn.nextInt();
		
		//flush
		sysIn.nextLine();		
		
		try {
			commandBlock.put("id", "set_quantity");
			commandBlock.put("prod_id", id);
			commandBlock.put("quantity", quantity);
			
			output.writeUTF(commandBlock.toString());
			
			errorCode = input.readInt();
			
		} catch (IOException e) {
			System.err.println("IOException no recebimento do servidor. AddProduct do Manager.");
		} catch (JSONException e) {
			System.err.println("JSONException no recebimento do servidor. AddProduct do Manager.");
		}
				
		return errorCode;
	}
	
	public static void main(String[] args) {
		Socket managerSocket = null;
		
		if(args.length != 2) {
			System.err.println("Usage: java ServerClient <ip> <port number>");
			System.exit(1);
		}
		
		try {
			managerSocket = new Socket(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			System.out.println("Error while trying to connect the Manager.");
		}
		
		
		Manager manager = new Manager(managerSocket);
		manager.addProduct();
		manager.ShowProducts();
		manager.setProductQuantity();
		manager.ShowProducts();
	}
}