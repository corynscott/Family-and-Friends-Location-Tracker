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
 *
 * @author Coryn Scott
 */
@Singleton
@Path("user")
public class RSUser {
    
    @EJB
    UserSSB ussb;
           
    
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<AppUser> getAllUsersJsonOrXML(){
        return ussb.getAllUsers();
    }
    @GET
    @Path("{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AppUser getUserJsonOrXML(@PathParam("username") String username){
        
        return ussb.getUser(username);
    }
    
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addNewUserJsonorXML(AppUser user, @Context UriInfo context){
        System.out.println("contetn"+user.getName()+user.getUsername()+user.getPassword()+user.getPhoneNumber());
        if(ussb.doesUsernameExist(user.getUsername())){
            return Response.status(409).build();
        }
        if(ussb.addUser(user.getName(), user.getUsername(), user.getPassword(),user.getPhoneNumber())){
            try {
                return Response.created(new URI(context.getRequestUri().toString() + user.getUsername())).entity(user).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(RSUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.notModified().build(); 
    }
    
    
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
}
