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
 *
 * @author Coryn Scott
 */
@Singleton
@Path("friends")
public class RSFriends {

    @EJB
    UserSSB ussb;
    @EJB
    LocationSSB lssb;

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

    @GET
    @Path("{username}/pendingfriends")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<FriendBond> getPendingFriendsJsonOrXML(@PathParam("username") String username, @Context UriInfo context) {
        if (ussb.doesUsernameExist(username)) {
            return ussb.getUser(username).getFriendRequest();

        }
        return null;


    }
    
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
    public String modifyNumber(String number) {
        if (number.startsWith("0")) {
            return "+44" + number.substring(1);
        } else {
            return number;
        }
    }
}
