package store.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import helper.ErrorConstants;
import helper.MD5;

public class Client {
	private DataOutputStream output;
	private DataInputStream input;
	private String id;
	private Scanner sysIn;
	
	public Client(Socket socket) {
		this.id = null;
		sysIn = new Scanner(System.in);
		try {
			this.output = new DataOutputStream(socket.getOutputStream());
			this.input = new DataInputStream(socket.getInputStream());
		} catch(IOException e) {
			System.out.println("Error while creating output on ServerClient.");
		}
	}
	
	/* Description of the show products command block:
	 * id: "register_user"
	 * fields: String name
	 * 		   String email
	 * 		   String id;
	 * 		   String password;
	 * 		   String address;
	 * 		   String telephoneNumber;
	 */
	public int registerUser() {
		String name, email, id, password, encryptedPassword, address, telephoneNumber;
		JSONObject commandBlock = new JSONObject();
		int errorCode = -100;
		
		System.out.println("Cadastro de Usuario");
		System.out.println();
		
		System.out.print("Nome: ");
		name = sysIn.nextLine();
		
		System.out.print("Email: ");
		email = sysIn.nextLine();
		
		System.out.print("id: ");
		id = sysIn.nextLine();
		
		System.out.print("Senha: ");
		password = sysIn.nextLine();
		
		System.out.print("Endereco: ");
		address = sysIn.nextLine();
		
		System.out.print("Telefone: ");
		telephoneNumber = sysIn.nextLine();
		
		encryptedPassword = MD5.crypt(password);
		
		try {
			commandBlock.put("id", "register_user");
			commandBlock.put("name", name);
			commandBlock.put("email", email);
			commandBlock.put("user_id", id);
			commandBlock.put("password", encryptedPassword);
			commandBlock.put("address", address);
			commandBlock.put("telephoneNumber", telephoneNumber);
			
			output.writeUTF(commandBlock.toString());
			
			errorCode = input.readInt();
		} catch (JSONException e) {
			System.err.println("JSONException em registerUser no Client.");
		} catch (IOException e) {
			System.err.println("IOException em registerUser no Client.");
		}
		
		return errorCode;
	}
	
	/* Description of the show products command block:
	 * id: "login"
	 * fields: String id;
	 * 		   String password;
	 */
	public int login() {
		int errorCode = -100;
		String id, password, encryptedPassword;
		JSONObject commandBlock = new JSONObject();
		
		System.out.println("Login");
		System.out.println();
		
		System.out.print("id: ");
		id = sysIn.nextLine();
		
		System.out.print("Senha: ");
		password = sysIn.nextLine();
		
		encryptedPassword = MD5.crypt(password);
		
		try {
			commandBlock.put("id", "login");
			commandBlock.put("user_id", id);
			commandBlock.put("password", encryptedPassword);
			
			output.writeUTF(commandBlock.toString());
			
			errorCode = input.readInt();
		} catch (JSONException e) {
			System.err.println("JSONException em login no Client.");
		} catch (IOException e) {
			System.err.println("IOException em login no Client.");
		}
		
		if(errorCode == ErrorConstants.SUCCESS)
			this.id = id;
		
		return errorCode;
	}
	
	/* Description of the show products command block:
	 * id: "buy_producet"
	 * fields: String buyerId;
	 * 		   int productId;
	 * 		   int quantity;
	 */
	public int buyProduct() {
		int errorCode = -100;
		String buyerId; 
		int productId, quantity;
		JSONObject commandBlock = new JSONObject();
		
		System.out.println("Compra de produtos");
		System.out.println();
		
		if(this.id == null) {
			errorCode = ErrorConstants.NOT_LOGGED;
		} else {
			
			buyerId = this.id;

			System.out.print("ID do produto: ");
			productId = sysIn.nextInt();
			
			System.out.print("Quantidade desejada: ");
			quantity = sysIn.nextInt();
			
			//flush
			sysIn.nextLine();
			
			try {
				commandBlock.put("id", "buy_product");
				commandBlock.put("user_id", buyerId);
				commandBlock.put("product_id", productId);
				commandBlock.put("quantity", quantity);
				
				output.writeUTF(commandBlock.toString());
				
				errorCode = input.readInt();
			} catch (JSONException e) {
				System.err.println("JSONException em login no Client.");
			} catch (IOException e) {
				System.err.println("IOException em login no Client.");
			}

		}
		
		return errorCode;
	}
	
	/* Description of the show products command block:
	 * id: "show_products"
	 * fields: none 
	 */	
	public int ShowProducts() {
		JSONObject commandBlock = new JSONObject();
		JSONObject productBlock;
		String productBlockString;
		int productsListSize;
		int errorCode = ErrorConstants.SUCCESS;
		
		if(this.id == null) {
			errorCode = ErrorConstants.NOT_LOGGED;
		} else {
		
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
		
		return errorCode;
	}
	
	public void menu() {
		int option = -100;
		int errorCode;
		
		while(option != 0){
			System.out.println("\tMENU");
			System.out.println();
        	System.out.println("1. Registrar!");
        	System.out.println("2. Login!");
        	System.out.println("0. Sair");
        	
            option = sysIn.nextInt();
			sysIn.nextLine();
        	
        	if(option == 1) {
        		errorCode = this.registerUser();
        		
        		if(errorCode == ErrorConstants.SUCCESS)
        			System.out.println("Cadastro realizado com sucesso!");
        		else if(errorCode == ErrorConstants.SAME_NAME)
        			System.out.println("Ja existe um usuario cadastrado com esse email!");
        		else if(errorCode == ErrorConstants.SAME_ID)
        			System.out.println("Ja existe um usuario cadastrado com esse id!");
        		else if(errorCode == ErrorConstants.SAME_EMAIL)
        			System.out.println("Ja existe um usuario cadastrado com esse email!");
        	
        	} else if(option == 2) {
        		errorCode = this.login();
        		
        		if(errorCode == ErrorConstants.NOT_FOUND)
        			System.out.println("Nao foi encontrado nenhum usuario com este ID!");
        		else if(errorCode == ErrorConstants.WRONG_PASSWORD)
        			System.out.println("Senha errada!");
        		else if(errorCode == ErrorConstants.SUCCESS) {
        			System.out.println("Login realizado com sucesso!");
        		
        			//menu dos logados
        			while(option != 0) {
        				
        	        	System.out.println("1. Listar produtos!");
        	        	System.out.println("2. Comprar produto!");
        	        	System.out.println("3. Voltar a pagina inicial!");
        	        	System.out.println("0. Sair");
        				
        	            option = sysIn.nextInt();
        				sysIn.nextLine();
        				
        				if(option == 1) {
        					errorCode = this.ShowProducts();
        					
        					if(errorCode == ErrorConstants.NOT_LOGGED)
        						System.out.println("Voce nao esta logado!");
        				
        				} else if(option == 2) {
        					errorCode = this.buyProduct();
        					
        					if(errorCode == ErrorConstants.SUCCESS)
        						System.out.println("Compra realizada com sucesso!");
        					else if(errorCode == ErrorConstants.NOT_FOUND)
        						System.out.println("Produto nao encontrado!");
        					else if(errorCode == ErrorConstants.QUANTITY_NOT_ENOUGH)
        						System.out.println("Quantidade insuficiente para a compra.");
        				} else if(option == 3) {
        					option = 10;
        					break;
        				}
        				else if(option == 0) {
        					break;
        				}

        			}
        		}
        	}
		}
	}
	
	public static void main(String[] args) {
		Socket clientSocket = null;
		
		if(args.length != 2) {
			System.err.println("Usage: java ServerClient <ip> <port number>");
			System.exit(1);
		}
		
		try {
			clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			System.out.println("Erro de conexao no Client.");
		}
		
		
		Client client = new Client(clientSocket);
		client.menu();
	}
}
