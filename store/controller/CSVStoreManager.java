package store.controller;

import java.io.*;
import java.util.*;

import store.model.Product;
import store.model.Purchase;
import store.model.User;

public class CSVStoreManager {
	private File userFile;
	private File productFile;
	private File purchaseFile;
	private GregorianCalendar today;

    public CSVStoreManager(String userFileName, String productFileName, String purchaseFileName) {
    	
    	this.today = new GregorianCalendar();
		today.setTime(new Date());
    	this.userFile = new File(userFileName);
    	this.productFile = new File(productFileName);
    	this.purchaseFile = new File(purchaseFileName);
        
        if(!this.userFile.exists()) {
            try {
                this.userFile.createNewFile();        
            } catch (Exception e) {
                System.out.println("Nao foi possivel criar o arquivo de usuarios.");
            }   
        }
        if(!this.productFile.exists()) {
            try {
                this.productFile.createNewFile();        
            } catch (Exception e) {
                System.out.println("Nao foi possivel criar o arquivo de produtos.");
            }   
        }
        if(!this.purchaseFile.exists()) {
            try {
                this.purchaseFile.createNewFile();        
            } catch (Exception e) {
                System.out.println("Nao foi possivel criar o arquivo de compras.");
            }   
        }
    }

    public void addUser(User user) {
    	
    	try{
			FileWriter userWriter = new FileWriter(userFile, true);
			BufferedWriter userBuffer = new BufferedWriter(userWriter);
			
			userBuffer.write(user.toString());
			userBuffer.write("\n");
			userBuffer.close();
			
        } catch (IOException ex){
        	System.out.println("Failed at writing in file");
        }    	

    }
    
    public void clearFile(String fileId) {
        try {
            PrintWriter writer;
            if(fileId.equals("book"))
                writer = new PrintWriter(productFile);
            else if(fileId.equals("user"))
                writer = new PrintWriter(userFile);
            else
                writer = new PrintWriter(purchaseFile);     
                
            writer.print("");
            writer.close();
       } catch (Exception e) {
           System.out.println("Erro no clearFile.");
       }
    }

    public void addProduct(Product product) {
		try{
		
			FileWriter productWriter = new FileWriter(productFile, true);
			BufferedWriter productBuffer = new BufferedWriter(productWriter);
			
			productBuffer.write(product.toString());
			productBuffer.write("\n");	
			productBuffer.close();
			
        } catch (IOException ex){
        	System.out.println("Failed at writing in file");
        }
    }
    
    public void addPurchase(Purchase purchase) {
    
    	try{
			FileWriter purchaseWriter = new FileWriter(purchaseFile, true);
			BufferedWriter purchaseBuffer = new BufferedWriter(purchaseWriter);
			
			purchaseBuffer.write(purchase.toString());
		    purchaseBuffer.write("\n");
            purchaseBuffer.close();

        } catch (IOException ex){
        	System.out.println("Failed at writing in file");
        }

	}
	
	public ArrayList<Product> loadProducts(){
		ArrayList<Product> products = new ArrayList<Product>();
		String name, provider;
		float price;
		int id;
		int quantity;
		
		try{
			
			BufferedReader productBuffer = new BufferedReader(new FileReader(productFile));
			String line = productBuffer.readLine();

			while(line != null){
				String data[] = line.split(", ");
			
				name  = data[0];
				provider = data[1];
				price = (float) Double.parseDouble(data[2]);
				id = Integer.parseInt(data[3]);
				quantity = Integer.parseInt(data[4]);
					
                products.add(new Product(id, name, provider, price, 0, 0, 0, quantity));
				line = productBuffer.readLine();
			}
			
			productBuffer.close();
			
		} catch (IOException ex) {
			System.out.println("Failed at reading the file");
		}
		
		return products;
	}
	
	public ArrayList<User> loadUsers(){
		ArrayList<User> users = new ArrayList<User>();
		String name, email, id, password, address, telephoneNumber;
		
		try{
			
			BufferedReader userBuffer = new BufferedReader(new FileReader(productFile));
			String line = userBuffer.readLine();

			while(line != null){
				String data[] = line.split(", ");
			
				name  = data[0];
				email = data[1];
				id  = data[2];
				password = data[3];
				address  = data[4];
				telephoneNumber = data[5];

                users.add(new User(name, email, id, password, address, telephoneNumber));
				line = userBuffer.readLine();
			}
			
			userBuffer.close();
			
		} catch (IOException ex) {
			System.out.println("Failed at reading the file");
		}
		
		return users;
	}
	
	public ArrayList<Purchase> loadPurchases(){
		ArrayList<Purchase> purchases = new ArrayList<Purchase>();
		String buyerId;
		int productId, quantity;
		float unitaryPrice, totalPrice;
		int day, month, year;
		
		try{
			BufferedReader purchaseBuffer = new BufferedReader(new FileReader(productFile));
			String line = purchaseBuffer.readLine();

			while(line != null){
				String data[] = line.split(", ");
			
				buyerId  = data[0];
				productId = Integer.parseInt(data[1]);
				quantity  = Integer.parseInt(data[2]);
				unitaryPrice = (float) Double.parseDouble(data[3]);
				totalPrice  = (float) Double.parseDouble(data[4]);
				day = Integer.parseInt(data[5]);
				month = Integer.parseInt(data[6]);
				year = Integer.parseInt(data[7]);

                purchases.add(new Purchase(buyerId, productId, quantity, unitaryPrice,
                		totalPrice, day, month, year));
                
				line = purchaseBuffer.readLine();
			}
			
			purchaseBuffer.close();
			
		} catch (IOException ex) {
			System.out.println("Failed at reading the file");
		}
		
		return purchases;
	}

}
