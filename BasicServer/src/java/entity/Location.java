package entity;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Location class representing locations
 * @author Coryn Scott
 */
@XmlRootElement(name = "location")
public class Location {
    
    String username;
    String longitude;
    String latitude;
    Date date;
    Date time;
    String ULI;
    
    /**
     *Empty Constructor
     */
    public Location(){
        
    }
    /**
     * getter for username field
     * @return string user's username
     */
    @XmlAttribute
    public String getUsername() {
        return username;
    }
    /**
     * setter for username field
     * @param username string user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * getter for the longitude field
     * can be returned as an XML attribute
     * @return longitude string of the longitude of the location
     */
    @XmlAttribute
    public String getLongitude() {
        return longitude;
    }
    /**
     * setter for the longitude field
     * @param longitude string of the longitude of the location
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    /**
     * getter for the latitude field
     * can be returned as an XML attribute
     * @return string latitude of the location 
     */
    @XmlAttribute
    public String getLatitude() {
        return latitude;
    }
    /**
     * setter for the latitude field
     * @param latitude string latitude of the location 
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    /**
     * getter for the date field
     * can be returned as an XML attribute
     * @return date object of the date the location was recorded
     */
    @XmlAttribute
    public Date getDate() {
        return date;
    }
    /**
     * setter for the date field
     * @param date date object of the date the location was recorded
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * getter for the time field
     * can be returned as an XML attribute
     * @return date object of the time the location was recorded
     */
    @XmlAttribute
    public Date getTime() {
        return time;
    }
    /**
     * setter for the time field
     * @param time date object of the time the location was recorded
     */
    public void setTime(Date time) {
        this.time = time;
    }
    
    /**
     * getter for the ULI field
     * can be returned as an XML attribute
     * @return long of the unique location identifier
     */
    @XmlAttribute
    public String getULI() {
        return ULI;
    }
    
    /**
     * setter for the ULI field
     * @param ULI long of the ULI to be set
     */
    public void setULI(String ULI) {
        this.ULI = ULI;
    }
}
