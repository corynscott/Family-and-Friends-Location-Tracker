/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Coryn Scott
 */
public class AuthClient {

    /**
     * @param args the command line arguments
     */
    public void getall() throws IOException {
        
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:8080/LocationAppServer/location/all");
        getRequest.addHeader("accept", "application/json");
        byte[] encodingb = Base64.encodeBase64("user1:p".getBytes());
        String encoding = new String(encodingb);
        System.out.println(new String(encodingb));
        getRequest.addHeader("Authentication:", encoding);        
        
        
        System.out.println(getRequest.toString());
        Header[] headers = getRequest.getAllHeaders();
        for (int i =0 ; i<headers.length; i++){
            System.out.println(headers[i].getName()+" " + headers[i].getValue());
        }
        
        HttpResponse response = httpClient.execute(getRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        httpClient.getConnectionManager().shutdown();
    }
    
    public void post(){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("http://localhost:8080/LocationAppServer/index");
                
        postRequest.addHeader("authentication", "Basic dXNlcjE6cGFzc3dvcmQ=");
        
        
    }

    public static void main(String[] args) throws IOException {
        AuthClient c = new AuthClient();
        c.getall();
                
    }
}
