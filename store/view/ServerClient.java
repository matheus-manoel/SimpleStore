package store.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import org.json.*;


public class ServerClient {
	private DataOutputStream output;
	private DataInputStream input;
	private Scanner sysIn;
	
	public ServerClient(Socket socket) {
		sysIn = new Scanner(System.in);
		try {
			this.output = new DataOutputStream(socket.getOutputStream());
			this.input = new DataInputStream(socket.getInputStream());
		} catch(IOException e) {
			System.out.println("Error while creating output on ServerClient.");
		}
	}
	
	/* Description of the add product command block:
	 * id: "add_p"
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
		int codeError = 0;
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
		
		try {
			commandBlock.put("id", "add_p");
			commandBlock.put("name", name);
			commandBlock.put("provider", provider);
			commandBlock.put("price", price);
			commandBlock.put("day", day);
			commandBlock.put("month", month);
			commandBlock.put("year", year);
			commandBlock.put("quantity", quantity);
		} catch (JSONException e1) {
			System.err.println("Erro no momento de criar o commandBlock em addProduct de ServerClient.");
		}

		
		try {
			this.output.writeUTF(commandBlock.toString());
		} catch (IOException e) {
			System.err.println("Erro de envio pro servidor. AddProduct do ServerClient.");
		}
		
	/*	try {
			codeError = this.input.readInt();
		} catch (IOException e) {
			System.err.println("Erro de recebimento do servidor. AddProduct do ServerClient.");
		}
	*/	
		return codeError;
	}
	
	
	
	
	public static void main(String[] args) {
		Socket serverClientSocket = null;
		
		if(args.length != 2) {
			System.err.println("Usage: java ServerClient <ip> <port number>");
			System.exit(1);
		}
		
		try {
			serverClientSocket = new Socket(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			System.out.println("Error while trying to connect the serverClient.");
		}
		
		
		ServerClient serverClient = new ServerClient(serverClientSocket);
		serverClient.addProduct();
	}
}