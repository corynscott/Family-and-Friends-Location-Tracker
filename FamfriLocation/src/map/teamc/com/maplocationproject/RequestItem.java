package map.teamc.com.maplocationproject;

/**
 * 		Class for the request items' object
 *
 */

public class RequestItem {
	/*
	 * Name of the user that made a request
	 */
	private String name;
	
	/*
	 * Phone number of the user that made a request
	 */
	private String phoneNumber;
	
	/*
	 * Username of the user that made a request
	 */
	private String userName;
	
	public RequestItem(){
		
	}
	public RequestItem(String name, String phone){
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