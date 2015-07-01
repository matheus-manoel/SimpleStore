package store.model;

public class User {
	private String name;
	private String email;
	private String id;
	private String password;
	private String address;
	private String telephoneNumber;
	
	public User(String name, String email, String id, String password,
			String address, String telephoneNumber) {
		this.name = name;
		this.email = email;
		this.id = id;
		this.password = password;
		this.address = address;
		this.telephoneNumber = telephoneNumber;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Override
	public String toString() {
		return name + ", " + email + ", " + id + ", " + password + ", "
				+ address + ", " + telephoneNumber;
	}
}
