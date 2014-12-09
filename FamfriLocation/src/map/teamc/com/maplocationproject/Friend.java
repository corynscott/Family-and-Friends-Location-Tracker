package map.teamc.com.maplocationproject;

/**
 * 
 * 		Class for the friend's object
 *
 */
public class Friend {
	
	/*
	 * Username of the friend
	 */
	private String username;
	
	/*
	 * Phone number of the friend
	 */
	private String phoneNumber;
	
	/*
	 * Name of the friend
	 */
	private String name;
	
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
}
