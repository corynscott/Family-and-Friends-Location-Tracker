/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.AppUser;
import entity.Location;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The LocationSSB provides access to the location database entries, all code in the rest of the project accesses location data in the database through this class. 
 * @author Coryn Scott
 */
@Stateless
public class LocationSSB {
    /**
     * Entity Manager provides us with access to the database
     */
    @PersistenceContext
    EntityManager em;
    
    /**
     * This allows us to add a location to the location table in the database. it takes a location object and stores it in the database
     * @param location this is our location to be stored in the database
     * @return true if the location has successfully be stored.
     */
    public boolean addLocation(Location location){
        em.persist(location);
        return true;
    }
   
    /**
     * This will return all the locations for as specific user, where the user's username is passed as a parameter.
     * @param username the username of the user we want to get the locations of.
     * @return List of locations for the specific user, specified by the username parameter..
     */
    public List<Location> getUserLocations(String username){
        List<Location> userLocations = em.createNamedQuery("getUserLocations").setParameter("username", username).getResultList();
        return userLocations;
    }
    
    /**
     * This method provides a list of all the locations stored in the database, only used for testing.
     * @return list of all locations in the database.
     */
    public List<Location> getAllLocations(){
        List<Location> allLocations = em.createNamedQuery("getAllLocations").getResultList();
        return allLocations;
    }
    
    /**
     * This method add a check-in to the database, the username is a field in the location object that is passed to it, a check-in is the same as a location except for the boolean field in the location object called check-in is set to true. 
     * @param location the check-in (location that we want to store
     * @return true if the check-in (location) is stored.
     */
    public boolean addCheckIn(Location location){
        em.persist(location);
        return true;
    }
    /**
     * this method returns all the check-ins of a specific user where the username of that user is passed as a parameter, a check-in is the same as a location object except for the boolean field in the location object called check-in is set to true. 
     * @param username of the user we want to get the check-ins for.
     * @return list of check-ins (locations) for the specific user.
     */
    public List<Location> getUserCheckIns(String username){
        List<Location> userCheckIns = em.createNamedQuery("getUserCheckIns").setParameter("username", username).getResultList();
        return userCheckIns;
    
    }
  /**
     * This method provides a list of all the check-ins (locations) stored in the database, only used for testing.
     * @return list of all check-ins (locations) in the database.
     */
    public List<Location> getAllCheckIns(){
        List<Location> allCheckIns = em.createNamedQuery("getAllCheckIns").getResultList();
        return allCheckIns;
    }
}
