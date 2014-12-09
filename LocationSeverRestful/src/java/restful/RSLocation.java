package restful;

import ejb.LocationSSB;
import ejb.UserSSB;
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
 * This Class Provides a Restful interface to the system, specifically for location interactions
 * @author Coryn Scott
 */
@Singleton
@Path("/")
public class RSLocation {
    @EJB
    LocationSSB lssb;
    @EJB
    UserSSB ussb;
    
    /**
     * Get method to get all users locations, used for testing
     * @return List of locations for all users
     */
    @GET
    @Path("locations/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllLocationsJsonOrXML(){
        return lssb.getAllLocations();
    }
    
    /**
     * Get method for returning all users check-ins
     * @return all check-ins (locations) for all users
     */
    @GET
    @Path("checkIns/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllCheckInsJsonOrXML(){
        return lssb.getAllCheckIns();
    }
    
    /**
     * Get method for getting a specific users locations
     * @param username username of the client who's locations we want
     * @return list of locations for the given username
     */
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
    
    /**
     * Get method for getting a specific users check-ins (location)
     * @param username username of the client who's check-ins we want
     * @return list of check-ins (locations) for the given username 
     */
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
    
    /**
     * Post method to add a location for a specific user
     * @param location the location we want to store
     * @param username the username of the user we want to store the location for.
     * @param context context of the request
     * @return Response OK- Location stored successfully or NOTMODIFIED-if its not a valid username
     */
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
    
    /**
    * Post method to add a check-in (location) for a specific user
     * @param location the check-in (location) we want to store
     * @param username the username of the user we want to store the check-in (location) for.
     * @param context context of the request
     * @return Response OK- if the check-in is stored successfully or NOTMODIFIED-if its not a valid username
     */
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

