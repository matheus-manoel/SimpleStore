package store.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
		// TODO Auto-generated method stub
		
	}

}
