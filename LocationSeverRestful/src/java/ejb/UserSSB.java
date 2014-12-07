/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.AppUser;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The UserSSB provides access to the user database entries, all code in the rest of the project accesses user data in the database through this class. 
 * @author Coryn Scott
 */
@Stateless
public class UserSSB {
    @PersistenceContext
    EntityManager em;
    
    /**
     * This method adds a user to the database. 
     * @param name user's name
     * @param username user's username
     * @param password user's password: The user's password is encrypted using the encryptPassword(password) method prior to storing.
     * @param phoneNumber user's telephone number
     * @return ture when the user is added. otherwise return false.
     */
    public synchronized boolean addUser(String name, String username, String password, String phoneNumber){
            String encryptedPassword;
        try {
            encryptedPassword = encryptPassword(password);
            AppUser user = new AppUser(name,username, encryptedPassword,phoneNumber);
            em.persist(user);
            return true;
        
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserSSB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserSSB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
            
    }


    
    /**
     * This method returns all the current users stored in the database
     * @return list of all AppUsers representing users stored in the database.
     */
    public synchronized List<AppUser> getAllUsers(){
        List<AppUser> users = em.createNamedQuery("getAllUsers").getResultList();
        return users;
    }
    /**
     * This method a specific user with the username passed as a parameter.
     * @param username of the user that we want.
     * @return AppUser representing the user.
     */
    public synchronized AppUser getUser(String username){
        AppUser user = (AppUser) em.createNamedQuery("getUser").setParameter("username", username).getSingleResult();
        return user;
    }
    /**
     * checks that a user has a specific password. The username passed as a parameter along with the unencrypted password is compared to the database to check that the users details are correct.
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the user's details are correct, false if not.
     */
    public synchronized boolean checkPassword(String username,String password){
        AppUser user = (AppUser) em.createNamedQuery("getUser").setParameter("username", username).getSingleResult();
        try {
            return user.getPassword().equals(encryptPassword(password));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserSSB.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserSSB.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        }
       
        
    }
    
    /**
     * This method checks if the user with the username, passed as a parameter, exists on the database.
     * @param username the username that we want to look up.
     * @return true if the user with that username exists on the database. 
     */
    public synchronized boolean doesUsernameExist(String username){
        List<AppUser> users = em.createNamedQuery("getUser").setParameter("username", username).getResultList();
        if (users.isEmpty()) {
               return false;
            
        } 
        else {
            return true;
        }
    }
    
    /**
     * This returns a user based on their phone number passed as a parameter.
     * @param phoneNumber of th user we want
     * @return AppUser representing the user with the phone number passed as a parameter.
     */
    public synchronized AppUser getUserByPhoneNumber(String phoneNumber){
        List<AppUser> users = em.createNamedQuery("getUserByPhoneNumber").setParameter("phoneNumber", phoneNumber).getResultList();
        if (users.isEmpty()) {
               return null;
        } 
        else {
            return users.get(0);
        }
    }

    /**
     * This method encrypts our plain text password to an encrypted password to be stored on the database. The method makes use of the one way hash function, specifically SHA-265 prototcol.
     * @param plainTextPassword the plaintext password
     * @return string representation of the encrypted password 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException
     */
    public synchronized String encryptPassword(String plainTextPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException{
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            String passwordSt = plainTextPassword;
            md.update(passwordSt.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String encryptedPassword = bigInt.toString(16);
            return encryptedPassword;
    }
}
