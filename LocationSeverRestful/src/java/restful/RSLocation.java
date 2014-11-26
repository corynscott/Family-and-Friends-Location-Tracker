/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restful;

import ejb.LocationSSB;
import ejb.UserSSB;
import entity.AppUser;
import entity.Location;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Coryn Scott
 */
@Singleton
@Path("/")
public class RSLocation {
    @EJB
    LocationSSB lssb;
    @EJB
    UserSSB ussb;
    
    @GET
    @Path("locations/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllLocationsJsonOrXML(){
        return lssb.getAllLocations();
    }
    
    @GET
    @Path("checkIns/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllCheckInsJsonOrXML(){
        return lssb.getAllCheckIns();
    }
    
    @GET
    @Path("{username}/locations")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getUserLocationsJsonOrXML(@PathParam("username") String username){
        if(ussb.doesUsernameExist(username)){
            return lssb.getUserLocations(username);
        }
        else
            
            return null;
    }
    
    @GET
    @Path("{username}/checkIns")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getUserCheckInsJsonOrXML(@PathParam("username") String username){
        if(ussb.doesUsernameExist(username)){
            return lssb.getUserCheckIns(username);
        }
        else
            return null;
    }
    
    @POST
    @Path("{username}/addLocation")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addLocationJsonorXML(Location location,@PathParam("username") String username, @Context UriInfo context){
        System.out.print(username);
        if(ussb.doesUsernameExist(username)){
            Location locationToStore = new Location(location.getLongitude(),location.getLatitude(),location.getDateandtime(),location.getComment(),false,ussb.getUser(username),location.getPlace());
            lssb.addLocation(locationToStore);
            try {
                return Response.created(new URI(context.getRequestUri().toString())).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(RSLocation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.notModified().build(); 
    }
    
    @POST
    @Path("{username}/addCheckIn")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCheckInJsonorXML(Location location, @PathParam("username") String username,@Context UriInfo context){
        if(ussb.doesUsernameExist(username)){
            Location locationToStore = new Location(location.getLongitude(),location.getLatitude(),location.getDateandtime(),location.getComment(),true,ussb.getUser(username),location.getPlace());
            lssb.addLocation(locationToStore);
            try {
                return Response.created(new URI(context.getRequestUri().toString())).build();
            } catch (URISyntaxException ex) {
                Logger.getLogger(RSLocation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.notModified().build(); 
    }
}

