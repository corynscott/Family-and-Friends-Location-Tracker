package map.teamc.com.maplocationproject;

import java.util.ArrayList;

public class Friend {
	private String username;
	
	private ArrayList<Checkin> location = new ArrayList<Checkin>();
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setCheckin(ArrayList<Checkin> location){
		this.location = location;
	}
	
	public ArrayList<Checkin> getCheckin(){
		return this.location;
	}
}
