
package entity;

import friend.FriendBond;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * AppUser represnts the User of the system.
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
    
    @OneToOne
    private List<AppUser> friends;
    
    private List<FriendBond> friendRequest;

    /**
     * empty constructor required for an entity.
     */
    public AppUser() {
    }

    /**
     * Constructor for AppUser that takes the necessary attributes as parameters and sets the fields appropriately.
     * @param name the user's name
     * @param username the user's username
     * @param password the user's password as plain text
     * @param phoneNumber the user's phone number
     */
    public AppUser(String name, String username, String password, String phoneNumber) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        friendRequest = new ArrayList();
    }
    
    /**
     * getter for ID field
     * @return long the ID of the user
     */
    @XmlAttribute
    public long getId() {
        return id;
    }

    /**
     * setter for ID field
     * @param id long the ID of the user
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * getter for name field
     * @return string user's name
     */
    @XmlAttribute
    public String getName() {
        return name;
    }

    /**
     * setter for name field
     * @param name string user's name
     */
    public void setName(String name) {
        this.name = name;
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
     * getter for password field
     * @return string user's password
     */
    @XmlAttribute
    public String getPassword() {
        return password;
    }

    /**
     * setter for password field
     * @param password string user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * setter for phoneNumber field
     * @return string user's phone number
     */
    @XmlAttribute
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * setter for phoneNumber field
     * @param phoneNumber string user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * getter for locations field
     * @return list of user's locations
     */
    @XmlTransient
    public List<Location> getLocations() {
        return locations;
    }
    
    /**
     * setter for locations field
     * @param locations list of user's locations
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    /**
     * getter for checkIns field
     * @return list of check-ins (locations)
     */
    @XmlTransient
    public List<Location> getCheckIns() {
        return checkIns;
    }
    
    /**
     * setter for checkIns field
     * @param checkIns the list of check-ins 
     */
    public void setCheckIns(List<Location> checkIns) {
        this.checkIns = checkIns;
    }
    
    /**
     * getter for friends list
     * @return list of friends (AppUser) of the user
     */
    @XmlTransient
    public List<AppUser> getFriends() {
        return friends;
    }
    
    /**
     * setter for friends list
     * @param friends list of friends (AppUsers) of the user to be set
     */
    public void setFriends(List<AppUser> friends) {
        this.friends = friends;
    }
    
    /**
     * Method for adding an individual friend to the friend list, takes the friend (AppUser) as a parameter
     * @param au the friend (AppUser) we want to add to the friends list.
     */
    public void addFriend(AppUser au){
        friends.add(au);
    }
    
    /**
     * Method for identifying if a user with the username passed as a parameter is a friend, ie is in the list friends.
     * @param username string username of the user that we want to check 
     * @return true if the username is in the list of friends, otherwise false
     */
    public boolean isFriend(String username) {
        for(AppUser au:friends){
            if(au.username.equals(username)){
                return true;
            }
        }
        return false;
    }
    

    
    /**
     * getter for the friendRequest field
     * @return list of FriendBond's representing a friend request
     */
    @XmlTransient
    public List<FriendBond> getFriendRequest() {
        return friendRequest;
    }
    
    /**
     * Method for adding a friend request to the list of friend requests of the user
     * @param fb FriendBond representing a friend request to be added to the list of friend requests.
     */
    public void addFriendRequest(FriendBond fb){
        friendRequest.add(fb);
    }
    
    /**
     * Method for removing a friend request from the list of friend requests of the user
     * @param fb FriendBond representing a friend request to be removed from the list of friend requests.
     */
    public void removeFriendRequest(FriendBond fb){
        friendRequest.remove(fb);
    }   
    /**
     * Method for removing a friend request from the list of friend requests of the user, based on the user's usernames involved in the request
     * @param username one of the users involved in the friend request
     * @param username1 the other user involved in the friend request
     */
    public void removeFriendRequest(String username, String username1){
        int indexrem ;
        for(int i=0;i<friendRequest.size();i++){
            FriendBond fb = friendRequest.get(i);
            if(fb.getInitUsername().equals(username)&&fb.getRecpUsername().equals(username1)){
                friendRequest.remove(i);
        
            }
            else if(fb.getInitUsername().equals(username1)&&fb.getRecpUsername().equals(username)){
                friendRequest.remove(i);
        
            }
        }
    }   
    
    /**
     * setter for the friendRequest field
     * @param friendRequest the list of FriendBonds representing the friend requests of the user
     */
    public void setFriendRequest(List<FriendBond> friendRequest) {
        this.friendRequest = friendRequest;
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
