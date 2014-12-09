package map.teamc.com.maplocationproject;

/**
 * 
 * 		Class that contains the user information - Register
 *
 */
public class User {

	/*
	 * Username to register
	 */
	private String userName;
	
	/*
	 * Name to register
	 */
	private String name;
	
	/*
	 * Phone number to register
	 */
	private String phoneNumber;
	
	/*
	 * Password to be registered
	 */
	private String pwd;
	
	public User(){
		
	}

	User(String user, String name, String phone, String pass){
		this.userName = user;
		this.name = name;
		this.phoneNumber = phone;
		this.pwd = pass;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	@Override
	public String toString(){
		return name + " " + userName + " " + phoneNumber;
	}
	
}
