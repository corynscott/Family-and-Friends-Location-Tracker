package map.teamc.com.maplocationproject;

import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;


/**
 * 		ActionBarActivity, this is the main activity, it is divided in content_frame and a 
 * 			menu list.
 * 			The content_frame will change depending of the action required.
 *
 */
@SuppressWarnings("deprecation")
public class MapActivity extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener{

    /*
     * LocationRequest,this initialize the location 
     */
    private LocationRequest mlRequest;
    
    /*
     * Object that contains methods to obtain the location information
     */
    private LocationClient mlClient;
    
    /*
     * A boolean value that allows to start or stop the periodic updates
     */
    boolean updateRequested;
    
    /*
     * Variable that will contain the location information
     */
    private Location location;

    /*
	 * SharedPreferences object that will get information stored in the application
	 */
    SharedPreferences pref;
    
    /*
	 * Editor object that will store information in the application in SharedPreferences
	 */
    SharedPreferences.Editor editor;
    
    /*
	 * SharedPreferences object that will get information stored in the application
	 */
    SharedPreferences preferences;
    
    /*
     * String that contains the name of the application SharedPreferences
     */
    public static final String MyPREFERENCES = "MyPrefs";
   
    /*
     * This will store information on a UserLocation object to store the location information and
     * 	send it to the server
     */
    private UserLocation userLocation = null;
    
    /*
	 * The object of the client makes the calls to the server
	 */
    private Client client = new Client();
    

    /*
     * Side menu variable
     */
    ListView menuList;  
    
    /*
     * DrawerLayout, the main element of the Activity
     */
    private DrawerLayout NavDrawerLayout;
    
    /*
     * The Action of showing or closing the side menu
     */
	private ActionBarDrawerToggle mDrawerToggle;
    
	/*
	 * Fragment that will contain the map
	 */
	private MapView fragmentMap = null;
	
	/*
	 * Item selected from the side menu
	 */
	private int selectedItem;
	
	/*
	 * Item previously selected in the side menu
	 */
	private int previousSelected = 0;
	
	/*
	 * Check whether the map has been displayed in the fragment or not
	 */
	private boolean mainMap = true;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("MapActivity", "onCreate: savedInstanceState = " + savedInstanceState);
        
        setContentView(R.layout.map);
        
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        
        menuList = (ListView) findViewById(R.id.MenuList);
        
        //menuList.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,Titles));
        MyAdapter adapter = new MyAdapter(this, generateData());
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, NavDrawerLayout,
                R.drawable.ic_drawer, R.string.hello_world,R.string.app_name){

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);

                Log.d("onCreate", "onDrawerClosed: view = " + view);
                
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                
                Log.d("onCreate", "onDrawerOpened: drawerView = " + drawerView);
                
                supportInvalidateOptionsMenu();
            }
        };
        
        NavDrawerLayout.setDrawerListener(mDrawerToggle);

        setLocationAttributes();
        
        selectedItem = -1;
        
        selectItem(0);

    }
    
	/*
	 * Titles and options that will be displayed in the side menu
	 */
    String [] Titles = new String[]{"Map", "Show Locations",  //0, 1
			"CheckIn", "New CheckIn", "History CheckIn", //2, 3, 4 
			"Friends", "New Friends", "View Request", "View Friends", "Delete Friends", //5, 6, 7, 8, 9
			"Logout"}; //10
    
    private ArrayList<Model> generateData(){
        ArrayList<Model> models = new ArrayList<Model>();
        models.add(new Model("Map", "0"));
        models.add(new Model("Show Locations", "1"));
        models.add(new Model("Checkin"));//2
        models.add(new Model("New Checkin","3"));
        models.add(new Model("Checkin History","4"));
        models.add(new Model("Friends"));//5
        models.add(new Model("New Friends","6"));
        models.add(new Model("Requests","7"));
        models.add(new Model("Friends' List","8"));
        models.add(new Model("Delete Friends","9"));
        models.add(new Model(""));//10
        models.add(new Model("Logout","11"));
 
        return models;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        
        Log.d("MapActivity", "onPostCreate: savedInstanceState = " + savedInstanceState);
        
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        
        Log.d("MapActivity", "onConfigurationChanged: newConfig = " + newConfig);
        
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
    	Log.d("MapActivity", "onPrepareOptionsMenu: menu = " + menu);
    	
    	NavDrawerLayout.isDrawerOpen(menuList);
        
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.d("MapActivity", "OnCreateOptionsMenu: menu = " + menu);
    	
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        restoreActionBar();
        
        return true;
    }
    
    /**
     * 		Code to add the ActionBar
     */
    public void restoreActionBar() {
    	Log.d("MapActivity", "restoreActionBar");
    	
		ActionBar actionBar = getSupportActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		actionBar.setDisplayShowTitleEnabled(true);
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		actionBar.setTitle("FamFriLocator");
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	Log.d("MapActivity", "onOptionsItemSelected: item = " + item);
    	
        int id = item.getItemId();
        
        if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
        switch(id){
            case R.id.auth_update:
            	Log.d("onOptionsItemSelected", "auth_update");
            	
        		if(checkGPS()){
        			Log.d("checkGPS", "if true: GPS is enabled");
        			
        			if(item.getTitle().toString().compareTo("UPDATE ON") == 0){
        				Log.d("UPDATE ON","if true: UPDATE ON");
        				
        				startUpdates();
        				
                		item.setTitle("UPDATE OFF");
        			}
        			else{
        				Log.d("UPDATE ON","if false: UPDATE OFF");
        				
        				stopUpdates();
        				
                        item.setTitle("UPDATE ON");
        			}
        		}
        		else{
        			Log.d("checkGPS", "GPS disabled");
        			
        			Toast.makeText(MapActivity.this, "GPS is disabled. Cannot execute function",
        					Toast.LENGTH_LONG).show();
        		}
                return true;
            case R.id.clear_map:
            	Log.d("onOptionItemSelected", "clear_map");
            	clearMap();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * 
     * 		Code to check whether the GPS is active or not
     * @return
     * 		Return true if the GPS is active or false if GPS is not active
     * 
     */
    private boolean checkGPS(){
    	boolean result = false;
    	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
    	boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	//String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    	if(enabled){
    		result = true;
    	}
    	else{
    		result = false;
    	}
    	return result;
    }

    /**
     * 		Code to initialize the location variables
     * 
     */
    private void setLocationAttributes(){
        Log.d("MapActivity", "setLocationAtributtes()");
        
        mlRequest = LocationRequest.create();

        mlRequest.setInterval(280000);
        
        mlRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        
        updateRequested = false;
        
        mlClient = new LocationClient(this, this, this);
    }

    @Override
    public void onConnected(Bundle dataBundle){
        Log.d("MapActivity", "onConnected: dataBundle = " + dataBundle);
        
        if(updateRequested){
        	Log.d("updateRequested", "if true");
        	
            startPeriodicUpdates();
        }
        
        location = mlClient.getLastLocation();
    }

    @Override
    public void onDisconnected(){
        Log.d("MapActivity", "onDisconnected");
    }

    /**
     * 		Check if GooglePlayServices is avaiable
     * @return 
     * 		boolean - returns true if the GooglePlayServices is availabel, returns false if not
     * 
     */
    private boolean isServicesConnected(){
        //Check if Google Play Services is available
    	Log.d("MapActivity", "isServiceConnected");
    	
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(ConnectionResult.SUCCESS == resultCode){
            Log.d("Location", "Google Play is available");
            
            return true;
        }
        else{
            Log.d("Location","PROBLEM with Google Play Services");
            
            return false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
    	Log.d("MapActivity", "onConnectionFailed: result = " + result);
    	
        if(result.hasResolution()){
        	Log.d("hasResolution", "if true: resolution = " + result.hasResolution());
        	
            try{
                result.startResolutionForResult( this, LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch(IntentSender.SendIntentException e){
                Log.d("Exception","SendIntentException: e = " + e);
                
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location){
    	Log.d("MapActivity", "onLocationChanged: location = " + location);
    	
        this.location = location;
        
        Log.d("Lon, Lat", "" + location.getLongitude() + "-" + location.getLatitude());
        
        userLocation = new UserLocation(getUserName(), 
        								String.valueOf(location.getTime()), 
        								location.getLongitude(), 
        								location.getLatitude());
        
        client.addLocation(userLocation, this.getApplicationContext());
    }

    /**
     * 		Code to start making periodical updates to the server, only if GooglePlayServices is available
     * 
     */
    private void startUpdates(){
    	Log.d("MapActivity", "startUpdates");
    	
        updateRequested = true;
        
        if(isServicesConnected()){
        	Log.d("isServiceConnected", "if true");
        	
            startPeriodicUpdates();
        }

    }

    /**
     * 		Code to start making periodical updates to the server
     * 
     */
    public void startPeriodicUpdates(){
        Log.d("MapActivity","startPeriodicUpdates");
        
        mlClient.requestLocationUpdates(mlRequest, this);
    }

    /**
     * 		Code to stop making periodical updates to the server, only if GooglePlayServices is available
     * 
     */
    public void stopUpdates(){
    	Log.d("MapActivity", "stopUpdates");
    	
        updateRequested = false;

        if(isServicesConnected()){
        	Log.d("isServiceConnected", "if true");
        	
            stopPeriodicUpdates();
        }
    }

    /**
     * 		Code to stop making periodical updates to the server
     * 
     */
    public void stopPeriodicUpdates(){
        Log.d("MapActivity", "stopPeriodicUpdates");
        
        mlClient.removeLocationUpdates(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        
        
        Log.d("MapActivity","onStart");
        
        mlClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        
        Log.d("TestPause", "onResume");
        Log.d("MapActivity", "onResume");
    }

    @Override
    public void onStop(){
    	super.onStop();
    	
    	Log.d("MapActivity", "onStop");
    	
        if(mlClient.isConnected()){
        	Log.d("isConnected", "if true");
        	
            stopPeriodicUpdates();
        }      
    }

    /**
     * 		Code to make a call to the fragment to add markers with the stored locations
     * 
     */
    private void getLocationALL(){
    	Log.d("MapActivity", "getLocationAll");
    	
    	Bundle newArgs = new Bundle();
    	
    	newArgs.putString("username_history_checkin", getUserName());
    	
    	fragmentMap.setAllLocations(newArgs, this);
    }

    /**
     * 
     * @Class: DrawerItemClickListener
     * @Description
     * 		Class for the Drawer, it executes when an item is selected
     *
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(@SuppressWarnings("rawtypes") AdapterView parent, View view, int position, long id){
            Log.d("DrawerItemClickListener","onItemClick: parent = " + parent +
            		"\nview = " + view + "\nposition = " + position + "\nid = " + id);
            
            selectItem(position);
        }
    }
    
    /**
     * 		Code that changes fragment or executes the correspondent code depending on the item selected
     * @param position
     * 		Position of the item selected
     * 
     */
    private void selectItem(int position){
    	Log.d("MapActivity", "selecItem: position = " + position);
    	
        Fragment  fragment = null;

        Bundle args = new Bundle();
        
        if(selectedItem != position){
        	Log.d("selectedItem", "if true: selectedItem = " + selectedItem + 
        			"\nposition = " + position);
        	
        	switch(position){
		        case 0:
		        	Log.d("position", "Map");
		        	
		        	if(fragmentMap == null){
		        		Log.d("fragmentMap", "if true: fragmentMap = null");
		        		
		        		fragmentMap = new MapView();
		        		
		        		fragmentTransition(fragmentMap, position);
		        		
		        	}
		        	if(previousSelected == 1){
		        		Log.d("previousSelected", "if true");
		        		
		        		previousSelected = 0;
		        		
		        		fragmentTransition(fragmentMap, position);
		        	}
		        	
		        	//selectedItem = position;
		        	
		            break;
		        case 1:
		        	Log.d("position", "Show User Locations");
		        		
		        	getLocationALL();
		        	
		        	//selectedItem = position;
		        	break;
		        case 3:
		        	Log.d("position", "New Checkin");
		        	
		        	if(checkGPS()){
		        		Log.d("checkGPS", "if true: GPS is enabled");
		        		
		        		try{
			        		args.putDouble("longitude", location.getLongitude());
			        		
				        	args.putDouble("latitude", location.getLatitude());

				        	fragment = new CheckinFragment();
				        	
				            fragment.setArguments(args);
				            
				            fragmentTransition(fragment, position);
				            
				            previousSelected = 1;
			        	}
			        	catch(NullPointerException e){
			        		Log.d("Exception", "NullPointerException: e = " + e);
			        		
			        		Toast.makeText(MapActivity.this, "Could not get Location",
		        					Toast.LENGTH_LONG).show();
			        	}
		        		
		        		//selectedItem = position;
		        		
		        	}
		        	else{
		        		Log.d("checkGPS", "if true: GPS is disabled");
		        		
		        		Toast.makeText(MapActivity.this, "GPS is disabled. Cannot execute function",
	        					Toast.LENGTH_LONG).show();
		        	}	            
		            break;
		        case 4:  
		        	Log.d("position", "User - History Checkin");

		        	if(previousSelected == 1){
		        		Log.d("previousSelected", "if true");
		        		
		        		fragmentTransition(fragmentMap, position);
		        		
		        		previousSelected = 0;
		        	}
		        	
		        	getHistoryCheckin();
		        	
		        	//selectedItem = position;
		            break;
		        case 6:
		        	Log.d("position", "New Requests");
		        	fragment = new AddFriendFragment();
		        	
		        	fragment.setArguments(args);
		        	
		        	fragmentTransition(fragment, position);
		        	
		        	//selectedItem = position;
		        	break;
		        case 7:
		        	Log.d("position", "Pending Request");
		        	fragment = new RequestFriendFragment();
		        	
		        	fragment.setArguments(args);
		        	
		        	fragmentTransition(fragment, position);
		        	
		        	//selectedItem = position;
		        	break;
		        case 8:
		        	Log.d("position", "View Friends Location");
		        	
		        	GoogleMap map = fragmentMap.getMap();
		        	
		        	fragment = new LocationFriendFragment(map, fragmentMap);
		        	
		        	fragment.setArguments(args);
		        	
		        	fragmentTransition(fragment, position);
		        	
		        	//selectedItem = position;
		        	break;
		        case 9:
		        	Log.d("position", "Friend - Delete");
		        	
		        	
		        	fragment = new DeleteFriendFragment();
		        	
		        	fragment.setArguments(args);
		        	
		        	fragmentTransition(fragment, position);
		        	
		        	//selectedItem = position;
		        	break;
		        case 11:
		        	Log.d("position", "Logout");
		        	
		            Toast.makeText(MapActivity.this, "Login out",
		    				Toast.LENGTH_LONG).show();
		            
		        	functionLogout();
		            
		        	break;
		        	
		        case 2:
		        case 5:
		        case 10:
		        	Log.d("position", "Titles");
		        	break;
		    }
        	 if(position != 2 & position != 5 & position != 10){
         		
     		    NavDrawerLayout.closeDrawer(menuList);
         	}
        	
        }
        if(position != 2 & position != 5 & position != 10){
    		
		    NavDrawerLayout.closeDrawer(menuList);
    	}	
    }
    
    /**
     * 		Code that makes the fragments transaction
     * @param position
     * 		Position of the item selected
     * @param fragment
     * 		Fragment to be added to the view
     */
    private void fragmentTransition(Fragment fragment, int position){
    	Log.d("MapActivity", "fragmentTransition: fragment = " + fragment + 
    			"\nposition = " + position);
    	
    	FragmentManager fragmentManager = getSupportFragmentManager();
    	
    		if(mainMap){
    			fragmentManager.beginTransaction()
    			.replace(R.id.content_frame, fragment)
    			.addToBackStack("")
    			.commit();
    		}
    		else{
    			fragmentManager.beginTransaction()
    			.replace(R.id.content_frame, fragment)    			
    			.commit();
    			mainMap = false;
    		}
    }
    
    /**
     * 		Code to make a call to the fragment to add markers with the stored checkins
     * 
     */
    private void getHistoryCheckin(){
    	Log.d("MapActivity", "getHistoryCheckin");
    	
    	Bundle newArgs = new Bundle();
    	
    	newArgs.putString("username_history_checkin", getUserName());
    	
    	fragmentMap.setUIArguments(newArgs, this);
    }
    
    /**
     * 		Code to logout of the application
     * 
     */
    public void functionLogout(){
    	Log.d("MapActivity", "functionLogout");
    	
        SharedPreferences.Editor editor = preferences.edit();
        
        editor.remove("user_famfri");
        
        editor.remove("pwd_famfri");
        
        editor.commit();
        
        Toast.makeText(MapActivity.this, "Logout Successfully",
				Toast.LENGTH_LONG).show();
        
        finish();
    }
    
    /**
     * 		Code to store a checkin in the server (the buttons is added in the Checkin Fragment)
     * @param view
     * 		view of CheckinFragment
     * 
     */
    public void functionCheckin(View view){
    	Log.d("MapACtivity", "functionCheckin: view = " + view);
    	
    	Spinner placesSpinner = (Spinner) findViewById(R.id.spinner);
    	
    	EditText placeEditText;
    	
    	String placeName = "";  	
    	
    	EditText comment = (EditText) findViewById(R.id.experience_text);
    	
    	if(placesSpinner.getSelectedItem().toString().compareTo("Other") == 0){
    		Log.d("Spinner", "Selected Other");
    		
    		placeEditText = (EditText) findViewById(R.id.name_location);
    		
    		placeName = placeEditText.getText().toString();
    	}
    	else{
    		Log.d("Spinner", "Selected from Google Places");
    		
    		placeName = placesSpinner.getSelectedItem().toString();
    	}
    	
    	UserLocation userLocation = new UserLocation(getUserName(), 
    												String.valueOf(location.getTime()), 
    												location.getLongitude(), 
    												location.getLatitude(),
    												true,
    												comment.getText().toString(),
    												placeName);
    	
    	client.addCheckin(this, userLocation);
    	
    	selectItem(0);
    }
    
    /**
     * 		Code to cancel a checkin (the buttons is added in the Checkin Fragment)
     * @param view
     * 		view of CheckinFragment
     * 
     */
    public void functionCancelCheckin(View view){
    	Log.d("MapActivity", "functionCancelCheckin: view = " + view);
    	selectItem(0);
    }
    
    /**
     * 		Get the username from SharedPreferences
     * @return
     * 		Returns the username stored in SharedPreferences
     */
    private String getUserName(){
    	Log.d("MapActivity", "getUserName");
    	
    	SharedPreferences userName = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	return userName.getString("user_famfri", "");
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	Log.d("TestPause", "onPause");
    }
    
    /**
     * 		Clear the Markers of the map
     * 
     */
    public void clearMap(){
    	Log.d("MapActivity","clearMap");
    	fragmentMap.getMap().clear();
    }
    
}


