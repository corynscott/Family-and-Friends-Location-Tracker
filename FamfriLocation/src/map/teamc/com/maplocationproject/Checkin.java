package map.teamc.com.maplocationproject;

/**
 * Created by alex64 on 10/11/2014.
 */
public class Checkin {
    private String username;
    private String placeName;
    private String longitude;
    private String latitude;
    private String comment;
    private String date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getComment(){
    	return this.comment;
    }
    
    public void setComment(String comment){
    	this.comment = comment;
    }
    
    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }
}
