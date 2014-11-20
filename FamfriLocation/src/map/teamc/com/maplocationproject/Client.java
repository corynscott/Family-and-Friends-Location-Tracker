package map.teamc.com.maplocationproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.entity.StringEntity;
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

import android.support.v7.app.ActionBarActivity;

/**
 * Created by alex64 on 30/10/2014.
 */

/**
 * 
 * @Class: Client
 * @Description
 * 		Class that makes the request calls to the server
 * @Created: 01/11/2014
 *
 */
public class Client {
	
    private final static String IP = "54.171.93.166:8080";
    //private final static String APP = "LocationAppServer";
    private final static String APP = "LocationSeverRestful";
    //private final static String ROOT = "location";
    private String URL = "";

    private final static String HEADER = "application/json";
    private static final String MyPREFERENCES = "MyPrefs";
    
    private ArrayList<HashMap<String,String>> locationList = new ArrayList<HashMap<String, String>>();
    private ArrayList<UserLocation> historyCheckin = new ArrayList<UserLocation>();
    
    Client(){
        //URL = "http://"+ IP + "/" + APP + "/" + ROOT + "/";
    	URL = "http://"+ IP + "/" + APP + "/";
    }
    
    /**
     * ****************************************************************************************
     * Beginning Login methods
     */
    
    /**
     * @Function: firstLogin
     * @Access:  Public
     * @Return: void
     * @Description:
     * 		Method that will make a POST request to the server with the credentials of the user
     * 		for the first time.
     * @param context
     * 		Context of the Activity
     * 		Get SharedPreferences to get the username and password
     * @param userName
     * 		Username to login
     * @param pwd
     * 		Password to login
     * 
     * @Created: 18/11/2014
     * @Modifications:
     * 		@1m 
     * 
     */
    public void firstLogin(final Context context, final String userName, final String pwd){
    	Log.d("Client", "firstLogin: Context=" + context + 
    			"\nuserName = " + userName);
    	
    	final SharedPreferences preferences;
    	preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	SyncHttpClient clientHTTP = new SyncHttpClient();
        String requestURL = URL + "user/login";
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("@username",userName);
            jsonParams.put("@password", pwd);

            StringEntity entity = new StringEntity(jsonParams.toString());
            clientHTTP.post(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Login Correct
            	 * 			401 - Unauthorized (Either username or password are incorrect)
            	 */
            	 @Override
                 public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                    Log.d("LoginResponse", "onSuccess: statusCode = " + statusCode +
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                    
                    
                    if (statusCode == 200){
                    	Log.d("statusCode", "if true: " + statusCode);
                    	
                    	SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_famfri",userName);
                        editor.putString("pwd_famfri",pwd);
                        editor.commit();
                    	
                    	goToMap(context);
                    }
                    else{
                    	Log.d("statusCode", "if false: " + statusCode);
                    	/*ToastMEssage*/
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                    Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    /*ToastMessage*/
                }
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);
        	/*ToastMessage*/
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);
        	/*ToastMessage*/
        }
    }
    
    /**
     * @Function: sendCredential
     * @Access:  Public
     * @Return: void
     * @Description:
     * 		Method that will make a POST request to the server with the credentials of the user
     * @param context
     * 		Context of the Activity
     * 		Get SharedPreferences to get the username and password
     * @Created: 18/11/2014
     * @Modifications:
     * 		@1m 
     * 
     */
    public void sendCredential(final Context context){    	
    	Log.d("Client", "sendCredential: Context=" + context);
    	
    	SharedPreferences preferences;    	
    	preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);    	
    	String username = preferences.getString("user_famfri", "");
    	
    	SyncHttpClient clientHTTP = new SyncHttpClient();
        String requestURL = URL + "user/login";
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("@username",username);
            jsonParams.put("@password", preferences.getString("pwd_famfri", ""));

            StringEntity entity = new StringEntity(jsonParams.toString());
            clientHTTP.post(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Login Correct
            	 * 			401 - Unauthorized (Either username or password are incorrect)
            	 */
            	 @Override
                 public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                    Log.d("LoginResponse", "onSuccess: statusCode = " + statusCode +
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                    
                    
                    if (statusCode == 200){
                    	
                    	Log.d("statusCode", "if true: " + statusCode);
                    	goToMap(context);
                    }
                    else{
                    	Log.d("statusCode", "if false: " + statusCode);
                    	/*ToastMEssage*/
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                    Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    /*ToastMessage*/
                }
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);
        	/*ToastMessage*/
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);
        	/*ToastMessage*/
        }
    }
    
    /**
     * End Login methods
     * ****************************************************************************************
     */
    
    /**
     * Beginning Register methods
     */
    
    /**
     * @Function: registerUser
     * @Access: public
     * @Return: void
     * @Description
     * 		Send a POST request to the server to register a new user
     * @param context
     * 		Context of the Activity
     * @param registerUser
     * 		User object
     * @Created: 19/11/2014
     */
    public void registerUser(final Context context, User registerUser){
    	Log.d("Client", "registerUser: context = " + context +
    			"\nregisterUser = " + registerUser);
    	SyncHttpClient clientHTTP = new SyncHttpClient();
        String requestURL = URL + "user/add";
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("@username",registerUser.getUserName());
            jsonParams.put("@password", registerUser.getPwd());
            jsonParams.put("@name",registerUser.getName());
            jsonParams.put("@phoneNumber", registerUser.getPhoneNumber());

            StringEntity entity = new StringEntity(jsonParams.toString());
            clientHTTP.post(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			201 - Created
            	 * 			409 - Conflict (username already exists)
            	 */
            	 @Override
                 public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                    Log.d("RegisterResponse", "onSuccess: statusCode = " + statusCode +
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                    
                    
                    if (statusCode == 201){
                    	
                    	Log.d("statusCode", "if true: " + statusCode);
                    	/*ToastMessage*/
                    	Intent login = new Intent(context, TestLogin.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(login);
                        ((ActionBarActivity) context).finish();
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                    Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    Log.d("statusCode", "if false: " + statusCode);
                    /*ToastMessage*/
                }
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);
        	/*ToastMessage*/
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);
        	/*ToastMessage*/
        }
    }
    
    /**
     * End Register methods
     */
    
    /**
     * Beginning Location Methods
     */
    
    /**
     * @Function: addLocation
     * @Access: public
     * @Return: void
     * @Description
     * 		Send a POST request to the server to add a new location
     * @param context
     * 		Context of the Activity
     * @param userLocation
     * 		Location object of the user
     * @Created: 01/11/2014
     */
    public void addLocation(UserLocation userLocation, final Context context){
    	Log.d("Client", "addLocation: userLocation = " + userLocation + "\ncontext = " + context);

    	String requestURL;

        AsyncHttpClient clientHTTP = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("@dateandtime", userLocation.getDate());
            jsonParams.put("@longitude",userLocation.getLongitude());
            jsonParams.put("@latitude", userLocation.getLatitude());
            jsonParams.put("@checkIn", userLocation.isCheckin());
            jsonParams.put("@comment", userLocation.getComment());
            jsonParams.put("@place", userLocation.getPlaceName());

            StringEntity entity = new StringEntity(jsonParams.toString());
            requestURL = URL + userLocation.getUsername() + "/addLocation";
            clientHTTP.post(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			201 - Location Added
            	 * 			304 - NotModified (username is not valid)
            	 */
            	@Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                	Log.d("AddLocation", "onSuccess: statusCode = " + statusCode +
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                	
                    if (statusCode == 201){           	
                    	Log.d("statusCode", "if true: " + statusCode);
                    }
                    else{
                    	Log.d("statusCode", "if false: " + statusCode);
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                	Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    /*ToastMessage*/
                }
            });
        }
        catch(JSONException e){

        }
        catch(UnsupportedEncodingException e) {

        }
    }
    
    
    /**
     * End Location Methods
     */
    
    /**
     * Beginning Checkin Methods
     */
    
    /**
     * @Function: addCheckin
     * @Access: public
     * @Return: void
     * @Description
     * 		Send a POST request to the server to add a new checkin
     * @param context
     * 		Context of the Activity
     * @param userLocation
     * 		Location object of the user
     * @Created: 01/11/2014
     */
    public void addCheckin(final Context context, UserLocation userLocation){
    	Log.d("Client", "addCheckin: userLocation = " + userLocation + "\ncontext = " + context);

    	String requestURL;

        AsyncHttpClient clientHTTP = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("@dateandtime", userLocation.getDate());
            jsonParams.put("@longitude",userLocation.getLongitude());
            jsonParams.put("@latitude", userLocation.getLatitude());
            jsonParams.put("@checkIn", userLocation.isCheckin());
            jsonParams.put("@comment", userLocation.getComment());
            jsonParams.put("@place", userLocation.getPlaceName());

            StringEntity entity = new StringEntity(jsonParams.toString());
            requestURL = URL + userLocation.getUsername() + "/addCheckIn";
            clientHTTP.post(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			201 - Checkin Added
            	 * 			304 - NotModified (username is not valid)
            	 */
            	@Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                	Log.d("AddLocation", "onSuccess: statusCode = " + statusCode +
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                	
                    if (statusCode == 201){           	
                    	Log.d("statusCode", "if true: " + statusCode);
                    }
                    else{
                    	Log.d("statusCode", "if false: " + statusCode);
                    }
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                	Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    /*ToastMessage*/
                }
            });
        }
        catch(JSONException e){

        }
        catch(UnsupportedEncodingException e) {

        }
    }
    
    /**
     * 
     */
    public void getUserCheckin(String userName){
    	Log.d("Cient", "getUserCheckin: userName = " + userName);
    	SyncHttpClient clientHTTP = new SyncHttpClient();
        String requestURL = URL + userName + "/checkIns";
        clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
        	
        	/**
        	 * onSuccess
        	 * 		int statusCode
        	 * 			200 - Data
        	 * 			204 - Not Content (username invalid?)
        	 */
           @Override
           public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
               Log.d("getUserCheckin", "onSuccess: statusCode = " + statusCode +
               		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
        	   String xmlData = new String(responseBody);
               if(xmlData != ""){
                   Log.d("xmlData", "if true: xmlData = " + xmlData);
                             	   
                	   Document b1 = getXML(xmlData);
                	   
                       if(b1 != null){
                    	   Log.d("b1", "if true: b1 is not null");
                           Node locationNode;
                           NamedNodeMap attributes;
                           locationNode = b1.getFirstChild().getFirstChild();
                           if(locationNode != null){
                               UserLocation location;
                               do{
                                   location = new UserLocation();
                                   attributes = locationNode.getAttributes();                                  
                            	   location.setUsername(attributes.getNamedItem("appUsername").getNodeValue());
                            	   location.setCheckin(Boolean.valueOf(attributes.getNamedItem("checkIn").getNodeValue()));
                            	   location.setDate(attributes.getNamedItem("dateandtime").getNodeValue());
                            	   location.setLongitude(Double.valueOf(attributes.getNamedItem("longitude").getNodeValue()));
                            	   location.setLatitude(Double.valueOf(attributes.getNamedItem("latitude").getNodeValue()));
                            	   location.setComment(attributes.getNamedItem("comment").getNodeValue());
                            	   location.setUli(attributes.getNamedItem("uli").getNodeValue());
                            	   if(attributes.getNamedItem("place") != null){
                            		   location.setPlaceName(attributes.getNamedItem("place").getNodeValue());
                            	   }
                            	   else{
                            		   location.setPlaceName("Missing Place");
                            	   }
                            	   Log.d("Adding", "Adding checkin");
                                   historyCheckin.add(location);
                               }
                               while((locationNode = locationNode.getNextSibling()) != null);
                           }
                       }
                       else{
                    	   Log.d("b1", "if false: b1 is null");
                       }
               }
               else{
            	   Log.d("xmlData", "if false: xmlData = " + xmlData);
               }
           }

            @Override
            public void onFinish(){
                Log.d("getUserCheckin", "onFinish");
            }
        });
    }
    
    /**
     * 
     * @return
     */
    public ArrayList<UserLocation> getHistoryCheckin(){
        return historyCheckin;
    }
    
    /**
     * End Checkin Methods
     */
    
    /**
     * Beginning Other Methods
     */
    
    /**
     * @Function: goToMap
     * @Access:  Private
     * @Return: Void
     * @Description
     * 		Code to start the Map activity and kill the current activity
     * @param context
     *		Move to another Activity if Login is successful
     *
     * @Created: 18/11/2014
     * 
     * @Modifications:
     * 		@1m
     */
    private void goToMap(Context context){
    	Intent map = new Intent(context, MapActivity.class);
        map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(map);
        ((ActionBarActivity) context).finish();
    }
    
    /**
     * 
     * @param responseBody
     * @return
     */
    private Document getXML(String responseBody){
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document b1 = null;
		try {
			builder = factory.newDocumentBuilder();
			b1 = builder.parse(new InputSource(new StringReader(responseBody)));
			return b1;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b1;
    }
        
    /**
     * End Other Methods
     */
    
    
    
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
        String encoded = new String(Base64.encode("user:password".getBytes(), Base64.NO_WRAP));
        //clientHTTP.setBasicAuth("user","password");
        clientHTTP.addHeader("Authorization", "Basic " + encoded);
        clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){

           public ArrayList<HashMap<String,String>> locationList = new ArrayList<HashMap<String, String>>();
           @Override
           public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
               String xmlData = new String(responseBody);
               String time = "";
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
                            new GetLocation().execute("","a");
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

    private class GetLocation extends AsyncTask<String, Integer, Long>{
        @Override
        protected Long doInBackground(String... param){
            Log.d("Background","Client");
            return 0L;
        }

        @Override
        protected void onProgressUpdate(Integer... param){
            Log.d("Progress","Client");
        }

        @Override
        protected void onPostExecute(Long param){
            Log.d("Post","Client");
        }
    }
}
