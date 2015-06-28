package store.model;

import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

public class Product {
	private String name, provider;
	private GregorianCalendar validity;
	private float price;
	private static AtomicInteger nextId = new AtomicInteger();
	private int id;
	private int quantity;
	
	public Product(String name, String provider, float price, int day, int month, int year, int quantity) {
		this.name = name;
		this.provider = provider;
		this.price = price;
		this.validity = new GregorianCalendar(year, month, day, 0, 0, 0);
		this.id = nextId.incrementAndGet();
		this.setQuantity(quantity);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public GregorianCalendar getValidity() {
		return validity;
	}

	public void setValidity(GregorianCalendar validity) {
		this.validity = validity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
