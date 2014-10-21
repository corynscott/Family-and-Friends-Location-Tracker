/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locationappserver;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
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
    
    public Location(){
        
    }
    
    @XmlAttribute
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @XmlAttribute
    public String getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    @XmlAttribute
    public String getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    @XmlAttribute
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    @XmlAttribute
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    @XmlAttribute
    public String getULI() {
        return ULI;
    }
    
    public void setULI(String ULI) {
        this.ULI = ULI;
    }
    
    
    
}
