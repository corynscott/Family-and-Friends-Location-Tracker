package friend;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


/**
 * UsernameNumber is used to provide the relationship between a username and the users telephone number, used when a user is requesting potential friends.
 * @author Coryn Scott
 */
@XmlRootElement
public class UsernameNumber {
    
    String username;
    String number;
    
    /**
     * Empty Constructor
     */
    public UsernameNumber (){}

    /**
     * Constructor that takes all the attributes of a UsernameNumber as parameters
     * @param username string username that the corresponding number belongs to
     * @param number string phone number that the corresponding username belongs to
     */
    public UsernameNumber(String username, String number) {
        this.username = username;
        this.number = number;
    }
    
    /**
     * getter for the username field
     * can be returned as XML attribute
     * @return string username
     */
    @XmlAttribute  
    public String getUsername() {
        return username;
    }
    
    /**
     * setter for the username field
     * @param username string username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * getter for the number field
     * can be returned as XML attribute
     * @return string phone number
     */
    @XmlAttribute  
    public String getNumber() {
        return number;
    }

    /**
     * setter for the number field
     * @param number string phone number
     */
    public void setNumber(String number) {
        this.number = number;
    }

}
    

