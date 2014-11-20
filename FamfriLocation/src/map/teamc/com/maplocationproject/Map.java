package map.teamc.com.maplocationproject;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by alex64 on 12/11/2014.
 */



public class Map extends Fragment {
    //Map attributes
    private static View view;
    
    private SupportMapFragment fragment;
    private GoogleMap googleMap;
    
    private ArrayList<UserLocation> historyCheckin = null;
    
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    	Log.d("Map", "onAttach");
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	Log.d("Map", "onCreate");
    	if(savedInstanceState != null){
    		//Restore state
    	}
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Map", "onCreateView");
    	if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.map_layout, container, false);
            
            
            Log.d("Map", "Creating Map Fragment\n" + savedInstanceState);
        }
        catch(InflateException e){
            Log.d("InflateException","Map_Class");
        }
        return view;
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	Log.d("Map", "onStart");
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	Log.d("Map", "onPause");
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	Log.d("Map", "onStop");
    }
    
    @Override
    public void onSaveInstanceState(Bundle toSave){
    	super.onSaveInstanceState(toSave);
    	Log.d("Map", "onSaveInstanceState");
    	getChildFragmentManager().putFragment(toSave, "mapContent", fragment);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	//Safe for making objects by their ID
    	Log.d("Map", "onActivityCreated: savedInstanceState = " + savedInstanceState);
    	if(savedInstanceState != null){
    		Log.d("onActivityCreated", "if true: savedInstanceState is not null");
    		fragment = (SupportMapFragment) getChildFragmentManager().getFragment(savedInstanceState, "mapFragment");
    		historyCheckin = (ArrayList<UserLocation>) getArguments().getSerializable("history_checkin");
    	}
    	else{
    		FragmentManager fm = getChildFragmentManager();
        	fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_layout_2);
        	Log.d("Null", "Null");
        	if(getArguments() != null){
        		//historyCheckin = (ArrayList<UserLocation>) getArguments().getSerializable("history_checkin");
        	}
        	Log.d("Null", "Null");
        	if(fragment == null){
        		Log.d("fragment", "null");
        		
        		fragment = SupportMapFragment.newInstance();
        		fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        	}
    	}
    	
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	Log.d("Map, onResume", "Yep");
    		Log.d("map", "null");
    		try{
    			initializeMap();
    		}
    		catch(NullPointerException e){
    			Log.d("NullException", "Map, onResume");
    		}
    		/*map = fragment.getMap();
    		map.addMarker(new MarkerOptions().position(new LatLng(0,0)));*/
    	
    	/*else{
    		map.addMarker(new MarkerOptions().position(new LatLng(0,0)));
    	}*/
    }
    
    //MarkerOptions marker = new MarkerOptions().position(new LatLng(0, 0)).title("Hello Maps ");
    
    private void initializeMap() {
        Log.d("MapActivity", "initializeMap");
        if (googleMap == null) {
        	Log.d("initializeMap","if true: googleMap is null");
        	googleMap =	fragment.getMap();
            if (googleMap == null) {
            	Log.d("initializeMap", "if true: googleMap is still null");
                Log.d("Error","OnMap");
            }

            else{
            	Log.d("initializeMap", "if true: googleMap is no longer null");
                /*Intent intent = getIntent();
                Double lat = intent.getDoubleExtra("Lat", 0.0);
                Double lon = intent.getDoubleExtra("Lon", 0.0);
                Log.d("Lat", lat.toString());
                Log.d("Lon", lon.toString());*/
            	//Log.d("Map", "Create MAarker");
                googleMap.setMyLocationEnabled(true);
                
                if(historyCheckin != null){
            		Log.d("initializeMap", "if true: historyCheckin = " + historyCheckin);
            		MarkerOptions marker;
            		for(int i = 0; i < historyCheckin.size(); i++){
            			marker = new MarkerOptions().position(new LatLng(historyCheckin.get(i).getLatitude(), 
            															historyCheckin.get(i).getLongitude()))
            										.title(historyCheckin.get(i).getPlaceName())
            										.snippet(historyCheckin.get(i).getComment());
            			googleMap.addMarker(marker);
            		}
            	}
                // create marker
                
                //adding marker
                //googleMap.addMarker(marker);
            }
        }
        else{
        	Log.d("initializeMap","if true: googleMap = " + googleMap);
        	if(historyCheckin != null){
        		Log.d("initializeMap", "if true: historyCheckin = " + historyCheckin);
        		MarkerOptions marker;
        		for(int i = 0; i < historyCheckin.size(); i++){
        			marker = new MarkerOptions().position(new LatLng(historyCheckin.get(i).getLatitude(), 
        															historyCheckin.get(i).getLongitude()))
        										.title(historyCheckin.get(i).getPlaceName())
        										.snippet(historyCheckin.get(i).getComment());
        		}
        	}
        	//googleMap.addMarker(marker);
        }
    }
    
    public void setUIArguments(final Bundle args){
    	Log.d("Map", "setUIArguments: args = " + args);
    	getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				Log.d("Runnable", "run");
				historyCheckin = (ArrayList<UserLocation>) args.getSerializable("history_checkin");
				MarkerOptions marker;
        		for(int i = 0; i < historyCheckin.size(); i++){
        			marker = new MarkerOptions().position(new LatLng(historyCheckin.get(i).getLatitude(), 
        															historyCheckin.get(i).getLongitude()))
        										.title(historyCheckin.get(i).getPlaceName())
        										.snippet(historyCheckin.get(i).getComment());
        			googleMap.addMarker(marker);
        		}
			}
    		
    	});
    }
}
