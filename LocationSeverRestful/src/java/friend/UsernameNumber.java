package friend;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement
public class UsernameNumber {
    
    String username;
    String number;
    public UsernameNumber (){}

    public UsernameNumber(String username, String number) {
        this.username = username;
        this.number = number;
    }
    
    @XmlAttribute  
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    @XmlAttribute  
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    
   

}
    

