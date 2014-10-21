/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package locationappserver;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
 *
 * @author Coryn Scott
 */

@Singleton
@Path("location")
public class RSLocation {

    private final HashMap<String, Location> locations;

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
    
   @GET
   @Path("{uli}")
   @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Location getLocationJSONorXML(@PathParam("uli") String uli) {
        System.out.println("getLocationJSONorXML");
        return locations.get(uli);
    }
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllLocationsJSONorXML() {
        System.out.println("getAllLocationsJSONorXML");
        return new ArrayList<Location>(locations.values());
    }
    
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

  
    public void init() {
        System.out.println("Singleton Object for this RESTfull Web Service has been constructed!!");
    }

    @PreDestroy
    public void clean() {
        System.out.println("Singleton Object for this RESTfull Web Service has been destroyed!!");
    }
}

    
            

