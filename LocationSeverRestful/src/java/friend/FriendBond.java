/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package friend;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * FriendBond represnts a friend request
 * @author Coryn Scott
 */
@XmlRootElement
public class FriendBond implements Serializable{
    String initUsername;
    String RecpUsername;

    /**
     * empty constructor
     */
    public FriendBond() {
    }

    
    
    /**
     * Constructor that takes all the attributes of a FriendBond as a parameters
     * @param initUsername string username of the user requesting the friendship
     * @param RecpUsername string username of the user being requested to be a friend
     */
    public FriendBond(String initUsername, String RecpUsername) {
        this.initUsername = initUsername;
        this.RecpUsername = RecpUsername;
    }
    /**
     * getter for the username of the user requesting the friendship
     * can be returned as XML attribute
     * @return string of username of the user requesting the friendship
     */
    @XmlAttribute  
    public String getInitUsername() {
        return initUsername;
    }

    /**
     * setter for the username of the user requesting the friendship
     * @param initUsername string of username of the user requesting the friendship
     */
    public void setInitUsername(String initUsername) {
        this.initUsername = initUsername;
    }
    /**
     * getter for the username of the user being requested to be a friend
     * can be returned as XML attribute
     * @return string of username of the user being requested to be a friend
     * @return
     */
    @XmlAttribute  
    public String getRecpUsername() {
        return RecpUsername;
    }

    /**
     * setter for the username of the user being requested to be a friend
     * @return string of username of the user being requested to be a friend
     */
    public void setRecpUsername(String RecpUsername) {
        this.RecpUsername = RecpUsername;
    }
    
    
    
}
