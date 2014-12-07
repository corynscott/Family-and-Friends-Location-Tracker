/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Location class representing both locations of the user as well as their check-ins
 * @author Coryn Scott
 */

@NamedQueries({
 @NamedQuery(name="getUserLocations",query="SELECT l FROM Location l WHERE l.appUser.username = :username"),
 @NamedQuery(name="getUserCheckIns",query="SELECT l FROM Location l WHERE l.appUser.username = :username and l.checkIn = true"),
 @NamedQuery(name="findLocationsByULI",query="SELECT l FROM Location l WHERE l.ULI = :uli"),
 @NamedQuery(name="getAllLocations",query="SELECT l FROM Location l"),
 @NamedQuery(name="getAllCheckIns",query="SELECT l FROM Location l WHERE l.checkIn = true"),
 })

@Entity
@XmlRootElement(name = "location")
public class Location implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long ULI;

    @NotNull
    private String longitude;
    
    @NotNull
    private String latitude;
    
    @NotNull
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateandtime;
    
    private String comment;
    
    private boolean checkIn;
    
    private String place;
    
    @ManyToOne
    private AppUser appUser;
    

    /**
     * Empty constructor required for the Entity
     */
    public Location() {
    }

    /**
     * Constructor, taking all attributes of location as parameters
     * @param longitude of the location
     * @param latitude of the location
     * @param dateandtime that the location was recorded
     * @param comment comment for that location, only used when its a check-in
     * @param checkIn true if the location represents a check-in false if its just a plain location
     * @param appUser the user that the location belongs to
     * @param place further attribute representing the location, used for check-ins for when the user is checking into a place
     */
    public Location(String longitude, String latitude, Date dateandtime, String comment, boolean checkIn,AppUser appUser,String place) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateandtime = dateandtime;
        this.comment = comment;
        this.checkIn = checkIn;
        this.appUser = appUser;
        this.place = place;
    }
    
    /**
     * getter for the ULI field
     * can be returned as an XML attribute
     * @return long of the unique location identifier
     */
    @XmlAttribute
    public Long getULI() {
        return ULI;
    }

    /**
     * setter for the ULI field
     * @param ULI long of the ULI to be set
     */
    public void setULI(Long ULI) {
        this.ULI = ULI;
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
     * getter for the dateandtime field
     * can be returned as an XML attribute
     * @return date object of the date the location was recorded
     */
    @XmlAttribute
    public Date getDateandtime() {
        return dateandtime;
    }

    /**
     * setter for the dateandtime field
     * @param dateandtime date object of the date the location was recorded
     */
    public void setDateandtime(Date dateandtime) {
        this.dateandtime = dateandtime;
    }
    /**
     * getter for the comment field
     * can be returned as an XML attribute
     * @return string of the comment relating to the location
     */
    @XmlAttribute
    public String getComment() {
        return comment;
    }

    /**
     * setter for the comment field
     * @param comment string of the comment relating to the location
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    /**
     * getter for check-in
     * can be returned as an XML attribute
     * @return true if location is a check-in false if it just a standard location
     */
    @XmlAttribute
    public boolean isCheckIn() {
        return checkIn;
    }

    /**
     * setter for check-in
     * @param checkIn true if location is a check-in false if it just a standard location
     */
    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }
    /**
     * getter for AppUser field
     * @return AppUser who the location belongs to
     */
    @XmlTransient
    public AppUser getAppUser() {
        return appUser;
    }

    /**
     * setter for AppUser field
     * @param appUser AppUser who the location belongs to
     */
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    /**
     * Method to get the username of the AppUser who the location belongs to.
     * can be returned as an XML attribute
     * @return string username of the AppUser who the location belongs to.
     */
    @XmlAttribute
     public String getAppUsername() {
        return appUser.getUsername();
    }
    /**
     * getter for the place field
     * can be returned as an XML attribute
     * @return string place that the user checked-in
     */
    @XmlAttribute
    public String getPlace() {
        return place;
    }

    /**
     * setter for the place field
     * @param place string place that the user checked-in
     */
    public void setPlace(String place) {
        this.place = place;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.ULI);
        hash = 47 * hash + Objects.hashCode(this.longitude);
        hash = 47 * hash + Objects.hashCode(this.latitude);
        hash = 47 * hash + Objects.hashCode(this.dateandtime);
        hash = 47 * hash + Objects.hashCode(this.comment);
        hash = 47 * hash + (this.checkIn ? 1 : 0);
        hash = 47 * hash + Objects.hashCode(this.appUser);
        hash = 47 * hash + Objects.hashCode(this.place);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (!Objects.equals(this.ULI, other.ULI)) {
            return false;
        }
        if (!Objects.equals(this.longitude, other.longitude)) {
            return false;
        }
        if (!Objects.equals(this.latitude, other.latitude)) {
            return false;
        }
        if (!Objects.equals(this.dateandtime, other.dateandtime)) {
            return false;
        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
        if (this.checkIn != other.checkIn) {
            return false;
        }
        if (!Objects.equals(this.appUser, other.appUser)) {
            return false;
        }
        if (!Objects.equals(this.place, other.place)) {
            return false;
        }
        return true;
    }
    
    
}
