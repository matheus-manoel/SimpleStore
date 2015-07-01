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
		System.out.println("cadastro errod code: " + client.registerUser());
		System.out.println("login errod code: " + client.login());
		System.out.println("login errod code: " + client.login());
		System.out.println("login errod code: " + client.login());
	}
}
