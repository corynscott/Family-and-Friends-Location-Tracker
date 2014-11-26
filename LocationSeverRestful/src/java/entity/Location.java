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
 *
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
    

    public Location() {
    }

    public Location(String longitude, String latitude, Date dateandtime, String comment, boolean checkIn,AppUser appUser,String place) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateandtime = dateandtime;
        this.comment = comment;
        this.checkIn = checkIn;
        this.appUser = appUser;
        this.place = place;
    }
    
    @XmlAttribute
    public Long getULI() {
        return ULI;
    }

    public void setULI(Long ULI) {
        this.ULI = ULI;
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
    public Date getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(Date dateandtime) {
        this.dateandtime = dateandtime;
    }
    @XmlAttribute
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    @XmlAttribute
    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }
    @XmlTransient
    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    @XmlAttribute
     public String getAppUsername() {
        return appUser.getUsername();
    }
    @XmlAttribute
    public String getPlace() {
        return place;
    }

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
