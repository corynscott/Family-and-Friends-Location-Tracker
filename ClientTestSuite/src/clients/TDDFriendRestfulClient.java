/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Coryn Scott
 */
public class TDDFriendRestfulClient {

    String ROOTURL = "http://localhost:8080/LocationSeverRestful/friends/";

    public String getPotentialFriends(String username,String data) {
        try {
            String subURL = username+"/potentialfriends";
            HttpResponse response = postRequest(subURL,data);
            String responseStr = extractResponse(response);
            return responseStr;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   
    public String requestFriendship(String initatiorusername,String recipientusername) {
        
            String subURL = initatiorusername+"/requestfriend/"+recipientusername;

            HttpResponse response = getRequest(subURL);
            String responseStr = extractStatusResponse(response);
            return responseStr;
        
    }
    public String acceptFriendship(String recipientusername,String initatiorusername) {
        
            String subURL = recipientusername+"/acceptfriend/"+initatiorusername;

            HttpResponse response = getRequest(subURL);
            String responseStr = extractStatusResponse(response);
            return responseStr;
        
    }
    public String declineFriendship(String recipientusername,String initatiorusername) {
        
            String subURL = recipientusername+"/declinefriend/"+initatiorusername;

            HttpResponse response = getRequest(subURL);
            String responseStr = extractStatusResponse(response);
            return responseStr;
        
    }
    
     public String getFriendList(String username) {
        
            String subURL = username+"/friends";

            HttpResponse response = getRequest(subURL);
            String responseStr = extractResponse(response);
            return responseStr;
        
    }
     
      public String getPendingFriendList(String username) {
        
            String subURL = username+"/pendingfriends";

            HttpResponse response = getRequest(subURL);
            String responseStr = extractResponse(response);
            return responseStr;
        
    }
      
       public String deleteFriend(String username, String friendToDeleteUsername) {
        
            String subURL = username+"/deletefriend/" + friendToDeleteUsername;

            HttpResponse response = getRequest(subURL);
            String responseStr = extractStatusResponse(response);
            return responseStr;
        
    }
        
    public String getFriendLocations(String username, String friendUsername){
        String subURL = username+"/friend/"+friendUsername+"/locations";
        HttpResponse response = getRequest(subURL);
        String responseStr = extractResponse(response);
        return responseStr; 
    }
    public String getFriendCheckIns(String username, String friendUsername){
        String subURL = username+"/friend/"+friendUsername+"/checkins";
        HttpResponse response = getRequest(subURL);
        String responseStr = extractResponse(response);
        return responseStr; 
    }
   

    public HttpResponse getRequest(String subURL) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(ROOTURL + subURL);
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = null;
        try {
            response = httpClient.execute(getRequest);
        } catch (IOException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        httpClient.getConnectionManager().shutdown();
        return response;

    }

    public HttpResponse postRequest(String subURL, String data) throws UnsupportedEncodingException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(ROOTURL + subURL);
        postRequest.setHeader("Content-type", " application/json");
        postRequest.setEntity(new StringEntity(data));
        
        HttpResponse response = null;
        
        try {
            response = httpClient.execute(postRequest);
        } catch (IOException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        httpClient.getConnectionManager().shutdown();
        return response;

    }

    public void processResponse(HttpResponse response) {
        
        System.out.println("Status: " + response.getStatusLine());
        System.out.println("Content ....");
        BufferedReader br = null;
        try {
            Header[] responseHeaders = response.getAllHeaders();
            br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
        } catch (IOException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException npe){
            System.err.println("null");
        }
        finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
            }catch (NullPointerException npe){
        }
        }
        
    }
    public String extractResponse(HttpResponse response) {
        
        BufferedReader br = null;
        try {
            Header[] responseHeaders = response.getAllHeaders();
            br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output="";
            String currentline = br.readLine();
            while (currentline != null) {
                output +=currentline;
                currentline = br.readLine();
            }
            return output;
        } catch (IOException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException npe){
            System.err.println("null");
        }
        finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(TDDFriendRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
            }catch (NullPointerException npe){
        }
        }
        return null;
    }
    
     public String extractStatusResponse(HttpResponse response) {
        String statusline = "" +response.getStatusLine();
        return statusline;
       
        
    }

    public static void main(String[] args) {
        TDDFriendRestfulClient rc = new TDDFriendRestfulClient();
        System.out.println("---------------------");
        
    }
}