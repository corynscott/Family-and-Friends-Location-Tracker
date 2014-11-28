/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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
public class RestfulClient {

    String ROOTURL = "http://54.171.93.166:8080/LocationSeverRestful/";

    public void addUser(String name,String password,String phoneNumber, String username) {
        try {
            String subURL = "user/add";
            String data = "{\"@name\":\""+username+"\",\"@password\":\""+password+"\",\"@phoneNumber\":\""+phoneNumber+"\",\"@username\":\""+username+"\"}";

            postRequest(subURL,data);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void loginUser(String username,String password) {
        try {
            String subURL = "user/login";
            String data = "{\"@username\":\""+username+"\",\"@password\":\""+password+"\"}";

            postRequest(subURL,data);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addLocation(String username,String longitude ,String latitude, String comment, String dateandtime, String place) {
        try {
            String subURL = username+"/addLocation";
            String data = "{\"@checkIn\":\"false\",\"@comment\":\""+comment+"\",\"@dateandtime\":\""+dateandtime+"\",\"@latitude\":\""+latitude+"\",\"@longitude\":\""+longitude+"\",\"@place\":\""+place+"\"}";
            postRequest(subURL,data);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void addCheckIn(String username,String longitude ,String latitude, String comment, String dateandtime, String place) {
        try {
            String subURL = username+"/addCheckIn";
            String data = "{\"@checkIn\":\"true\",\"@comment\":\""+comment+"\",\"@dateandtime\":\""+dateandtime+"\",\"@latitude\":\""+latitude+"\",\"@longitude\":\""+longitude+"\",\"@place\":\""+place+"\"}";
            postRequest(subURL,data);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getUserLocations(String username){
        String subURL = username+"/locations";
        getRequest(subURL);
    }
    public void getUserCheckIns(String username){
        String subURL = username+"/checkIns";
        getRequest(subURL);
    }

    public HttpResponse getRequest(String subURL) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(ROOTURL + subURL);
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = null;
        try {
            response = httpClient.execute(getRequest);
        } catch (IOException ex) {
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        processResponse(response);
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
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);

        }
        
        processResponse(response);
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
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException npe){
            System.err.println("null");
        }
        finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(RestfulClient.class.getName()).log(Level.SEVERE, null, ex);
            }catch (NullPointerException npe){
        }
        }
        
    }
    

    public static void main(String[] args) {
        RestfulClient rc = new RestfulClient();
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