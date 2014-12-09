package restful;

import entity.Location;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * This Class Provides a Restful interface to the location services.
 * @author Coryn Scott
 */

@Singleton
@Path("location")
public class RSLocation {

    private final HashMap<String, Location> locations;

    /**
     * Constructor that initialises the Hashmap to store locations and adds a test location.
     */
    public RSLocation() {
        locations = new HashMap<>();
        Location loc = new Location();
        loc.setUsername("coryn");
        loc.setLatitude("123");
        loc.setLongitude("456");
        Calendar cal = Calendar.getInstance();
        loc.setTime(cal.getTime());
        loc.setULI("111");
        locations.put(loc.getULI(), loc);
        
    }
    
    /**
     * Get method to return a location based on its unique location identifies, passes in the path.
     * @param uli the unique location identifier for the location we want
     * @return Location with the specified uli.
     */
   @GET
   @Path("{uli}")
   @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Location getLocationJSONorXML(@PathParam("uli") String uli) {
        System.out.println("getLocationJSONorXML");
        return locations.get(uli);
    }
   
    /**
     * Get method to return all locations stored on the server
     * @return List of all locations stored on the server
     */
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllLocationsJSONorXML() {
        System.out.println("getAllLocationsJSONorXML");
        return new ArrayList<Location>(locations.values());
    }
    
    /**
     * Get method to return all locations for a specific user stored on the server
     * @param username the username of the user's locations to be returned
     * @return List of locations for the specified user
     */
    @GET
    @Path("/username/{username}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllLocationsJSONorXML(@PathParam("username") String username) {
        System.out.println("getAllLocationsForSpecificUserJSONorXML");
        ArrayList<Location> locationsArray = new ArrayList<Location>(locations.values());
        ArrayList<Location> userLocations = new ArrayList<Location>();
        for(Location l: locationsArray){
            if(l.getUsername().equals(username)){
                userLocations.add(l);
            }
            
        }
        return userLocations;
        
    }
    

    /**
     * Delete method to delete a location.
     * @param uli the unique location identifier for the location to be delete.
     * @return Response OK if successfully deleted, NOCONTENT if the location does not exist.
     */
    @DELETE
    @Path("{uli}")
    public Response deleteLocation(@PathParam("uli") String uli) {
        System.out.println("deleteLocation");
        if(locations.containsKey(uli)){
            locations.remove(uli);
            return Response.ok().build();
        }
        else 
        {
            return Response.noContent().build();
        }
        
    }

    /**
     * Method to post a location to the server to be stored in the hashmap
     * @param loc the location to be stored
     * @param context context of the request
     * @return Response OK if the location already exists, CREATED if successfully added.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postLocationJSONorXML(Location loc, @Context UriInfo context) {
        System.out.println("postLocationJSONorXML");
        if (locations.containsKey(loc.getULI()) == true) {
            try {
                locations.put(loc.getULI(), loc);
                return Response.ok().contentLocation(new URI(context.getRequestUri().toString() + loc.getULI())).entity(loc).build();
            } catch (URISyntaxException e) {
                return null;
            }
        } else {
            try {
                locations.put(loc.getULI(), loc);
                return Response.created(new URI(context.getRequestUri().toString() + loc.getULI())).entity(loc).build();
            } catch (URISyntaxException e) {
                return null;
            }
        }
    }

    /**
     * Method to post a location to the server to be stored in the hashmap
     * @param uli the unique location identifier that we want to store the location under.
     * @param loc the location to be stored
     * @param context context of the request
     * @return Response OK if the location already exists, CREATED if successfully added.
     */
    @PUT
    @Path("{uli}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putLocationJSONorXML(@PathParam("uli") String uli, Location loc, @Context UriInfo context) {
        System.out.println("putLocationJSONorXML");
        if (locations.containsKey(uli) == true) {
            locations.put(uli, loc);
            return Response.ok().build();
        } else {
            locations.put(uli, loc);
            return Response.created(context.getRequestUri()).build();
        }
    }
}

    
            

