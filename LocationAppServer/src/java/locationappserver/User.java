/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locationappserver;

/**
 *
 * @author Coryn Scott
 */
public class User {
    public String username;
    public String firstName;
    public String secondName;
    public String UID;
    
    public User(){
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    
        
}
