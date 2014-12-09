package map.teamc.com.maplocationproject;

/**
 * 		Class for the potential friend's object
 *
 */
public class ContactItem {
	/*
	 * Name of the contact
	 */
	private String name;
	
	/*
	 * Phone number of the contact
	 */
	private String phoneNumber;
	
	/*
	 * Username of the potential friend
	 */
	private String userName;
	
	public ContactItem(){
		
	}
	public ContactItem(String name, String phone){
		this.name = name;
		this.phoneNumber = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
