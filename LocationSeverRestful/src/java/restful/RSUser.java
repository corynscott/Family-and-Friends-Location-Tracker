/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restful;

import ejb.UserSSB;
import entity.AppUser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**
 * This Class Provides a Restful interface to the system, specifically for UserAccount interactions, path is /user
 * @author Coryn Scott
 */
@Singleton
@Path("user")
public class RSUser {
    
    @EJB
    UserSSB ussb;
           
    
    /**
     * Get method to return all users of the system, used for testing.
     * @return list of AppUsers of the system
     */
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<AppUser> getAllUsersJsonOrXML(){
        return ussb.getAllUsers();
    }
    /**
     * Get method to get a specific user's details based on their username
     * @param username of the user we want to get the details of
     * @return AppUser for the username passed as a parameter/path
     */
    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AppUser getUserJsonOrXML(@PathParam("username") String username){
        
        return ussb.getUser(username);
    }
    
    /**
     * Post method to add a new user
     * @param user AppUser representing the users details
     * @param context context of the request
     * @return Response OK-if users is successfully stored, CONFILCT is the username is taken by anther user, or NOTMODIFIFED if the user is not stored for any other reason.
     */
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addNewUserJsonorXML(AppUser user, @Context UriInfo context){
        System.out.println("contetn"+user.getName()+user.getUsername()+user.getPassword()+user.getPhoneNumber());
        System.out.println(ussb.doesUsernameExist(user.getUsername()));
        if(ussb.doesUsernameExist(user.getUsername())){
            return Response.status(409).build();
        }
        if(ussb.addUser(user.getName(), user.getUsername(), user.getPassword(),modifyNumber(user.getPhoneNumber()))){
            try {
                return Response.created(new URI(context.getRequestUri().toString() + user.getUsername())).entity(user).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(RSUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.notModified().build(); 
    }
    
    
    /**
     * Post method to log a user in, this method checks that the user credentials passed in the request are correct
     * @param user AppUser containing the users username and password
     * @param context context of the request
     * @return OK- if the credentials are correct, NOTAUTHORISED if the users credentials are incorrect.
     */
    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response loginJsonorXML(AppUser user, @Context UriInfo context){
        System.out.println("contetn"+user.getName()+user.getUsername()+user.getPassword()+user.getPhoneNumber());
        if(ussb.doesUsernameExist(user.getUsername())){
             if(ussb.checkPassword(user.getUsername(), user.getPassword())){
                 return Response.ok().build();
             }
             
                 
        }
        return Response.status(401).build();
        
        
    }
    /**
    * Method to modify a number to ensure there is no issue when it comes to users storing phone number with 0 or +44
     * @param number to modify
     * @return the number with +44 prefix if it starts with 0.
     */
    public String modifyNumber(String number){
        if(number.startsWith("0")){
            return "+44" + number.substring(1);
        }
        else
            return number;
    }
}
