package map.teamc.com.maplocationproject;

/**
 * 		Class for the friend's request object
 *
 */
public class FriendRequest {
	
	/*
	 * Name of the user that make the friend request
	 */
	private String initUsername;
	
	/*
	 * Name of the user that receives the friend request
	 */
	private String recepUsername;
	
	public String getInitUsername() {
		return initUsername;
	}
	public void setInitUsername(String initUsername) {
		this.initUsername = initUsername;
	}
	public String getRecepUsername() {
		return recepUsername;
	}
	public void setRecepUsername(String recepUsername) {
		this.recepUsername = recepUsername;
	}
	
	
	
}
