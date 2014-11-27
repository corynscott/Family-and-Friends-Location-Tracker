/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import friend.FriendBond;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Coryn Scott
 */

@NamedQueries({
        @NamedQuery(name="getUser",query="SELECT u FROM AppUser u WHERE u.username = :username"),
        @NamedQuery(name="getUserByPhoneNumber",query="SELECT u FROM AppUser u WHERE u.phoneNumber = :phoneNumber"),
        @NamedQuery(name="getAllUsers",query="SELECT u FROM AppUser u WHERE u.username != 'admin'"),
})
@Entity
@XmlRootElement(name = "appUser")
public class AppUser implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    
    @NotNull
    String name;
    @NotNull
    String username;
    @NotNull
    String password;
    @NotNull
    String phoneNumber;
    @OneToMany(mappedBy = "appUser")
    private List<Location> locations;
    
    @OneToMany(mappedBy = "appUser")
    private List<Location> checkIns;
    
    private List<FriendBond> friendRequest;

    public AppUser() {
    }

    public AppUser(String name, String username, String password, String phoneNumber) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
    
    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @XmlAttribute
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @XmlAttribute
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @XmlTransient
    public List<Location> getLocations() {
        return locations;
    }
    
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    @XmlTransient
    public List<Location> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<Location> checkIns) {
        this.checkIns = checkIns;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.username);
        hash = 29 * hash + Objects.hashCode(this.password);
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
        final AppUser other = (AppUser) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }
    
    
}
