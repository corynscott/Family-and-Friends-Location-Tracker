/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restful;

import friend.UsernameNumber;
import ejb.LocationSSB;
import ejb.UserSSB;
import entity.AppUser;
import entity.Location;
import friend.FriendBond;
import java.net.URI;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * This Class Provides a Restful interface to the system, specifically for friends interactions, with the path /friends
 * @author Coryn Scott
 */
@Singleton
@Path("friends")
public class RSFriends {

    @EJB
    UserSSB ussb;
    @EJB
    LocationSSB lssb;

    /**
     * Post method for getting a list of potential friends, the client sends a list of UsernameNumbers with empty usernames, this method looks up the numbers its been sent by the client and responds with a refined list of UsernameNumbers where the telephone number matches a user on the system.
     * For Example Client sends (Username:" ",Number"0770000001")
     * Response (Username:"John",Number"0770000001")
     * @param numbers the list of numbers that the client want to look up
     * @param username the username of the client
     * @param context context path
     * @return List of UsernameNumbers where the username 
     */
    @POST
    @Path("{username}/potentialfriends")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UsernameNumber> postNumbersJsonOrXML(List<UsernameNumber> numbers, @PathParam("username") String username, @Context UriInfo context) {
        List<UsernameNumber> usernameNumbers = new ArrayList();
        for (UsernameNumber n : numbers) {
            AppUser user = ussb.getUserByPhoneNumber(modifyNumber(n.getNumber()));
            if (user != null) {
                UsernameNumber un = new UsernameNumber(user.getUsername(), user.getPhoneNumber());
                usernameNumbers.add(un);
            }
        }
        return usernameNumbers;
    }
    
    /**
     * Get method to get all usernames and numbers on the system, used for testing.
     * @param username username of the client
     * @param context context of the request
     * @return List of UsernameNumbers for all users on the system
     */
    @GET
    @Path("{username}/allnumbers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UsernameNumber> NumbersJsonOrXML( @PathParam("username") String username, @Context UriInfo context) {
        List<AppUser> users = ussb.getAllUsers();
        
        List<UsernameNumber> usernameNumbers = new ArrayList();
        for (AppUser au : users) {
                UsernameNumber un = new UsernameNumber(au.getUsername(), au.getPhoneNumber());
                usernameNumbers.add(un);
            
        }
        return usernameNumbers;
    }

    /**
     * Get method for requesting a friendship, based on the clients username (initUsername) and the other user they want to befriend (recpUsername).
     * @param initUsername client username
     * @param recpUsername username of user the client wants to befriend
     * @param context context of the request
     * @return Response OK - if the request is successful, NOTMODIFIED- if the username don't exist or the request has already been made or if the users are already friends.
     */
    @GET
    @Path("{initUsername}/requestfriend/{recpUsername}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response requestFriendJsonOrXML(@PathParam("initUsername") String initUsername, @PathParam("recpUsername") String recpUsername, @Context UriInfo context) {
        if (ussb.doesUsernameExist(recpUsername) && ussb.doesUsernameExist(initUsername) && !recpUsername.equals(initUsername)) {
            FriendBond fb = new FriendBond(initUsername, recpUsername);
            AppUser initUser = ussb.getUser(initUsername);
            AppUser recpUser = ussb.getUser(recpUsername);
            List<FriendBond> initRequests = initUser.getFriendRequest();
            List<FriendBond> recpRequests = recpUser.getFriendRequest();
            for (FriendBond fbondi : initRequests) {
                if (fbondi.getInitUsername().equals(initUser.getUsername())&&fbondi.getRecpUsername().equals(recpUser.getUsername())) {
                         return Response.notModified().build(); 
                    }
                else if (fbondi.getInitUsername().equals(recpUser.getUsername())&&fbondi.getRecpUsername().equals(initUser.getUsername())) {
                        return Response.notModified().build(); 
                }
            }
            if(!initUser.getFriends().contains(recpUser)&&!recpUser.getFriends().contains(initUser)){
            initUser.addFriendRequest(fb);
            recpUser.addFriendRequest(fb);
            return Response.ok().build();
            }

        }
        return Response.notModified().build();


    }

    /**
     * Get method for getting a list of pending friends, i.e. the list of friend requests.
     * @param username clients username
     * @param context context of the request
     * @return List of FriendBonds for all of the pending friends.
     */
    @GET
    @Path("{username}/pendingfriends")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<FriendBond> getPendingFriendsJsonOrXML(@PathParam("username") String username, @Context UriInfo context) {
        if (ussb.doesUsernameExist(username)) {
            return ussb.getUser(username).getFriendRequest();

        }
        return null;


    }
    
    /**
     * Get method for getting a list of user's friends 
     * @param username of the client
     * @param context context of the request
     * @return List of AppUsers, one for each friend, or null if the user has no friends.
     */
    @GET
    @Path("{username}/friends")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<AppUser> getFriendsJsonOrXML(@PathParam("username") String username, @Context UriInfo context) {
        if (ussb.doesUsernameExist(username)) {
            List<AppUser> userswithpassword =  ussb.getUser(username).getFriends();
            List<AppUser> userswithoutpassword = new ArrayList<AppUser>();
            for(AppUser au:userswithpassword){
                AppUser au1 = new AppUser(au.getName(),au.getUsername(),"",au.getPhoneNumber());
                userswithoutpassword.add(au1);
            }
            return userswithoutpassword;
        }
        return null;


    }

    /**
     * Get method to Accept a friend request.
     * @param initUsername the username of the user who requested the friendship.
     * @param recpUsername the username of the user who has been request to be a friend.
     * @param context context of the request.
     * @return Response OK - friendship has been successful, NOTMODIFIED if the request between these two users is not present.
     */
    @GET
    @Path("{recpUsername}/acceptfriend/{initUsername}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response acceptFriendJsonOrXML(@PathParam("initUsername") String initUsername, @PathParam("recpUsername") String recpUsername, @Context UriInfo context) {
        if (ussb.doesUsernameExist(recpUsername) && ussb.doesUsernameExist(initUsername)) {
            AppUser initUser = ussb.getUser(initUsername);
            AppUser recpUser = ussb.getUser(recpUsername);
            List<FriendBond> initRequests = initUser.getFriendRequest();
            List<FriendBond> recpRequests = recpUser.getFriendRequest();
            recpUser.addFriend(initUser);
            initUser.addFriend(recpUser);
            
            
                recpUser.removeFriendRequest(initUsername,recpUsername);
                initUser.removeFriendRequest(initUsername,recpUsername);
                return Response.ok().build();
                
            
                 

        }
        return Response.notModified().build();
    }
    
     /**
     * Get method to reject a friend request.
     * @param initUsername the username of the user who requested the friendship.
     * @param recpUsername the username of the user who has been request to be a friend.
     * @param context context of the request.
     * @return Response OK - friendship has been successfully been declined, NOTMODIFIED if the request between these two users is not present.
     */
    @GET
    @Path("{recpUsername}/declinefriend/{initUsername}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response declineFriendrequestJsonOrXML(@PathParam("initUsername") String initUsername, @PathParam("recpUsername") String recpUsername, @Context UriInfo context) {
        if (ussb.doesUsernameExist(recpUsername) && ussb.doesUsernameExist(initUsername)) {
            AppUser initUser = ussb.getUser(initUsername);
            AppUser recpUser = ussb.getUser(recpUsername);
            recpUser.removeFriendRequest(initUsername,recpUsername);
            initUser.removeFriendRequest(initUsername,recpUsername);
            return Response.ok().build();
                
            
                 

        }
        return Response.notModified().build();
    }

    /**
     * Get method to get a friends locations.
     * @param username clients username
     * @param friendUsername friends username
     * @param context
     * @return list of the friends locations
     */
    @GET
    @Path("{username}/friend/{friendUsername}/locations")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getFriendLocationsrequestJsonOrXML(@PathParam("username") String username, @PathParam("friendUsername") String friendUsername, @Context UriInfo context) {
        if (ussb.doesUsernameExist(friendUsername) && ussb.doesUsernameExist(username)) {
            AppUser user = ussb.getUser(username);
            if(user.isFriend(friendUsername)){
                return lssb.getUserLocations(friendUsername);
            }
                

        }
        return null;
    }
    /**
     * Get method to get a friends check-ins.
     * @param username clients username
     * @param friendUsername friends username
     * @param context
     * @return list of the friends check-ins.
     */
    @GET
    @Path("{username}/friend/{friendUsername}/checkins")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getFriendcheckinsrequestJsonOrXML(@PathParam("username") String username, @PathParam("friendUsername") String friendUsername, @Context UriInfo context) {
        if (ussb.doesUsernameExist(friendUsername) && ussb.doesUsernameExist(username)) {
            AppUser user = ussb.getUser(username);
            if(user.isFriend(friendUsername)){
                return lssb.getUserCheckIns(friendUsername);
            }
                

        }
        return null;
    }
    
    /**
     * Get method to Delete a friend.
     * @param initUsername the username of the user who is deleting the friend.
     * @param recpUsername the username of the user who is being unfriended.
     * @param context context of the request.
     * @return Response OK - friendship has been successfully been deleted, NOTMODIFIED if the friendship between these two users does not exist.
     */
    @GET
    @Path("{initUsername}/deletefriend/{recpUsername}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteFriendrequestJsonOrXML(@PathParam("initUsername") String initUsername, @PathParam("recpUsername") String recpUsername, @Context UriInfo context) {
        if (ussb.doesUsernameExist(recpUsername) && ussb.doesUsernameExist(initUsername)) {
            AppUser initUser = ussb.getUser(initUsername);
            AppUser recpUser = ussb.getUser(recpUsername);
            if(initUser.isFriend(recpUsername)){
                initUser.deleteFriend(recpUser);
                recpUser.deleteFriend(initUser);
                return Response.ok().build();
            }
                
        }
        return Response.notModified().build();
    }
    
    /**
     * Method to modify a number to ensure there is no issue when it comes to users storing phone number with 0 or +44
     * @param number to modify
     * @return the number with +44 prefix if it starts with 0.
     */
    public String modifyNumber(String number) {
        if (number.startsWith("0")) {
            return "+44" + number.substring(1);
        } else {
            return number;
        }
    }
}
