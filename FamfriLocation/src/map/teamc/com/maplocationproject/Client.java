package map.teamc.com.maplocationproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * 		Class that makes the request calls to the server
 *
 */

@SuppressLint("SimpleDateFormat")
public class Client {
	
	/*
	 * Contains the information of the IP address of the server
	 */
    private final static String IP = "54.171.93.166:8080";
    
    /*
     * Contains the name of the APP, the first element of the URL to send a Restful request
     */
    private final static String APP = "LocationSeverRestful";
    
    /*
     * Variable the will contain the URL of the Restful request
     */
    private String URL = "";

    /*
     * It will contain an elment of the header that will be send with the request
     */
    private final static String HEADER = "application/json";
    
    /*
     * String that contains the name of the application SharedPreferences
     */
    private static final String MyPREFERENCES = "MyPrefs";
    
    /*
     * The client constructor. It will generate the default URL
     */
    public Client(){
    	URL = "http://"+ IP + "/" + APP + "/";
    }
     
    /**
     * ****************************************************************************************
     * Beginning Login methods
     */
    
    /**
     * 		Method that will make a POST call to the server with the credentials of the user
     * 		for the first time.
     * @param context
     * 		Context of the Activity
     * @param userName
     * 		Username to login
     * @param pwd
     * 		Password to login
     * 
     */
    public void firstLogin(final Context context, final String userName, final String pwd){
    	Log.d("Client", "firstLogin: Context=" + context + 
    			"\nuserName = " + userName);
    	
    	final SharedPreferences preferences;
    	
    	preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	AsyncHttpClient clientHTTP = new AsyncHttpClient();
    	
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
                    	
                	SharedPreferences.Editor editor = preferences.edit();
                	
                    editor.putString("user_famfri",userName);
                    
                    editor.putString("pwd_famfri",pwd);
                    
                    editor.commit(); 
                    
                	goToMap(context);
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                    Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    if(statusCode == 401){
                    	Toast.makeText(context, "Username or password incorrect.\nTry again", 
            					Toast.LENGTH_LONG).show();
                    }
                    else{
                    	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
            					Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);
        	
        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);
        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
        catch(NetworkOnMainThreadException e){
        	
        }
    }
    
    /**
     * 		Method that will make a POST call to the server with the credentials of the user
     * @param context
     * 		Context of the Activity
     * 		Get SharedPreferences to get the username and password
     * 
     */
    public void sendCredential(final Context context){    	
    	Log.d("Client", "sendCredential: Context=" + context);
    	
    	SharedPreferences preferences;    
    	
    	preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE); 
    	
    	String username = preferences.getString("user_famfri", "");
    	
    	AsyncHttpClient clientHTTP = new AsyncHttpClient();
    	
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
                    
                	goToMap(context);
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                    Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);
                    
                    if(statusCode == 401){
                    	Toast.makeText(context, "Username or password incorrect.\nTry again", 
            					Toast.LENGTH_LONG).show();
                    }
                    else{
                    	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
            					Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);

        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);

        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * End Login methods
     * ****************************************************************************************
     */
    
    /**
     * ****************************************************************************************
     * Beginning Register methods
     */
    
    /**
     * 		Send a POST call to the server to register a new user
     * @param context
     * 		Context of the Activity
     * @param registerUser
     * 		User object
     */
    public void registerUser(final Context context, User registerUser){
    	Log.d("Client", "registerUser: context = " + context +
    			"\nregisterUser = " + registerUser);
    	
    	AsyncHttpClient clientHTTP = new AsyncHttpClient();
    	
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
                    
                    
                    Toast.makeText(context, "Registration was Successful", 
        					Toast.LENGTH_LONG).show();
                    
                    SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                	editor.remove("phoneNumber_FamFri");
                    editor.remove("verificationPhone_FamFri");
                    editor.remove("expirationDate_FamFri");
                    editor.commit();
                    
                	Intent login = new Intent(context, LoginActivity.class);
                	
                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    
                    context.startActivity(login);
                    
                    ((ActionBarActivity) context).finish();
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                    Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);

                    if(statusCode == 409){
                    	Toast.makeText(context, "Username already exists. Try with another username", 
            					Toast.LENGTH_LONG).show();
                    }
                    else{
                    	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection",
                    			Toast.LENGTH_LONG).show();
                    }
                }
                
                
                
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);

        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);

        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 
     * End Register methods
     * **********************************************************************************
     */
    
    /**
     * **********************************************************************************
     * Beginning Location Methods
     */
    
    /**
     * 		Send a POST call to the server to add a new location
     * @param context
     * 		Context of the Activity
     * @param userLocation
     * 		Location object of the user
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
                }

                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                	Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                    		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                    		"\nerror = " + error);

                	if(statusCode == 304){
                    	Toast.makeText(context, "Username Invalid - Could not add location to the server", 
            					Toast.LENGTH_LONG).show();
                    }
                    else{
                    	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection",
                    			Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(JSONException e){
        	Log.d("JSONException", "exception = " + e);

        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
        catch(UnsupportedEncodingException e) {
        	Log.d("UnssupportedEncodingException", "exception = " + e);

        	Toast.makeText(context, "Error while creating the request", 
					Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 		Call to GetAllLocations class in order to obtain all the locations stored in the server
     * @param userName
     * 		Username of the application
     * @param context
     * 		Context of the Activity
     * @param googleMap
     * 		GoogleMap variable to add the markers of the location
     */
    public void getAllLocations(String userName, final Context context, GoogleMap googleMap){
    	
    	Log.d("Cient", "getUserCheckin: userName = " + userName);
    	
    	new GetAllLocations(context, userName, googleMap).execute();
    }
    
    /**
     * 
     * 		Class that makes a GET call to the server to obtain all the locations of the user of the app
     * 		Populate the map with markers using the locations data
     *
     */
    private class GetAllLocations extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * ArrayList that will contain an array of HashMaps that have the user's location information
         */
        public ArrayList<HashMap<String,String>> locationList = new ArrayList<HashMap<String, String>>();
        
        /*
         * String variable for the username of the application
         */
        private String userName = "";
        
        /*
         * GoogleMap variable to draw markers on the map
         */
        private GoogleMap googleMap;
        
        /*
         * Status of the result of the call to the server, if it is successful the value will be true
         */
        private boolean status = true;
        
        public GetAllLocations(Context context, String userName, GoogleMap googleMap) {
            this.context = context;
            
            this.userName = userName;
            
            this.googleMap = googleMap;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            
            Log.d("GetAllLocations", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            if(status){
            	MarkerOptions marker;
                
       			for(int i = 0; i < locationList.size(); i++){
       				
       				marker = new MarkerOptions()
       							.position(new LatLng(
       												Double.valueOf(locationList.get(i).get("latitude")), 
       												Double.valueOf(locationList.get(i).get("longitude"))))
    							.title(locationList.get(i).get("username"))
    							.snippet(locationList.get(i).get("time"))
    							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
       				
       				googleMap.addMarker(marker);
       			}
            }
            else{
            	Toast.makeText(context, "There are no locations stored", 
    					Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetAllLocations", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("GetAllLocations", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
            String requestURL = URL + userName + "/locations";
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
               
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data
            	 * 			304 - Username is not valid
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
            	   Log.d("GetAllLocations", "onSuccess: statusCode = " + statusCode + "\nheaders = " + headers + 
            			   "\nresponseBody = " + responseBody);
            	   
                   String xmlData = new String(responseBody);
                   
                   String time = "";
                   
                   if(xmlData != ""){
                       Log.d("Success", xmlData);
                       
                	   Document b1 = getXML(xmlData);
                	   
                       if(b1 != null){
                           Node locationNode;
                           
                           NamedNodeMap attributes;
                           
                           locationNode = b1.getFirstChild().getFirstChild();
                           
                           if(locationNode != null){
                               HashMap<String, String> location;
                               
                               do{
                                   location = new HashMap<String, String>();
                                   
                                   attributes = locationNode.getAttributes();
                                   
                                   if(attributes.getNamedItem("checkIn").getNodeValue().compareTo("false") == 0){
                                	   location.put("username",attributes.getNamedItem("appUsername").getNodeValue());
                                	   
                                       if(attributes.getNamedItem("dateandtime") != null){
                                           time = attributes.getNamedItem("dateandtime").getNodeValue();
                                       }
                                       else{
                                           time = "no-time";
                                       }
                                       location.put("time", time );
                                       
                                       location.put("longitude",attributes.getNamedItem("longitude").getNodeValue());
                                       
                                       location.put("latitude",attributes.getNamedItem("latitude").getNodeValue());
                                       
                                       locationList.add(location);
                                   }
                               }
                               while((locationNode = locationNode.getNextSibling()) != null);
                           }
                           else{
                        	   Log.d("locationNode", "if false: there are no locations");
                        	   
                        	   status = false;
                           }
                       }
                       else{
                           Log.d("Error","Error with the XML");
                       }

                   }
                   else{
                	   Log.d("Error","Error with the XML");
                   }
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
               	Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
               	if(statusCode == 304){
                   	Toast.makeText(context, "Username is not valid", 
           					Toast.LENGTH_LONG).show();
                   }
                   else{
                   	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
           					Toast.LENGTH_LONG).show();
                   }
               }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    
    /**
     * 
     * End Location Methods
     * **********************************************************************************************
     */
    
    /**
     * *********************************************************************************************
     * Beginning Checkin Methods
     */
    
    /**
     * 		Send a POST call to the server to add a new checkin
     * @param context
     * 		Context of the Activity
     * @param userLocation
     * 		Location object of the user
     * 
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
                    	Toast.makeText(context, "Checkin added", 
            					Toast.LENGTH_LONG).show();
                    	
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
                	if(statusCode == 304){
                    	Toast.makeText(context, "Username is not valid", 
            					Toast.LENGTH_LONG).show();
                    }
                    else{
                    	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
            					Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch(JSONException e){

        }
        catch(UnsupportedEncodingException e) {

        }
    }
    
    /**
     * 		Call to GetUserCheckin class in order to obtain all the checkins stored in the server
     * @param userName
     * 		Username of the application
     * @param context
     * 		Context of the Activity
     * @param googleMap
     * 		GoogleMap variable to add the markers of the checkins
     * 
     */
    public void getUserCheckin(String userName, final Context context, GoogleMap googleMap){
    	Log.d("Cient", "getUserCheckin: userName = " + userName);
    	new GetUserCheckin(context, userName, googleMap).execute();
    }
    
    /**
     * 
     * @Class: GetUserCheckin
     * @Description
     * 		Class that makes a GET call to the server to obtain all the checkins of the user of the app
     * 		Populate the map with markers  using the checkins data
     *
     */
    private class GetUserCheckin extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
    	
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * An ArrayList of UserLocation objects that contains the information of the user's checkins
         */
        private ArrayList<UserLocation> historyCheckin = new ArrayList<UserLocation>();
        
        /*
         * String variable for the username of the application
         */
        private String userName = "";
        
        /*
         * GoogleMap variable to draw markers on the map
         */
        private GoogleMap googleMap;
        
        /*
         * Status of the result of the call to the server, if it is successful the value will be true
         */
        private boolean status = true;

        public GetUserCheckin(Context context, String userName, GoogleMap googleMap) {
            this.context = context;
            this.userName = userName;
            this.googleMap = googleMap;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("GetUserCheckin", "onPostExecute");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            if(status){
	            MarkerOptions marker;
	   			for(int i = 0; i < historyCheckin.size(); i++){
	   				marker = new MarkerOptions().position(new LatLng(historyCheckin.get(i).getLatitude(), 
	   										historyCheckin.get(i).getLongitude()))
											.title(historyCheckin.get(i).getPlaceName())
											.snippet(historyCheckin.get(i).getComment())
											.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));;
	   				googleMap.addMarker(marker);
	   			}
            }
            else{
            	Toast.makeText(context, "There are no checkins stored", 
    					Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetUserCheckin", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("GetUserCheckin", "doInBackground");
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
                               else{
                            	   Log.d("locationNode", "if false: there are no checkins");
                            	   
                            	   status = false;
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
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("Somehting", "Somehting in progress");
               }

                @Override
                public void onFinish(){
                    Log.d("getUserCheckin", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * End Checkin Methods
     * **********************************************************************************************
     */
    
    /**
     * **********************************************************************************************
     * Beginning Friend Methods
     */
    
    /**
     * 		Call to GetListFriend class in order to obtain a list of all the user's friends
     * @param context
     * 		Context of the Activity 
     * @param userName
     * 		Username of the application
     * @param infalter
     * 		LayoutInflater variable - will add elements to the current layout
     * 
     */
    public void getListFriendDelete(Context context, String userName, LayoutInflater inflater){
    	Log.d("Cient", "getListFriends: context = " + context + "\nusername = " + userName + 
    			"\ninflater = " + inflater);
    	new GetListFriendDelete(context, userName, inflater).execute();
    }
    
    /**
     * 
     * @Class: GetListFriendDelete
     * @Description
     * 		Class that makes a GET call to the server to obtain a list of user's friends
     * 		Create a Layout with the results
     *
     */
    private class GetListFriendDelete extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * LayoutInflater to create a new Layout within the code
         */
        private LayoutInflater inflater;
        
        /*
         * This object will be used to create the content of the list of friends in a new layout
         */
        FriendDeleteAdapter adapter;
        
        /*
         * An ArrayList of Friend objects that will contain the information of the friends of the user
         */
        private ArrayList<Friend> friendList = new ArrayList<Friend>();
        
        /*
         * String variable for the username of the application
         */
        private String userName;
        
        public GetListFriendDelete(Context context, String userName, LayoutInflater inflater) {
            this.context = context;
            this.userName = userName;
            this.inflater = inflater;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("GetListFriend", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            adapter = new FriendDeleteAdapter(context, R.layout.friend_list_item, friendList, 
            		inflater, userName);
    		ListView contactList = (ListView) ((ActionBarActivity) context).findViewById(R.id.listFriends);
    		contactList.setAdapter(adapter);
    		
            for(int i = 0; i < friendList.size(); i++){
            	Log.d("FriendName", friendList.get(i).getName());
            	Log.d("PhoneNumber", friendList.get(i).getPhoneNumber());
            	Log.d("Username", friendList.get(i).getUsername());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetListFriend", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("GetListFriend", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
        	/**
        	 * 
        	 */
            String requestURL = URL + "friends/" + userName + "/friends";
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			    - Not Content (username invalid?)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("getFriends", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
            	   String xmlData = new String(responseBody);
                   if(xmlData != ""){
                       Log.d("xmlData", "if true: xmlData = " + xmlData);
                                 	   
                    	   Document b1 = getXML(xmlData);
                    	   
                           if(b1 != null){
                        	   Log.d("b1", "if true: b1 is not null");
                               Node friendNode;
                               NamedNodeMap attributes;
                               friendNode = b1.getFirstChild().getFirstChild();
                               if(friendNode != null){
                                   Friend friend;
                                   do{
                                       friend = new Friend();
                                       
                                       attributes = friendNode.getAttributes(); 
                                       friend.setUsername(attributes.getNamedItem("username").getNodeValue());
                                       friend.setPhoneNumber(attributes.getNamedItem("phoneNumber").getNodeValue());
                                       friend.setName(attributes.getNamedItem("name").getNodeValue());
                                	   friendList.add(friend);
                                   }
                                   while((friendNode = friendNode.getNextSibling()) != null);
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
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("getFriends", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   
                   	Toast.makeText(context, "Connection error.", 
           					Toast.LENGTH_LONG).show();
               }

                @Override
                public void onFinish(){
                    Log.d("getUser", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to DeleteFriend class in order to delete a friend
     * @param context
     * 		Context of the Activity 
     * @param list
     * 		LinearLayout variable, contains all the LinearLayouts of the requests
     * @param request
     * 		LinearLayout variable, contains the information of the request
     * @param sendUsername
     * 		The friend's username that sent the request
     * @param userName
     * 		Username of the application
     * 
     */
    public void deleteFriend(Context context, LinearLayout list, LinearLayout request, String sendUsername, String username){
    	Log.d("Client", "deleteFriend: context = " + context + "list = " + list +
    			"\nrequest = " + request + "\nsendUsername = " + sendUsername + 
    			"\nusername = " + username);
    	
    	new DeleteFriend(context, list, request, sendUsername, username).execute();
    }
    
    /**
     * 
     * @Class: DeleteFriend
     * @Description
     * 		Class that makes a GET call to the server to delete a friend
     * 		Remove the LinearLayout of the request from the parent LinearLayout
     *
     */
    private class DeleteFriend extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * The username of the deleted friend 
         */
        private String sendUsername;
        
        /*
         * String variable for the username of the application
         */
        private String username;
        
        /*
         * Status of the result of the call to the server
         */
        private int status;
        
        /*
         * LinearLayout that contains the list of friends (a list of LinearLayouts)
         */
        private LinearLayout list;
        
        /*
         * LinearLayout that contains the name of the friend and the delete button
         */
        private LinearLayout request;
        
        public DeleteFriend(Context context, LinearLayout list, LinearLayout request, String sendUsername, String username) {
            this.context = context;
        	this.sendUsername = sendUsername;
            this.username = username;
            this.list = list;
            this.request = request;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("DeleteFriend", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            if(status == 200){
            	Toast.makeText(context, "Friend Accepted", Toast.LENGTH_LONG).show();
            	
            	list.removeView(request);
            }
            else{
            	if(status == 304){
            		Toast.makeText(context, "Not modified)", 
          					Toast.LENGTH_LONG).show();
            	}
            	else{
            		Toast.makeText(context, "Connection error.", 
          					Toast.LENGTH_LONG).show();
            	}
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("DeleteFriend", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("DeleteFriend", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
            String requestURL = URL + "friends/" + username + "/deletefriend/" + sendUsername;
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			304 - Not modified (already a friend/request already sent/username does not exist)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("DeleteFriend", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                   //view.setEnabled(false);
                   status = statusCode;
                   //Toast.makeText(context, "Request Send", Toast.LENGTH_LONG).show();
                  
               }
               
               @Override
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("DeleteFriend", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("DeleteFriend", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   status = statusCode;
                   if(statusCode == 304){
                	   
                	   //Toast.makeText(context, "Not modified (already a friend/request already sent/username does not exist)", 
              					//Toast.LENGTH_LONG).show();
                   }
                   else{
                	   //Toast.makeText(context, "Connection error.", 
              					//Toast.LENGTH_LONG).show();
                   } 	
               }

                @Override
                public void onFinish(){
                    Log.d("DeleteFriend", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to GetListFriend class in order to obtain a list of all the user's friends
     * @param context
     * 		Context of the Activity 
     * @param userName
     * 		Username of the application
     * @param infalter
     * 		LayoutInflater variable - will add elements to the current layout
     * @param googleMap
     * 		GoogleMap variable to add the markers of the locations or checkins of the friends
     * @param fragmentMap
     * 		Fragment of the map
     */
    public void getListFriend(Context context, String userName, LayoutInflater inflater, GoogleMap map, 
    						Fragment fragmentMap){
    	Log.d("Cient", "getListFriends: context = " + context + "\nusername = " + userName + 
    			"\ninflater = " + inflater + "\nmap = " + map + "\nfragmentMap = " + fragmentMap);
    	new GetListFriend(context, userName, inflater, map, fragmentMap).execute();
    }
    
    /**
     * 
     * @Class: GetListFriend
     * @Description
     * 		Class that makes a GET call to the server to obtain a list of user's friends
     * 		Create a Layout with the results
     *
     */
    private class GetListFriend extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * LayoutInflater to create a new Layout within the code
         */
        private LayoutInflater inflater;
        
        /*
         * This object will be used to create the content of the list of friends in a new layout
         */
        FriendAdapter adapter;
        
        /*
         * GoogleMap variable to draw markers on the map
         */
        GoogleMap map;
        
        /*
         * The fragment of the application were the map is contained
         */
        Fragment fragmentMap;
        
        private ArrayList<Friend> friendList = new ArrayList<Friend>();
        
        
        /*
         * String variable for the username of the application
         */
        private String userName;
        
        public GetListFriend(Context context, String userName, LayoutInflater inflater, GoogleMap map, Fragment fragmentMap) {
            this.context = context;
            this.userName = userName;
            this.inflater = inflater;
            this.map = map;
            this.fragmentMap = fragmentMap;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("GetListFriend", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            adapter = new FriendAdapter(context, R.layout.friend_list_item, friendList, 
            		inflater, userName, map, fragmentMap);
    		ListView contactList = (ListView) ((ActionBarActivity) context).findViewById(R.id.listFriends);
    		contactList.setAdapter(adapter);
    		
            for(int i = 0; i < friendList.size(); i++){
            	Log.d("FriendName", friendList.get(i).getName());
            	Log.d("PhoneNumber", friendList.get(i).getPhoneNumber());
            	Log.d("Username", friendList.get(i).getUsername());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetListFriend", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("GetListFriend", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
        	/**
        	 * 
        	 */
            String requestURL = URL + "friends/" + userName + "/friends";
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			    - Not Content (username invalid?)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("getFriends", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
            	   String xmlData = new String(responseBody);
                   if(xmlData != ""){
                       Log.d("xmlData", "if true: xmlData = " + xmlData);
                                 	   
                    	   Document b1 = getXML(xmlData);
                    	   
                           if(b1 != null){
                        	   Log.d("b1", "if true: b1 is not null");
                               Node friendNode;
                               NamedNodeMap attributes;
                               friendNode = b1.getFirstChild().getFirstChild();
                               if(friendNode != null){
                                   Friend friend;
                                   do{
                                       friend = new Friend();
                                       
                                       attributes = friendNode.getAttributes(); 
                                       friend.setUsername(attributes.getNamedItem("username").getNodeValue());
                                       friend.setPhoneNumber(attributes.getNamedItem("phoneNumber").getNodeValue());
                                       friend.setName(attributes.getNamedItem("name").getNodeValue());
                                	   friendList.add(friend);
                                   }
                                   while((friendNode = friendNode.getNextSibling()) != null);
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
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("getFriends", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   
                   	Toast.makeText(context, "Connection error.", 
           					Toast.LENGTH_LONG).show();
               }

                @Override
                public void onFinish(){
                    Log.d("getUser", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to GetFriend class in order to obtain a list of all possible friends
     * @param context
     * 		Context of the Activity 
     * @param userName
     * 		Username of the application
     * @param contact
     * 		ArraList of ContactItem that contains all the contacts in the Contact list
     * @param inflater
     * 		LayoutInflater variable - will add elements to the current layout
     * 
     */
    public void getFriends(Context context, String userName, ArrayList<ContactItem> contact, 
    		LayoutInflater inflater){
    	Log.d("Client", "getListFriends: context = " + context +
    			"\ncontact = " + contact + "\ninflater = " + inflater);
    	
    	new GetFriend(context, userName, contact, inflater).execute();
    }
    
    /**
     * 
     * @Class: GetFriend
     * @Description
     * 		Class that makes three call to the server to obtain:
     * 			- A list of user's friends (GET)
     * 			- A list of potential friends (POST)
     * 			- A list of pending request from other users (GET)
     * 		Create a Layout with the results:
     * 			- If the user is already a friend of the app, remove it
     * 			- If the user has already made a request or vice versa, it cannot make another request
     *
     */
    private class GetFriend extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * LayoutInflater to create a new Layout within the code
         */
        private LayoutInflater inflater;
        
        /*
         * This object will be used to create the content of the potential friends in a new layout
         */
        ContactAdapter adapter;
        
        /*
         * ArrayList that will contain the list of friends
         */
        private ArrayList<Friend> friendList = new ArrayList<Friend>();
        
        /*
         * ArrayList that contains the contacts on the user's contact list
         */
        private ArrayList<ContactItem> contact = new ArrayList<ContactItem>();
        
        /*
         * ArrayList that will contain the list of potential friends
         */
        private ArrayList<ContactItem> potentialFriend = new ArrayList<ContactItem>();
        
        /*
         * ArrayList that will contain a list of pending request from the user, to disable 
         * 		the buttons to send the request
         */
        private ArrayList<String> pendingSendRequest = new ArrayList<String>();
        
        /*
         * String variable for the username of the application
         */
        private String userName;
        
        public GetFriend(Context context, String userName, ArrayList<ContactItem> contact, 
        		LayoutInflater inflater) {
            this.context = context;
            this.userName = userName;
            this.contact = contact;
            this.inflater = inflater;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("GetFriends", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            for(int i = 0;i < friendList.size(); i++){
            	for(int j = 0; j < potentialFriend.size(); j++){
            		if(potentialFriend.get(j).getUserName()
            				.compareTo(friendList.get(i).getUsername()) == 0){
            			potentialFriend.remove(j);
            			break;
            		}
            		if(potentialFriend.get(j).getUserName()
            				.compareTo(userName) == 0){
            			potentialFriend.remove(j);
            			break;
            		}
            	}
            }
            
            adapter = new ContactAdapter(context, R.layout.contact_list_item, potentialFriend, 
            		inflater, userName, pendingSendRequest);
    		ListView contactList = (ListView) ((ActionBarActivity) context).findViewById(R.id.listContacts);
    		contactList.setAdapter(adapter);
            
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetFriends", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("GetFriends", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
        	/**
        	 * 
        	 */
            String requestURL = URL + "friends/" + userName + "/friends";
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			    - Not Content (username invalid?)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("getFriends", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
            	   String xmlData = new String(responseBody);
                   if(xmlData != ""){
                       Log.d("xmlData", "if true: xmlData = " + xmlData);
                                 	   
                    	   Document b1 = getXML(xmlData);
                    	   
                           if(b1 != null){
                        	   Log.d("b1", "if true: b1 is not null");
                               Node friendNode;
                               NamedNodeMap attributes;
                               friendNode = b1.getFirstChild().getFirstChild();
                               if(friendNode != null){
                                   Friend friend;
                                   do{
                                       friend = new Friend();
                                       
                                       attributes = friendNode.getAttributes(); 
                                       friend.setUsername(attributes.getNamedItem("username").getNodeValue());
                                       friend.setPhoneNumber(attributes.getNamedItem("phoneNumber").getNodeValue());
                                       friend.setName(attributes.getNamedItem("name").getNodeValue());
                                	   friendList.add(friend);
                                   }
                                   while((friendNode = friendNode.getNextSibling()) != null);
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
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("getFriends", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   
                   	Toast.makeText(context, "Connection error.", 
           					Toast.LENGTH_LONG).show();
               }

                @Override
                public void onFinish(){
                    Log.d("getUser", "onFinish");
                }
            });
            /**
             * 
             */
            requestURL = URL + "/friends/" + userName + "/potentialfriends";
        	JSONArray jsonArray = new JSONArray();
        	JSONObject jsonParams;
            try{
            	for(int i = 0;i < contact.size(); i++){
            		jsonParams = new JSONObject();
            		Log.d("Contact", contact.get(i).getPhoneNumber());
            		jsonParams.put("@number",contact.get(i).getPhoneNumber());
                    jsonParams.put("@username", "");
                    jsonArray.put(jsonParams);
            	}
                

                StringEntity entity = new StringEntity(jsonArray.toString());
                clientHTTP.post(context, requestURL,entity, HEADER, new AsyncHttpResponseHandler(){
                	
                	/**
                	 * onSuccess
                	 * 		int statusCode
                	 * 			200 - Login Correct
                	 * 			401 - Unauthorized (Either username or password are incorrect)
                	 */
                	 @Override
                     public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                        Log.d("GetFriends", "onSuccess: statusCode = " + statusCode +
                        		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                    	
                        String xmlData = new String(responseBody);
                        if(xmlData != ""){
                            Log.d("xmlData", "if true: xmlData = " + xmlData);
                                      	   
                         	   Document b1 = getXML(xmlData);
                         	   
                                if(b1 != null){
                             	   Log.d("b1", "if true: b1 is not null");
                                    Node potentialNode;
                                    NamedNodeMap attributes;
                                    potentialNode = b1.getFirstChild().getFirstChild();
                                    if(potentialNode != null){
                                        ContactItem potential;
                                        do{
                                            potential = new ContactItem();
                                            
                                            attributes = potentialNode.getAttributes(); 
                                            potential.setUserName(attributes.getNamedItem("username").getNodeValue());
                                            potential.setPhoneNumber(attributes.getNamedItem("number").getNodeValue());
                                            //friend.setName(attributes.getNamedItem("name").getNodeValue());
                                            Log.d("Test", potential.getUserName() + " " + potential.getPhoneNumber());
                                     	   	potentialFriend.add(potential);
                                        }
                                        while((potentialNode = potentialNode.getNextSibling()) != null);
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
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                        Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                        		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                        		"\nerror = " + error);
                    	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
            					Toast.LENGTH_LONG).show();
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
	        catch(NetworkOnMainThreadException e){
	        	
	        } 	
            /**
             * 
             */
            requestURL = URL + "friends/" + userName + "/pendingfriends";
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			    - Not Content (username invalid?)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("getFriends", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
            	   String xmlData = new String(responseBody);
                   if(xmlData != ""){
                       Log.d("xmlData", "if true: xmlData = " + xmlData);
                                 	   
                    	   Document b1 = getXML(xmlData);
                    	   
                           if(b1 != null){
                        	   Log.d("b1", "if true: b1 is not null");
                               Node pendingSendNode;
                               NamedNodeMap attributes;
                               pendingSendNode = b1.getFirstChild().getFirstChild();
                               if(pendingSendNode != null){
                                   String user;
                                   do{
                                       user  = "";
                                       attributes = pendingSendNode.getAttributes();
                                       Log.d("Username", userName);
                                       if(attributes.getNamedItem("initUsername").getNodeValue().compareTo(userName) == 0){
                                    	   Log.d("PendingFriend","if true: send by user");
                                    	   
                                    	   user = attributes.getNamedItem("recpUsername").getNodeValue();
                                       }
                                       else{
                                    	   Log.d("PendingFriend","if false: send by another user");
                                    	   
                                    	   user = attributes.getNamedItem("initUsername").getNodeValue();
                                       }
                                	   pendingSendRequest.add(user);
                                   }
                                   while((pendingSendNode = pendingSendNode.getNextSibling()) != null);
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
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("getFriends", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   
                   	Toast.makeText(context, "Connection error.", 
           					Toast.LENGTH_LONG).show();
               }

                @Override
                public void onFinish(){
                    Log.d("getUser", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to GetRequest class in order to obtain a list of all requests made to the user
     * @param context
     * 		Context of the Activity 
     * @param userName
     * 		Username of the application
     * @param inflater
     * 		LayoutInflater variable - will add elements to the current layout
     * 
     */
    public void getRequest(Context context, String userName, LayoutInflater inflater){
    	Log.d("Cient", "getRequest: context = " + context +
    			"\nuserName = " + userName + "\ninflater = " + inflater);
    	
    	new GetRequest(context, userName, inflater).execute();
    }
    
    /**
     * 
     * @Class: GetRequest
     * @Description
     * 		Class that makes a GET call to the server to obtain a list of requests made to the user
     * 		Create a Layout with the results
     *
     */
    private class GetRequest extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * LayoutInflater to create a new Layout within the code
         */
        private LayoutInflater inflater;
        
        /*
         * This object will be used to create the content of the list of request made to the user in a new layout
         */
        RequestAdapter adapter;

        /*
         * ArrayList of RequestItem objects that will contain the information of all the requests
         * 	made to the user
         */
        private ArrayList<RequestItem> contactRequested = new ArrayList<RequestItem>();
        
        /*
         * String variable for the username of the application
         */
        private String userName;
        
        public GetRequest(Context context, String userName, LayoutInflater inflater) {
            this.context = context;
            this.userName = userName;
            this.inflater = inflater;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("GetRequest", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            adapter = new RequestAdapter(context, R.layout.request_list_item, contactRequested, 
            		inflater, userName);
    		ListView requestList = (ListView) ((ActionBarActivity) context).findViewById(R.id.listRequest);
    		requestList.setAdapter(adapter);
            
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetRequest", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("GetRequest", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
            String requestURL = URL + "friends/" + userName + "/pendingfriends";
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			    - Not Content (username invalid?)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("getRequest", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
            	   String xmlData = new String(responseBody);
                   if(xmlData != ""){
                       Log.d("xmlData", "if true: xmlData = " + xmlData);
                                 	   
                    	   Document b1 = getXML(xmlData);
                    	   
                           if(b1 != null){
                        	   Log.d("b1", "if true: b1 is not null");
                               Node pendingSendNode;
                               NamedNodeMap attributes;
                               pendingSendNode = b1.getFirstChild().getFirstChild();
                               if(pendingSendNode != null){
                                   RequestItem user;
                                   do{
                                       user  = new RequestItem();
                                       attributes = pendingSendNode.getAttributes();
                                       Log.d("Username", userName);
                                       if(attributes.getNamedItem("recpUsername").getNodeValue().compareTo(userName) == 0){
                                    	   Log.d("PendingFriend","if true: send to user");
                                    	   
                                    	   user.setUserName(attributes.getNamedItem("initUsername").getNodeValue());
                                    	   contactRequested.add(user);
                                       }                                      
                                   }
                                   while((pendingSendNode = pendingSendNode.getNextSibling()) != null);
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
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("getFriends", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("LoginResponse", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   
                   	Toast.makeText(context, "Connection error.", 
           					Toast.LENGTH_LONG).show();
               }

                @Override
                public void onFinish(){
                    Log.d("getUser", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to SendFriendRequest class in order to send a friend request
     * @param context
     * 		Context of the Activity 
     * @param v
     * 		Button view for sending a request
     * @param sendUsername
     * 		The friend's username to send the request
     * @param userName
     * 		Username of the application
     * @param inflater
     * 		LayoutInflater variable - will add elements to the current layout
     * 
     */
    public void sendFriendRequest(Context context, View v, String sendUsername, String username){
    	Log.d("Client", "sendFriendRequest: context = " + context + "v = " + v +
    			"\nsendUsername = " + sendUsername + "\nusername = " + username);
    	
    	new SendFriendRequest(context, v, sendUsername, username).execute();
    }
    
    /**
     * 
     * @Class: SendFriendRequest
     * @Description
     * 		Class that makes a GET call to the server to send a friend's request
     * 		Disable the "Send Request" button after using it
     *
     */
    private class SendFriendRequest extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * View of the button, for disabling the button after sending a request
         */
        private View view;
        
        /*
         * The username of the potential friend that the request was made to 
         */
        private String sendUsername;
        
        /*
         * String variable for the username of the application
         */
        private String username;
        
        /*
         * Status of the result of the call to the server
         */
        private int status;
        
        public SendFriendRequest(Context context, View view, String sendUsername, String username) {
            this.context = context;
        	this.view = view;
        	this.sendUsername = sendUsername;
            this.username = username;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("SendFriendRequest", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            if(status == 200){
            	Toast.makeText(context, "Request Send", Toast.LENGTH_LONG).show();
            	view.setEnabled(false);
            }
            else{
            	if(status == 304){
            		Toast.makeText(context, "Not modified (already a friend/request already sent/username does not exist)", 
          					Toast.LENGTH_LONG).show();
            	}
            	else{
            		Toast.makeText(context, "Connection error.", 
          					Toast.LENGTH_LONG).show();
            	}
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("SendFriendRequest", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("SendFriendRequest", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
            String requestURL = URL + "friends/" + username + "/requestfriend/" + sendUsername;
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			304 - Not modified (already a friend/request already sent/username does not exist)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("sendFriendRequest", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                   //view.setEnabled(false);
                   status = statusCode;
                   //Toast.makeText(context, "Request Send", Toast.LENGTH_LONG).show();
                  
               }
               
               @Override
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("sendFriendRequest", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("sendFriendRequest", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   status = statusCode;
                   if(statusCode == 304){
                	   
                	   //Toast.makeText(context, "Not modified (already a friend/request already sent/username does not exist)", 
              					//Toast.LENGTH_LONG).show();
                   }
                   else{
                	   //Toast.makeText(context, "Connection error.", 
              					//Toast.LENGTH_LONG).show();
                   } 	
               }

                @Override
                public void onFinish(){
                    Log.d("getUser", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to AcceptFriend class in order to accept a friend's request
     * @param context
     * 		Context of the Activity 
     * @param list
     * 		LinearLayout variable, contains all the LinearLayouts of the requests
     * @param request
     * 		LinearLayout variable, contains the information of the request
     * @param sendUsername
     * 		The friend's username that sent the request
     * @param userName
     * 		Username of the application
     * 
     */
    public void acceptRequest(Context context, LinearLayout list, LinearLayout request, String sendUsername, String username){
    	Log.d("Client", "acceptRequest: context = " + context + "list = " + list +
    			"\nrequest = " + request + "\nsendUsername = " + sendUsername + 
    			"\nusername = " + username);
    	
    	new AcceptFriend(context, list, request, sendUsername, username).execute();
    }
    
    /**
     * 
     * @Class: AcceptFriend
     * @Description
     * 		Class that makes a GET call to the server to accept a friend's request
     * 		Remove the LinearLayout of the request from the parent LinearLayout
     *
     */
    private class AcceptFriend extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * The username of the friend which request was accepted 
         */
        private String sendUsername;
        
        /*
         * String variable for the username of the application
         */
        private String username;
        
        /*
         * Status of the result of the call to the server
         */
        private int status;
        
        /*
         * LinearLayout that contains the list of request (a list of LinearLayouts)
         */
        private LinearLayout list;
        
        /*
         * LinearLayout that contains the name of the user that made the request 
         * 	and the accept or reject button
         */
        private LinearLayout request;
        
        public AcceptFriend(Context context, LinearLayout list, LinearLayout request, String sendUsername, String username) {
            this.context = context;
        	this.sendUsername = sendUsername;
            this.username = username;
            this.list = list;
            this.request = request;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("AcceptFriend", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            if(status == 200){
            	Toast.makeText(context, "Friend Accepted", Toast.LENGTH_LONG).show();
            	
            	list.removeView(request);
            }
            else{
            	if(status == 304){
            		Toast.makeText(context, "Not modified)", 
          					Toast.LENGTH_LONG).show();
            	}
            	else{
            		Toast.makeText(context, "Connection error.", 
          					Toast.LENGTH_LONG).show();
            	}
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("AcceptFriend", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("AcceptFriend", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
            String requestURL = URL + "friends/" + username + "/acceptfriend/" + sendUsername;
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			304 - Not modified (already a friend/request already sent/username does not exist)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("AcceptFriend", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                   //view.setEnabled(false);
                   status = statusCode;
                   //Toast.makeText(context, "Request Send", Toast.LENGTH_LONG).show();
                  
               }
               
               @Override
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("AcceptFriend", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("AcceptFriend", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   status = statusCode;
                   if(statusCode == 304){
                	   
                	   //Toast.makeText(context, "Not modified (already a friend/request already sent/username does not exist)", 
              					//Toast.LENGTH_LONG).show();
                   }
                   else{
                	   //Toast.makeText(context, "Connection error.", 
              					//Toast.LENGTH_LONG).show();
                   } 	
               }

                @Override
                public void onFinish(){
                    Log.d("AcceptFriend", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to RejectFriend class in order to reject a friend's request
     * @param context
     * 		Context of the Activity 
     * @param list
     * 		LinearLayout variable, contains all the LinearLayouts of the requests
     * @param request
     * 		LinearLayout variable, contains the information of the request
     * @param sendUsername
     * 		The friend's username that sent the request
     * @param userName
     * 		Username of the application
     * 
     */
    public void rejectRequest(Context context, LinearLayout list, LinearLayout request, String sendUsername, String username){
    	Log.d("Client", "rejectRequest: context = " + context + "list = " + list +
    			"\nrequest = " + request + "\nsendUsername = " + sendUsername + 
    			"\nusername = " + username);
    	
    	new RejectFriend(context, list, request, sendUsername, username).execute();
    }
    
    /**
     * 
     * @Class: RejectFriend
     * @Description
     * 		Class that makes a GET call to the server to reject a friend's request
     * 		Remove the LinearLayout of the request from the parent LinearLayout
     *
     */
    private class RejectFriend extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * The username of the friend which request was rejected 
         */
        private String sendUsername;
        
        /*
         * String variable for the username of the application
         */
        private String username;
        
        /*
         * Status of the result of the call to the server
         */
        private int status;
        
        /*
         * LinearLayout that contains the list of requests (a list of LinearLayouts)
         */
        private LinearLayout list;
        
        /*
         * LinearLayout that contains the name of the user that made the request 
         * 	and the accept or reject button
         */
        private LinearLayout request;
        
        public RejectFriend(Context context, LinearLayout list, LinearLayout request, String sendUsername, String username) {
            this.context = context;
        	this.sendUsername = sendUsername;
            this.username = username;
            this.list = list;
            this.request = request;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("RejectFriend", "onPostExecute");
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            if(status == 200){
            	Toast.makeText(context, "Request Rejected", Toast.LENGTH_LONG).show();
            	
            	list.removeView(request);
            }
            else{
            	if(status == 304){
            		Toast.makeText(context, "Not modified)", 
          					Toast.LENGTH_LONG).show();
            	}
            	else{
            		Toast.makeText(context, "Connection error.", 
          					Toast.LENGTH_LONG).show();
            	}
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RejectFriend", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("RejectFriend", "doInBackground");
        	
        	SyncHttpClient clientHTTP = new SyncHttpClient();
        	
            String requestURL = URL + "friends/" + username + "/declinefriend/" + sendUsername;
            
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
            	
            	/**
            	 * onSuccess
            	 * 		int statusCode
            	 * 			200 - Data received
            	 * 			304 - Not modified (already a friend/request already sent/username does not exist)
            	 */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   Log.d("RejectFriend", "onSuccess: statusCode = " + statusCode +
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody);
                   //view.setEnabled(false);
                   status = statusCode;
                   //Toast.makeText(context, "Request Send", Toast.LENGTH_LONG).show();
                  
               }
               
               @Override
               public void onProgress(int bytesWritten, int totalSize) { 
               	Log.d("RejectFriend", "onProgress");
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
                   Log.d("RejectFriend", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
                   status = statusCode;
                   if(statusCode == 304){
                	   
                	   //Toast.makeText(context, "Not modified (already a friend/request already sent/username does not exist)", 
              					//Toast.LENGTH_LONG).show();
                   }
                   else{
                	   //Toast.makeText(context, "Connection error.", 
              					//Toast.LENGTH_LONG).show();
                   } 	
               }

                @Override
                public void onFinish(){
                    Log.d("RejectFriend", "onFinish");
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to FriendLocation class in order to get the last location of a friend from the server
     * @param context
     * 		Context of the Activity 
     * @param friendUsername
     * 		The friend's username that sent the request
     * @param userName
     * 		Username of the application
     * @param googleMap
     * 		GoogleMap variable to add the markers of the locations of the friend
     * @param fragmentMap
     * 		Fragment of the map
     * 
     */
    public void getFriendLocation(Context context, String friendUsername, String username, GoogleMap map,
    								Fragment fragmentMap){
    	Log.d("Client", "getFriendLocation: context = " + context + "\nfriendUsername = " + friendUsername + 
    			"\nusername = " + username + "\nmap = " + map + "\nfragmentMap = " + fragmentMap);
    	new FriendLocation(context, friendUsername, username, map, fragmentMap).execute();
    }
    
    /**
     * 
     * @Class: FriendLocation
     * @Description
     * 		Class that makes a GET call to the server to get the last friend's location
     * 		Populate the map with a marker with the friend's location data
     *
     */
    private class FriendLocation extends AsyncTask<String,String,Integer> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * ArrayList that will contain an array of HashMaps that have the user's location information
         */
        public HashMap<String,String> locationList = new HashMap<String, String>();
        
        /*
         * The username of the friend which last location was requested
         */
        private String friendUsername = "";
        
        /*
         * String variable for the username of the application
         */
        private String username = "";
        
        /*
         * GoogleMap variable to draw markers on the map
         */
        private GoogleMap googleMap;
        
        /*
         * The fragment of the application were the map is contained
         */
        private Fragment fragmentMap;
        
        /*
         * Status of the result of the call to the server, if it is successful the value will be true
         */
        private boolean status = true;

        public FriendLocation(Context context, String friendUsername, String username, GoogleMap googleMap,
        						Fragment fragmentMap) {
            this.context = context;
            this.friendUsername = friendUsername;
            this.googleMap = googleMap;
            this.username = username;
            this.fragmentMap = fragmentMap;
        }

        @Override
        protected void onPostExecute(Integer nothing) {
            super.onPostExecute(nothing);
            Log.d("FriendLocation", "onPostExecute");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
   			//for(int i = 0; i < locationList.size(); i++){
            
            if(status){
            	googleMap.clear();
    			MarkerOptions marker = new MarkerOptions()
    						.position(new LatLng(
    											Double.valueOf(locationList.get("latitude")), 
    											Double.valueOf(locationList.get("longitude"))))
    						.title(locationList.get("username"))
    						.snippet(locationList.get("time"))
    						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    			googleMap.addMarker(marker);
    			
    			FragmentManager fragmentManager = ((ActionBarActivity) context).getSupportFragmentManager();
    			fragmentManager.beginTransaction()
    				.replace(R.id.content_frame, fragmentMap)
    				//.addToBackStack(null)
    				.commit();
            }
            else{
            	Toast.makeText(context, "Your friend has not stored any location", 
    					Toast.LENGTH_LONG).show();
            }
   			//}
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("FriendLocation", "onPreExecute");
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override	
        protected Integer doInBackground(String... arg0) {
        	Log.d("FriendLocation", "doInBackground");
        	SyncHttpClient clientHTTP = new SyncHttpClient();
            String requestURL = URL + "friends/" + username + "/friend/" + friendUsername + "/locations";
            clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){

               /**
                * StatusCode
                * 		200 - Data
                * 		204 - No content
                */
               @Override
               public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
                   String xmlData = new String(responseBody);
                   String time = "";
                   boolean first = true;
                   if(xmlData != ""){
                       Log.d("Success", xmlData);
                	   Document b1 = getXML(xmlData);
                       if(b1 != null){
                           Node locationNode;
                           NamedNodeMap attributes;
                           locationNode = b1.getFirstChild().getFirstChild();
                           if(locationNode != null){
                               do{
                                   attributes = locationNode.getAttributes();
                                   if(attributes.getNamedItem("checkIn").getNodeValue().compareTo("false") == 0){
                                	   if(first){
                                		   locationList.put("username",attributes.getNamedItem("appUsername").getNodeValue());
                                		   locationList.put("time", attributes.getNamedItem("dateandtime").getNodeValue());
                                		   locationList.put("longitude",attributes.getNamedItem("longitude").getNodeValue());
                                		   locationList.put("latitude",attributes.getNamedItem("latitude").getNodeValue());
                                           first = false;
                                	   }
                                	   else{
                                		   time = attributes.getNamedItem("dateandtime").getNodeValue();
                                		   if(getMostRecent(locationList.get("time"), time)){
                                			   locationList.put("username",attributes.getNamedItem("appUsername").getNodeValue());
                                			   locationList.put("time", attributes.getNamedItem("dateandtime").getNodeValue());
                                			   locationList.put("longitude",attributes.getNamedItem("longitude").getNodeValue());
                                			   locationList.put("latitude",attributes.getNamedItem("latitude").getNodeValue());
                                		   }
                                	   }
                                   }
                               }
                               while((locationNode = locationNode.getNextSibling()) != null);
                           }
                           else{
                        	   Log.d("locationNode", "if false: there are is location");
                        	   
                        	   status = false;
                           }
                           //obtainData(locationList);
                           Log.d("Results", "Test");
                       }
                       else{
                           Log.d("Error","Error with the XML");
                       }

                   }
                   else{
                       Log.d("Error","Response without data");
                   }
               }
               
               @Override
               public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
               	Log.d("FriendLocation", "onFailure: statusCode = " + statusCode + 
                   		"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
                   		"\nerror = " + error);
               	if(statusCode == 304){
                   	Toast.makeText(context, "Username is not valid", 
           					Toast.LENGTH_LONG).show();
                   }
                   else{
                   	Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
           					Toast.LENGTH_LONG).show();
                   }
               }

                @Override
                public void onFinish(){
                    //obtainData(locationList);
                }
            });
            return Integer.valueOf(1);
        }
        
    }
    
    /**
     * 		Call to FriendCheckin class in order to get the checkins of a friend from the server
     * @param context
     * 		Context of the Activity 
     * @param friendUsername
     * 		The friend's username that sent the request
     * @param userName
     * 		Username of the application
     * @param googleMap
     * 		GoogleMap variable to add the markers of the locations of the friend
     * @param fragmentMap
     * 		Fragment of the map
     * 
     */
    public void getFriendCheckin(Context context, String friendUsername, String username, GoogleMap map,
			Fragment fragmentMap){
		Log.d("Client", "getFriendCheckin: context = " + context + "\nfriendUsername = " + friendUsername + 
		"\nusername = " + username + "\nmap = " + map + "\nfragmentMap = " + fragmentMap);
		new FriendCheckin(context, friendUsername, username, map, fragmentMap).execute();
	}
		
    /**
     * 
     * @Class: FriendCheckin
     * @Description
     * 		Class that makes a GET call to the server to get friend's checkins
     * 		Populate the map with markers with the friend's checkins data
     *
     */
	private class FriendCheckin extends AsyncTask<String,String,Integer> {
	
		/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
		private ProgressDialog dialog;
		
		/*
         * Context of the activity to draw elements in the layout
         */
		private Context context;
		
		/*
         * ArrayList that will contain an array of HashMaps that have the user's location information
         */
		public ArrayList<HashMap<String,String>> checkinList = new ArrayList<HashMap<String, String>>();
	
		/*
         * The username of the friend which checkins were requested
         */
		private String friendUsername = "";
		
		/*
         * String variable for the username of the application
         */
		private String username = "";
		
		/*
         * GoogleMap variable to draw markers on the map
         */
		private GoogleMap googleMap;
		
		/*
         * The fragment of the application were the map is contained
         */
		private Fragment fragmentMap;
		
		/*
         * Status of the result of the call to the server, if it is successful the value will be true
         */
		private boolean status = true;
	
		public FriendCheckin(Context context, String friendUsername, String username, GoogleMap googleMap,
				Fragment fragmentMap) {
			this.context = context;
			this.friendUsername = friendUsername;
			this.googleMap = googleMap;
			this.username = username;
			this.fragmentMap = fragmentMap;
		}
	
		@Override
		protected void onPostExecute(Integer nothing) {
			super.onPostExecute(nothing);
			Log.d("FriendCheckin", "onPostExecute");
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			if(status){
				googleMap.clear();
				for(int i = 0; i < checkinList.size(); i++){
					
					MarkerOptions marker = new MarkerOptions()
											.position(new LatLng(
															Double.valueOf(checkinList.get(i).get("latitude")), 
															Double.valueOf(checkinList.get(i).get("longitude"))))
											.title(checkinList.get(i).get("username") + "\n" + checkinList.get(i).get("place"))
											.snippet(checkinList.get(i).get("time") + "\n" + checkinList.get(i).get("comment"))											
											.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					googleMap.addMarker(marker);
					
				}
				FragmentManager fragmentManager = ((ActionBarActivity) context).getSupportFragmentManager();
				fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragmentMap)
					//.addToBackStack(null)
					.commit();
			}
			else{
				Toast.makeText(context, "Your friend has not stored any chekins", 
    					Toast.LENGTH_LONG).show();
			}
			
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("FriendCheckin", "onPreExecute");
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setMessage("Loading..");
			dialog.isIndeterminate();
			dialog.show();
		}
		
		@Override	
		protected Integer doInBackground(String... arg0) {
			Log.d("FriendCheckin", "doInBackground");
			SyncHttpClient clientHTTP = new SyncHttpClient();
			String requestURL = URL + "friends/" + username + "/friend/" + friendUsername + "/checkins";
			clientHTTP.get(requestURL, new AsyncHttpResponseHandler(){
			
				/**
				* StatusCode
				* 		200 - Data
				* 		204 - No content
				*/
				@Override
				public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
					String xmlData = new String(responseBody);
					if(xmlData != ""){
						Log.d("Success", xmlData);
						Document b1 = getXML(xmlData);
						if(b1 != null){
						   Node checkinNode;
						   NamedNodeMap attributes;
						   checkinNode = b1.getFirstChild().getFirstChild();
						   if(checkinNode != null){
							   HashMap<String,String> checkin;
						       do{
						    	   checkin = new HashMap<String,String>();
						           attributes = checkinNode.getAttributes();
						           checkin.put("username",attributes.getNamedItem("appUsername").getNodeValue());
						           checkin.put("time", attributes.getNamedItem("dateandtime").getNodeValue());
						           checkin.put("longitude",attributes.getNamedItem("longitude").getNodeValue());
						           checkin.put("latitude",attributes.getNamedItem("latitude").getNodeValue());
						           checkin.put("comment", attributes.getNamedItem("comment").getNodeValue());
						           checkin.put("place", attributes.getNamedItem("place").getNodeValue());
						           checkinList.add(checkin);
						       }
						       while((checkinNode = checkinNode.getNextSibling()) != null);
						   	}
						   else{
							   Log.d("locationNode", "if false: there are is location");
	                    	   
	                    	   status = false;
						   }
						   	Log.d("Results", "Test");
						}
						else{ 
						  
						}
					
					}
					else{
						Log.d("Error","Response without data");
					}
				}
			
				@Override
				public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, java.lang.Throwable error){
					Log.d("FriendCheckin", "onFailure: statusCode = " + statusCode + 
						"\nheaders = " + headers + "\nresponseBody = " + responseBody + 
						"\nerror = " + error);
					if(statusCode == 304){
					Toast.makeText(context, "Username is not valid", 
							Toast.LENGTH_LONG).show();
					}
					else{
					Toast.makeText(context, "Connection unsuccesful.\nVerify your internet connection", 
							Toast.LENGTH_LONG).show();
					}
				}
			
				@Override
				public void onFinish(){
					//obtainData(locationList);
				}
			});
			return Integer.valueOf(1);
		}
	
	}
    
    /**
     * End Friend Methods
     * ************************************************************************************
     */
    
    /**
     * ************************************************************************************
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
     */
    private void goToMap(Context context){
    	Intent map = new Intent(context, MapActivity.class);
        map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(map);
        ((ActionBarActivity) context).finish();
    }
   
     /**
     * @Function: getXML
     * @Access:  public
     * @Return: Document
     * @Description
     * 		Code that get the response from the server (XML in a String) and create a Document to navigate
     * 			through the nodes
     * @param responseBody
     * 			String, XML response 
     * @return 
     * 			Document object that will be used to navigate through the nodes
     */
    public Document getXML(String responseBody){
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
     * @Function: getMostRecent
     * @Access:  public
     * @Return: boolean
     * @Description
     * 		Compare two dates and return true if the second date is more recent
     * @param date1
     * 			String, date one
     * @param date2
     *  		String, date two
     * @return 
     * 			boolean, if the second date is more recent it will return true, otherwise it will return false
     * 
     */
    public boolean getMostRecent(String date1, String date2){
    	Log.d("Client", "getMostRecent: date1 = " + date1 + "\ndate2 = " + date2);
    	boolean result = false;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sss'Z'");
    	Calendar cal1 = Calendar.getInstance();
    	Calendar cal2 = Calendar.getInstance();
    	try {
			cal1.setTime(dateFormat.parse(date1));
			cal2.setTime(dateFormat.parse(date2));
			if(cal2.compareTo(cal1) == 1){
				result = true;
			}
		} catch (ParseException e) {
			Log.d("Exception", "getMostRecent: ParseException");
			e.printStackTrace();
		}
    	
    	return result;	
    }

    /**
     * End Other Methods
     * *****************************************************************************************
     */
    
}
