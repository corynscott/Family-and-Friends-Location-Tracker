/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testclient;

import TDDFriends.TDDFriendRestfulClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Coryn Scott
 */
public class TestClientLocationAndUser {

    String ROOTURL = "http://localhost:8080/LocationSeverRestful/";

    public String addUser(String name,String password,String phoneNumber, String username) {
        try {
            String subURL = "user/add";
            String data = "{\"@name\":\""+username+"\",\"@password\":\""+password+"\",\"@phoneNumber\":\""+phoneNumber+"\",\"@username\":\""+username+"\"}";

            HttpResponse response = postRequest(subURL,data);
            String responseStr = extractStatusResponse(response);
            return responseStr;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   
    public String loginUser(String username,String password) {
        try {
            String subURL = "user/login";
            String data = "{\"@username\":\""+username+"\",\"@password\":\""+password+"\"}";

            HttpResponse response = postRequest(subURL,data);
            String responseStr = extractStatusResponse(response);
            return responseStr; 
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String addLocation(String username,String longitude ,String latitude, String comment, String dateandtime, String place) {
        try {
            String subURL = username+"/addLocation";
            String data = "{\"@checkIn\":\"false\",\"@comment\":\""+comment+"\",\"@dateandtime\":\""+dateandtime+"\",\"@latitude\":\""+latitude+"\",\"@longitude\":\""+longitude+"\",\"@place\":\""+place+"\"}";
            HttpResponse response = postRequest(subURL,data);
            String responseStr = extractStatusResponse(response);
            return responseStr; 
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public String addCheckIn(String username,String longitude ,String latitude, String comment, String dateandtime, String place) {
        try {
            String subURL = username+"/addCheckIn";
            String data = "{\"@checkIn\":\"true\",\"@comment\":\""+comment+"\",\"@dateandtime\":\""+dateandtime+"\",\"@latitude\":\""+latitude+"\",\"@longitude\":\""+longitude+"\",\"@place\":\""+place+"\"}";
            HttpResponse response = postRequest(subURL,data);
            String responseStr = extractStatusResponse(response);
            return responseStr; 
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getUserLocations(String username){
        String subURL = username+"/locations";
        HttpResponse response = getRequest(subURL);
        String responseStr = extractResponse(response);
        return responseStr;
        
    }
    public String getUserCheckIns(String username){
        String subURL = username+"/checkIns";
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
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);

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
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);

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
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException npe){
            System.err.println("null");
        }
        finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(TestClientLocationAndUser.class.getName()).log(Level.SEVERE, null, ex);
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
        TestClientLocationAndUser rc = new TestClientLocationAndUser();
        System.out.println("---------------------");
        rc.addUser("justin","password1","076","justin");
        System.out.println("---------------------");
        rc.loginUser("justin","password1");
        System.out.println("---------------------");
        rc.addLocation("justin","000341","000205300","justinno comment","2014-11-17T00:00:00Z","pladcea");
        System.out.println("---------------------");
        rc.addCheckIn("justin","0001","0002000","HEllo this is james","2014-11-17T00:00:00Z", "placdfeb");
        System.out.println("---------------------");
        rc.getUserCheckIns("justin");
        System.out.println("---------------------");
        rc.getUserLocations("justin");
    }
}