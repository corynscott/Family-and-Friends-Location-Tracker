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
 *
 * @author Coryn Scott
 */
@Stateless
public class LocationSSB {
    @PersistenceContext
    EntityManager em;
    
    public boolean addLocation(Location location){
        em.persist(location);
        return true;
    }
   
    public List<Location> getUserLocations(String username){
        List<Location> userLocations = em.createNamedQuery("getUserLocations").setParameter("username", username).getResultList();
        
        return userLocations;
    }
    
    public List<Location> getAllLocations(){
        List<Location> allLocations = em.createNamedQuery("getAllLocations").getResultList();
        return allLocations;
    }
    
    public boolean addCheckIn(Location location){
        em.persist(location);
        return true;
    }
    public List<Location> getUserCheckIns(String username){
        List<Location> userCheckIns = em.createNamedQuery("getUserCheckIns").setParameter("username", username).getResultList();
        return userCheckIns;
    
    }
    public List<Location> getAllCheckIns(){
        List<Location> allCheckIns = em.createNamedQuery("getAllCheckIns").getResultList();
        return allCheckIns;
    }
}
