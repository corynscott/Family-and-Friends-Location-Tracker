package map.teamc.com.maplocationproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex64 on 16/10/2014.
 */
public class MapActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    //Map attributes
    private GoogleMap googleMap;

    //Location attributes
    private LocationRequest mlRequest;
    private LocationClient mlClient;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean updateRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        setLocationAttributes();


        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        int id = item.getItemId();
        switch(id){
            case R.id.auth_update:
                startUpdates();
                return true;
            case R.id.stop_update:
                stopUpdates();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLocationAttributes(){
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

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

            else{
                /*Intent intent = getIntent();
                Double lat = intent.getDoubleExtra("Lat", 0.0);
                Double lon = intent.getDoubleExtra("Lon", 0.0);
                Log.d("Lat", lat.toString());
                Log.d("Lon", lon.toString());*/
                googleMap.setMyLocationEnabled(true);
                // create marker
                //MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Hello Maps ");
                // adding marker
                //googleMap.addMarker(marker);
            }
        }
    }

    @Override
    public void onConnected(Bundle dataBundle){
        Log.d("Method", "onConnected");
        if(updateRequested){
            startPeriodicUpdates();
        }
    }

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

    @Override
    public void onLocationChanged(Location location){
        Log.d("Lon, Lat", "" + location.getLongitude() + "-" + location.getLatitude());
        //Code to store information on the Rest web service
        storeLocation(location);
    }

    public String getULI(String time){
        Date d = new Date(Long.parseLong(time));
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int month, day, hour, minute,second;
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);;
        String uli = "" + cal.get(Calendar.YEAR) +
                ((month < 10) ?  "0" + month :   month) +
                ((day < 10) ?  "0" + day :   day) +
                ((hour < 10) ?  "0" + hour :   hour) +
                ((minute < 10) ?  "0" + minute :   minute) +
                ((second < 10) ?  "0" + second :   second);
        return uli;
    }

    public String getDate(String time){
        Date d = new Date(Long.parseLong(time));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sss'Z'");
        String date = dateFormat.format(d);
        Log.d("Map", date);
        return date;
    }

    public final static String apiURL = "http://54.171.93.166:8080/LocationAppServer/location/";

    public void storeLocation(Location location){
        Context context = this.getApplicationContext();
        String uli = "";
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        try{
            String time = "" + location.getTime();
            String longitude = "" + location.getLongitude();
            String latitude = "" + location.getLatitude();
            String date = getDate(time);
            uli = getULI(time);
            jsonParams.put("@username","alex");
            jsonParams.put("@time", date);
            jsonParams.put("@longitude",longitude);
            jsonParams.put("@latitude", latitude);
            jsonParams.put("@ULI",String.valueOf(uli));



            StringEntity entity = new StringEntity(jsonParams.toString());
            url = apiURL +  uli;

            client.put(context, url,entity,"application/json", new AsyncHttpResponseHandler(){
                public void onSuccess(String result){
                    Log.d("REQUEST","Success");
                }

                public void onFailure(String fail){
                    Log.d("Request", "FAILURE");
                }
            });
        }
        catch(JSONException e){

        }
        catch(UnsupportedEncodingException e){

        }
    }

    @SuppressLint("NewApi")
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
    }

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
        initilizeMap();
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
}
