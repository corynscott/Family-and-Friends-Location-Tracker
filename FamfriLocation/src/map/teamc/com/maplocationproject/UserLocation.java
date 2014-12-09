package map.teamc.com.maplocationproject;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 		Class that manages the objects of the periodical updates of the user's location
 *
 */
@SuppressLint("SimpleDateFormat")
public class UserLocation{
	
	/*
	 * Username of the application
	 */
    private String username;
    
    /*
     * Date where the location was stored
     */
    private String date;
    
    /*
     * Longitude value of the stored location
     */
    private double longitude;
    
    /*
     * Latitude value of the stored location
     */
    private double latitude;
    
    /*
     * Whether the location is or not a checkin (if is false, the data is a location)
     */
    private boolean checkin;
    
    /*
     * Comment for checkin, if the data stored is a location the value will be "none"
     */
    private String comment;
    
    /*
     * Place name for a checkin, if the data stored is a location the value will be "none" 
     */
    private String placeName;
    
    /*
     * The Unique Location Identifier"
     */
    private String uli;

    

	public UserLocation(){
    	
    }
    
    public UserLocation(String username, String time, double longitude, double latitude){
        this.username = username;
        this.date = stringDate(time);
        this.longitude = longitude;
        this.latitude = latitude;
        this.checkin = false;
        this.comment = "none";
        this.placeName = "none";
    }
    
    public UserLocation(String username, String time, double longitude, double latitude, 
    		boolean checkin, String comment, String placeName){
    	this.username = username;
        this.date = stringDate(time);
        this.longitude = longitude;
        this.latitude = latitude;
        this.checkin = checkin;
        this.comment  = comment;
        this.placeName = placeName;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }
    
    public boolean isCheckin(){
    	return this.checkin;
    }
    
    public void setCheckin(boolean checkin){
    	this.checkin = checkin;
    }
    
    public String getComment(){
    	return this.comment;
    }
    
    public void setComment(String comment){
    	this.comment = comment;
    }
    
    public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	public String getUli() {
		return uli;
	}

	public void setUli(String uli) {
		this.uli = uli;
	}

	/**
     * 		Code that transforms the string date to another Date format
     * @param time
     * 		String value of the date
     * @return
     * 		A String value with the new date's format
     * 
     */
    private String stringDate(String time){
        Date d = new Date(Long.parseLong(time));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sss'Z'");
        String date = dateFormat.format(d);
        Log.d("Date", date);
        return date;
    }
    
    @Override
    public String toString(){
    	return "UserName: " + this.username + "\nDate: " + this.date + "\nLongitude: " + 
    			this.longitude + "\nLatitude: " + this.latitude + "\nComment: " + this.comment + 
    			" PlaceName: " + this.placeName;
    }
}
