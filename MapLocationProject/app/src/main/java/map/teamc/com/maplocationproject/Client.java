package map.teamc.com.maplocationproject;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Window;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alex64 on 30/10/2014.
 */
public class Client {
    private final static String IP = "54.171.93.166:8080";
    private final static String APP = "LocationAppServer";
    private final static String ROOT = "location";
    private String URL = "";

    private final static String HEADER = "application/json";

    Client(){
        URL = "http://"+ IP + "/" + APP + "/" + ROOT + "/";
    }

    private ArrayList<HashMap<String,String>> locationList = new ArrayList<HashMap<String, String>>();
    /**
     * Add a Location to the server based on a ULI
     * PUT request
     */
    public void putLocation(UserLocation userLocation, Context context){
        String requestURL;
        String uli = userLocation.getULI();

        AsyncHttpClient clientHTTP = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("@username",userLocation.getUsername());
            jsonParams.put("@time", userLocation.getDate());
            jsonParams.put("@longitude",userLocation.getLongitude());
            jsonParams.put("@latitude", userLocation.getLatitude());
            jsonParams.put("@ULI",uli);

            StringEntity entity = new StringEntity(jsonParams.toString());
            requestURL = URL +  uli;
            clientHTTP.put(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
                public void onSuccess(String result){
                    Log.d("REQUEST", "Success");
                }

                public void onFailure(String fail){
                    Log.d("Request", "FAILURE");
                }
            });
        }
        catch(JSONException e){

        }
        catch(UnsupportedEncodingException e) {

        }
    }

    /**
     * Delete a Location from the server based on a ULI
     * DELETE request
     */
    public boolean deleteLocation(){
        return true;
    }

    /**
     * Get ALL the Location from the server
     * GET request
     */
    public boolean getLocationAll(){
        SyncHttpClient clientHTTP = new SyncHttpClient();
        String requestURL = URL + "all";
        RequestHandle requestGet;
        requestGet = clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){

           public ArrayList<HashMap<String,String>> locationList = new ArrayList<HashMap<String, String>>();
           @Override
           public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
               String xmlData = new String(responseBody);
               String username[];
               String time = "";
               String longitude[];
               String latitude[];
               if(xmlData != ""){
                   Log.d("Success", xmlData);
                   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                   try {
                       DocumentBuilder builder = factory.newDocumentBuilder();
                       Document b1 = builder.parse(new InputSource(new StringReader(xmlData)));
                       if(b1 != null){
                           Node locationNode;
                           NamedNodeMap attributes;
                           locationNode = b1.getFirstChild().getFirstChild();
                           if(locationNode != null){
                               HashMap<String, String> location;
                               do{
                                   location = new HashMap<String, String>();
                                   attributes = locationNode.getAttributes();
                                   location.put("username",attributes.getNamedItem("username").getNodeValue());
                                   if(attributes.getNamedItem("time") != null){
                                       time = attributes.getNamedItem("time").getNodeValue();
                                   }
                                   else{
                                       time = "no-time";
                                   }
                                   location.put("time", time );
                                   location.put("longitude",attributes.getNamedItem("longitude").getNodeValue());
                                   location.put("latitude",attributes.getNamedItem("latitude").getNodeValue());
                                   locationList.add(location);
                               }
                               while((locationNode = locationNode.getNextSibling()) != null);
                           }
                           obtainData(locationList);
                           Log.d("Results", "Test");
                       }
                       else{
                           Log.d("Error","Error with the XML");
                       }
                   } catch (ParserConfigurationException e) {
                       e.printStackTrace();
                   } catch (SAXException e) {
                       e.printStackTrace();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
               else{
                   Log.d("Error","Response without data");
               }
           }

            @Override
            public void onFinish(){
                //obtainData(locationList);
            }
        });
        Log.d("Finished","Finihed");
        return true;
    }

    public ArrayList<HashMap<String,String>> getLocationList(){
        return locationList;
    }

    /**
     * Get a Location based on a Username
     * GET request
     */
    public boolean getLocationUsername(){
        return true;
    }

    /**
     * GET a Location  based on a ULI
     * GET request
     */
    public boolean getLocationULI(Context context){
        AsyncHttpClient clientHTTP = new AsyncHttpClient();
        String requestURL;
        String uli = "20141022025210";
        Log.d("Searching", uli);
        requestURL = URL + uli;
        Log.d("Searching",requestURL);
        clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            @Override
            public void onStart(){
                Log.d("Starting","Start");
            }

            @Override
            public void onFinish(){
                Log.d("Finishing", "Finish");
            }

            public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                String xmlData = new String(responseBody);
                String username;
                String time;
                String longitude;
                String latitude;
                if(xmlData != ""){
                    Log.d("Success", xmlData);
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    try {
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document b1 = builder.parse(new InputSource(new StringReader(xmlData)));
                        if(b1 != null){
                            Node locationNode;
                            NamedNodeMap attributes;
                            locationNode = b1.getFirstChild();
                            attributes = locationNode.getAttributes();
                            username = attributes.getNamedItem("username").getNodeValue();
                            time = attributes.getNamedItem("time").getNodeValue();
                            longitude = attributes.getNamedItem("longitude").getNodeValue();
                            latitude = attributes.getNamedItem("latitude").getNodeValue();
                            Log.d("Results", "\nusername: " + username + "\ntime: " + time + "\nlongitude: " + longitude + "\nlatitude: " + latitude);
                        }
                        else{
                            Log.d("Error","Error with the XML");
                        }
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Log.d("Error","Response without data");
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                Log.d("Failure","What");
            }
        });
        Log.d("Something","Some");
        return true;
    }

    public void obtainData(ArrayList<HashMap<String,String>> data){
        Log.d("Something","Data");
        locationList = data;
    }
}
