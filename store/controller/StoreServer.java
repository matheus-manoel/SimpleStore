package store.controller;

//import store.model.*;
//import store.view.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class StoreServer {
	private boolean serverOn;
	private ServerSocket ss;
	private ManagerStoreController managerStoreController;
	private ClientStoreController clientStoreController;
	private CSVStoreManager csvStoreManager;
	private ArrayList<ClientConnection> clientConnections;
	
	public StoreServer(int port) {
		this.serverOn = true;
		this.csvStoreManager = new CSVStoreManager("users.csv", "products.csv", "purchases.csv");
		this.managerStoreController = new ManagerStoreController(csvStoreManager);
		this.clientStoreController = new ClientStoreController(csvStoreManager);
		this.clientConnections = new ArrayList<ClientConnection>();
		
		try {
			this.ss = new ServerSocket(port);
		} catch(IOException ioe) { 
            System.out.println("Could not create server socket on port 11111. Quitting."); 
            System.exit(-1); 
        }
	}
	
	public void run() {
		//connecting with a ServerClient
		System.out.println("Server iniciado. Esperando comexão do Administrador.");
		Socket serverClientSocket = null;
		try {
			serverClientSocket = this.ss.accept();
			System.out.println("Administrador conectado.");
		} catch (IOException e) {
			System.out.println("Error while accepting serverClientSocket connection.");
		}
		ManagerConnection serverClientConnection = new ManagerConnection(serverClientSocket, managerStoreController);
		Thread serverClientThread = new Thread(serverClientConnection);
		serverClientThread.start();
		
		while(serverOn) {
			try {
				Socket clientSocket = this.ss.accept();
				
				ClientConnection clieCon = new ClientConnection(clientSocket, clientStoreController);
				
				this.clientConnections.add(clieCon);
				
				Thread thr = new Thread(clieCon);
				thr.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Usage: java StoreServer <port number>");
			System.exit(1);
		}
		
		StoreServer storeServer = new StoreServer(Integer.parseInt(args[0]));
		storeServer.run();
		
	}
}
