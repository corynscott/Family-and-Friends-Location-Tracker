/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restful;

import friend.UsernameNumber;
import ejb.LocationSSB;
import ejb.UserSSB;
import entity.AppUser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Coryn Scott
 */
@Singleton
@Path("friends")
public class RSFriends {
    @EJB
    UserSSB ussb;
    @EJB
    LocationSSB lssb;
   
    @POST
    @Path("{username}/potentialfriends")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UsernameNumber> postNumbersJsonOrXML(List<UsernameNumber> numbers, @PathParam("username") String username,@Context UriInfo context){
        List<UsernameNumber> usernameNumbers = new ArrayList();
        for(UsernameNumber n:numbers){
            AppUser user = ussb.getUserByPhoneNumber(modifyNumber(n.getNumber()));
            if(user!=null){
                UsernameNumber un = new UsernameNumber(user.getUsername(),user.getPhoneNumber());
                usernameNumbers.add(un);
            }
        }
        return usernameNumbers;
    }
    
    @GET
    @Path("{username}/requestfriend/{friUsername}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UsernameNumber> requestFriendJsonOrXML( @PathParam("username") String username,@PathParam("friUsername") String friUsername,@Context UriInfo context){
        List<AppUser> appuserslist = ussb.getAllUsers();
        List<UsernameNumber> usernumbers = new ArrayList();
        for(AppUser au:appuserslist){
            UsernameNumber un = new UsernameNumber("",au.getPhoneNumber());
            usernumbers.add(un);
        }
        
        
       
        return usernumbers;
    }
    
    public String modifyNumber(String number){
        if(number.startsWith("0")){
            return "+44" + number.substring(1);
        }
        else
            return number;
    }
    
}
