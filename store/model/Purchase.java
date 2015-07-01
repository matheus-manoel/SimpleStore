package store.model;

import java.util.GregorianCalendar;

public class Purchase {
	private String buyerId;
	private int productId;
	private int quantity;
	private float unitaryPrice;
	private float totalPrice;
	GregorianCalendar date;
	
	public Purchase(String buyerId, int productId, int quantity, float unitaryPrice,
			float totalPrice, int day, int month, int year) {
		this.buyerId = buyerId;
		this.productId = productId;
		this.quantity = quantity;
		this.unitaryPrice = unitaryPrice;
		this.totalPrice = totalPrice;
		this.date = new GregorianCalendar(year, month, day, 0, 0, 0);
	}

	public int getQuantity() {
		return quantity;
	}

	public float getUnitaryPrice() {
		return unitaryPrice;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public int getProductId() {
		return productId;
	}

	public int getDay() {
		return this.date.get(GregorianCalendar.DAY_OF_MONTH);
	}
	
	public int getMonth() {
		return this.date.get(GregorianCalendar.MONTH);
	}
	
	public int getYear() {
		return this.date.get(GregorianCalendar.YEAR);
	}
	
	public String toString() {
		return this.buyerId + ", " + this.productId + ", " + this.quantity +
				", " + this.unitaryPrice + ", " + this.totalPrice + ", " +
				this.date.get(GregorianCalendar.DAY_OF_MONTH) + ", " + this.date.get(GregorianCalendar.MONTH) + 
				", " + this.date.get(GregorianCalendar.YEAR);
	}
}
