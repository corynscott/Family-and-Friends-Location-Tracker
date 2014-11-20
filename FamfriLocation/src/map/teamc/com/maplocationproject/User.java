package map.teamc.com.maplocationproject;

/**
 * 
 * @Class: User
 * @Description
 * 		Class that contains the user information - Register
 * @Created: 18/11/2014
 *
 */
public class User {

	private String userName;
	private String name;
	private String phoneNumber;
	private String pwd;
	private boolean numberChecked;

	User(String user, String name, String phone, String pass){
		this.userName = user;
		this.name = name;
		this.phoneNumber = phone;
		this.pwd = pass;
		this.numberChecked = false;
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
	
	public boolean isNumberChecked() {
		return numberChecked;
	}

	public void setNumberChecked(boolean numberChecked) {
		this.numberChecked = numberChecked;
	}
	
	@Override
	public String toString(){
		return name + " " + userName + " " + phoneNumber;
	}
	
}
