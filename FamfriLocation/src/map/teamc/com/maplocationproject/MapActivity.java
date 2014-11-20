package map.teamc.com.maplocationproject;

import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.ArrayList;
import android.database.Cursor;

/**
 * Created by alex64 on 16/10/2014.
 */

/**
 * 
 * @Class: MapActivity
 * @Inheritance: FragmentActivity
 * @Interfaces: 
 * 		GooglePlayServicesClient.ConnectionCallbacks
 * 		GooglePlayServicesClient.OnConnectionFailedListener
 * 		LocationListener
 * @Description
 * 		Fragment Activity, this is the main activity, it is divided in content_frame and a 
 * 			menu list.
 * 			The content_frame will change depending of the action required.
 * @Created: 16/11/2014
 *
 */
@SuppressWarnings("deprecation")
public class MapActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener{//,
        //GoogleMap.OnMarkerClickListener {

    //Map attributes
    //private GoogleMap googleMap;

    //Location attributes
    private LocationRequest mlRequest;
    private LocationClient mlClient;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    boolean updateRequested;

    private UserLocation userLocation = null;
    private Client client = new Client();

    JsonAdapter mJsonAdapter;

    private String[] places = new String[1];
    private Location location;

    ListView menuList;
    
    String [] Titles = new String[]{"Map" , "CheckIn", "New CheckIn", "History CheckIn", 
    								"Friends", "Add Friends", "View Location", "History CheckIn"};

    private DrawerLayout NavDrawerLayout;

    
	private ActionBarDrawerToggle mDrawerToggle;
    /*private CharSequence mDrawerTitle;
    private CharSequence mTitle;*/
    
	
	private Map fragmentMap = null;
	
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        
        Log.d("MapActivity","Creating Instance\n" + savedInstanceState);
        //
        //mTitle = mDrawerTitle = getTitle();
        //Titles = getResources().getStringArray(R.array)
        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        
        menuList = (ListView) findViewById(R.id.MenuList);
        menuList.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,Titles));
        menuList.setOnItemClickListener(new DrawerItemClickListener());



        mDrawerToggle = new ActionBarDrawerToggle(this, NavDrawerLayout,
                R.drawable.ic_launcher, R.string.hello_world,R.string.app_name){

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                Log.d("Algo","Algo");
                supportInvalidateOptionsMenu();
            }
        };
        NavDrawerLayout.setDrawerListener(mDrawerToggle);
        //

        //
        places[0] = "";
        //

        /*TextView text = (TextView)findViewById(R.id.UpdateTimeText);
        text.setText("Algo");*/

        //
        setLocationAttributes();
        selectItem(0);
        /*try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = NavDrawerLayout.isDrawerOpen(menuList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("Algo","Algo");
        int id = item.getItemId();
        switch(id){
            case R.id.auth_update:
                startUpdates();
                return true;
            case R.id.stop_update:
                stopUpdates();
                return true;
            case R.id.getLocationULI:
                getLocationULI();
                return true;
            case R.id.getLocationALL:
                getLocationALL();
                //locationMarkers();
                return true;
            case R.id.getPlaces:
                //getPlaces();
                return true;
            case R.id.checkNumber:
                //checkNumber();
                return true;
            case R.id.contact_id:
            	getContact();
            	return true;
            case R.id.logout:
                functionLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void setLocationAttributes(){
        Log.d("MapActivity", "setLocationAtributtes()");
        mlRequest = LocationRequest.create();
        //mlRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
        mlRequest.setInterval(280000);
        mlRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mlRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        updateRequested = false;
        pref = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = pref.edit();
        mlClient = new LocationClient(this, this, this);
    }

    

    /*@Override
    public boolean onMarkerClick(final Marker marker){
        Log.d("GetPlaces", "" + marker.getTitle());
        return true;
    }*/

    @Override
    public void onConnected(Bundle dataBundle){
        Log.d("Method", "onConnected");
        if(updateRequested){
            startPeriodicUpdates();
        }
        location = mlClient.getLastLocation();
    }

    /**
     * 
     */
    @Override
    public void onDisconnected(){
        Log.d("Method", "onDisconnected");
    }

    private boolean isServicesConnected(){
        //Check if Google Play Services is available
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

    /**
     * 
     */
    @Override
    public void onConnectionFailed(ConnectionResult result){
        if(result.hasResolution()){
            try{
                result.startResolutionForResult( this, LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch(IntentSender.SendIntentException e){
                e.printStackTrace();
                Log.d("ERROR","ERROR");
            }
        }
    }

    /**
     * 
     */
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

    /*@SuppressLint("NewApi")
    public void getAddress(View v){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()){
            Log.d("Error", "ERROR - No Geocoder available");
            return;
        }

        if(isServicesConnected()){
            //Get the location
            Location currentLocation = mlClient.getLastLocation();

            //Background Task
            //TODO
        }
    }*/

    public void startUpdates(){

        updateRequested = true;
        if(isServicesConnected()){
            startPeriodicUpdates();
        }

    }

    public void startPeriodicUpdates(){
        Log.d("Method","StartPeriodicUpdates");
        mlClient.requestLocationUpdates(mlRequest, this);
    }

    public void stopUpdates(){

        updateRequested = false;

        if(isServicesConnected()){
            stopPeriodicUpdates();
        }
    }

    public void stopPeriodicUpdates(){
        Log.d("Method", "StopPeriodicUpdates");
        mlClient.removeLocationUpdates(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("Connected","Connection");
        mlClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MapActivity, onResume", "Resume");
        //initializeMap();
        if(pref.contains(LocationUtils.KEY_UPDATES_REQUESTED)){
            updateRequested = pref.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
        }
        else{
            editor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            editor.commit();
        }

    }

    @Override
    public void onStop(){
        if(mlClient.isConnected()){
            stopPeriodicUpdates();
        }
        super.onStop();
    }

    public void getLocationULI(){
        client.getLocationULI(this.getApplicationContext());
    }

    public void getLocationALL(){
        client.getLocationAll();
    }

    /*public void locationMarkers(){
        ArrayList<HashMap<String, String>> locationList = client.getLocationList();
        Log.d("get result", "result");
        MarkerOptions marker;
        String latitude;
        String longitude;
        String username;
        String time;
        for(int i = 0; i < locationList.size(); i++){
            // create marker
            //MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Hello Maps ");
            // adding marker
            //googleMap.addMarker(marker);
            latitude = locationList.get(i).get("latitude");
            longitude = locationList.get(i).get("longitude");
            username = locationList.get(i).get("username");
            time = locationList.get(i).get("time");
            marker = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title(username + "\n" + time);
            googleMap.addMarker(marker);
        }
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id){
            Log.d("Drawer","ItemSelected");
            Log.d("Drawer","" + position + " " + id);
            selectItem(position);
        }
    }

    /**
     * 
     * @param position
     */
    private void selectItem(int position){
    	Log.d("MapActivity", "selecItem: position = " + position);
        Fragment  fragment = null;
        Log.d("MapActivity, selectItem", "position = " + position);
        Log.d("Test", "" + menuList.isItemChecked(position));
        Bundle args = new Bundle();
            
        switch(position){
            case 0:
            	//TODO Check if the fragment is already created, if yes, then do nothing
                fragmentMap = new Map();
                break;
            case 2:
            	args.putDouble("longitude", location.getLongitude());
            	args.putDouble("latitude", location.getLatitude());
                fragment = new CheckinFragment();
                fragment.setArguments(args);
                break;
            case 3:           	
            	client.getUserCheckin(getUserName());
            	ArrayList<UserLocation> historyCheckin = client.getHistoryCheckin();
            	Bundle newArgs = new Bundle();
            	newArgs.putSerializable("history_checkin", historyCheckin);
            	Log.d("Here","Here");
        		//fragmentMap = new Map();
                fragmentMap.setUIArguments(newArgs);
                break;
            case 5:
            	break;
            case 6:
            	break;
            case 7:
            	break;
            case 1:
            case 4:
            	break;
        }


        Log.d("Algo","Algo");

        FragmentManager fragmentManager = getSupportFragmentManager();
        
        Log.d("selectItem","Transaction");
        //TODO Check the posibility of not selecting another map when displayed
        //		Maybe diabled the options in the menu
        if(position == 0){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragmentMap)
                    .commit();
            //fragmentManager.popBackStack();
        }
        else{
        	if(position != 1 & position != 4 & position != 3){
        		fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("")
                .commit();
        	}       
        }

        menuList.setItemChecked(position, true);
        //setTitle(Titles[position]);
        NavDrawerLayout.closeDrawer(menuList);
    }
    

    @Override
    public void setTitle(CharSequence title){
        //mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    public void pushButton(View view){
        Log.d("Work This","Work!!!!! " + view);
    }
    
    public void getContact(){
    	ArrayList<String> listName = new ArrayList<String>();
    	ArrayList<String> contactId = new ArrayList<String>();
    	ArrayList<String> no = new ArrayList<String>();
    	Uri simUri = Uri.parse("content://icc/adn");
    	Cursor cursorSim = this.getContentResolver().query(simUri, null, null, null, null);
    	
    	while(cursorSim.moveToNext()){
    		//listName.add(cursorSim.getString(cursorSim.getColumnIndex("name")));
    		Log.d("Name",cursorSim.getString(cursorSim.getColumnIndex("name")));
    		//contactId.add(cursorSim.getString(cursorSim.getColumnIndex("_id")));
    		Log.d("Id",cursorSim.getString(cursorSim.getColumnIndex("_id")));
    		//no.add(cursorSim.getString(cursorSim.getColumnIndex("number")));
    		Log.d("No",cursorSim.getString(cursorSim.getColumnIndex("number")));
    	}
    }
    
    public void functionLogout(){

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("number");
        editor.remove("pwd");
        editor.commit();
        finish();
    }
    
    /**
     * 
     * @param view
     */
    public void functionCheckin(View view){
    	Log.d("MapACtivity", "functionCheckin: view = " + view);
    	
    	Spinner placesSpinner = (Spinner) findViewById(R.id.spinner);
    	EditText placeEditText;
    	String placeName = "";  	
    	
    	EditText comment = (EditText) findViewById(R.id.experience_text);
    	if(placesSpinner.getSelectedItem().toString().compareTo("Other") == 0){
    		placeEditText = (EditText) findViewById(R.id.name_location);
    		placeName = placeEditText.getText().toString();
    	}
    	else{
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
    	
    	//Add movement to map fragment
    }
    
    /**
     * 
     * @param view
     */
    public void functionCancelCheckin(View view){
    	Log.d("MapActivity", "functionCancelCheckin: view = " + view);
    	selectItem(0);
    }
    
    /**
     * 
     * @return
     */
    private String getUserName(){
    	SharedPreferences userName = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	return userName.getString("user_famfri", "");
    }
}


