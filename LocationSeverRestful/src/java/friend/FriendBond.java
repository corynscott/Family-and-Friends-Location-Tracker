/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package friend;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Coryn Scott
 */
@XmlRootElement
public class FriendBond implements Serializable{
    String initUsername;
    String RecpUsername;

    public FriendBond() {
    }

    
    
    public FriendBond(String initUsername, String RecpUsername) {
        this.initUsername = initUsername;
        this.RecpUsername = RecpUsername;
    }
    @XmlAttribute  
    public String getInitUsername() {
        return initUsername;
    }

    public void setInitUsername(String initUsername) {
        this.initUsername = initUsername;
    }
    @XmlAttribute  
    public String getRecpUsername() {
        return RecpUsername;
    }

    public void setRecpUsername(String RecpUsername) {
        this.RecpUsername = RecpUsername;
    }
    
    
    
}
