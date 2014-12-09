package map.teamc.com.maplocationproject;

import android.app.Activity;
import android.content.Context;
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

/**
 * 
 * 		Class that contains the fragment of the map
 *
 */
public class MapView extends Fragment {
    
	/*
	 * View of the map
	 */
    private static View view;
    
    /*
     * SupportFragment needed for the map and used for  API < 14
     */
    private SupportMapFragment fragment;
    
    /*
     * GoogleMap variable
     */
    private GoogleMap googleMap;
    
    /*
	 * The object of the client makes the calls to the server
	 */
    private Client client = new Client();
    
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    	Log.d("MapView", "onAttach");
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	Log.d("MapView", "onCreate");
    	if(savedInstanceState != null){
    		//Restore state
    	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MapView", "onCreateView");
    	if(view != null){
    		 Log.d("view", "if true: view is null");
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.map_layout, container, false);
        }
        catch(InflateException e){
            Log.d("InflateException","Map_Class");
        }
        return view;
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	Log.d("MapView", "onStart");
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	Log.d("MapView", "onPause");
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	Log.d("MapView", "onStop");
    }
    
    @Override
	public void onDetach() {
		super.onDetach();
		Log.d("MapView","onDetach");
	}
    
    @Override
    public void onSaveInstanceState(Bundle toSave){
    	super.onSaveInstanceState(toSave);
    	Log.d("MapView", "onSaveInstanceState");
    	getChildFragmentManager().putFragment(toSave, "mapContent", fragment);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	//Safe for making objects by their ID
    	Log.d("MapView", "onActivityCreated: savedInstanceState = " + savedInstanceState);
    	if(savedInstanceState != null){
    		Log.d("onActivityCreated", "if true: savedInstanceState is not null");
    		fragment = (SupportMapFragment) getChildFragmentManager().getFragment(savedInstanceState, "mapFragment");
    	}
    	else{
    		FragmentManager fm = getChildFragmentManager();
        	fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_layout_2);
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
    	Log.d("MapView", "onResume");
    		try{
    			initializeMap();
    			
    		}
    		catch(NullPointerException e){
    			Log.d("NullException", "Map, onResume");
    		}
    }
    
    /**
     * @Function: initializeMap
     * @Access:  private
     * @Return: void
     * @Description:
     * 		Code to initialize the GoogleMap variable
     * 
     */
    private void initializeMap() {
        Log.d("MapView", "initializeMap");
        if (googleMap == null) {
        	Log.d("initializeMap","if true: googleMap is null");
        	googleMap =	fragment.getMap();
            if (googleMap == null) {
            	Log.d("initializeMap", "if true: googleMap is still null");
                Log.d("Error","OnMap");
            }

            else{
            	
            	Log.d("initializeMap", "if true: googleMap is no longer null");
                googleMap.setMyLocationEnabled(true);
               
            }
        }
        else{
        	Log.d("initializeMap","if true: googleMap = " + googleMap);
        }
    }
    
    /**
     * @Function: setUIArguments
     * @Access:  public
     * @Return: void
     * @Description:
     * 		Code to create a Thread that will add markers with the checkins to the map
     * @param args
     * 		Contains data from the main activity
     * @param context
     * 		Context of the activity
     * 
     */
    public void setUIArguments(final Bundle args, final Context context){
    	Log.d("MapView", "setUIArguments");
    	getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				Log.d("Runnable", "run");
				client.getUserCheckin(args.getString("username_history_checkin"), context, googleMap);
				Log.d("Continued","setUIArgument");
			}
    		
    	});
    }
    
    /**
     * @Function: setAllLocations
     * @Access:  public
     * @Return: void
     * @Description:
     * 		Code to create a Thread that will add markers with the locations to the map
     * @param args
     * 		Contains data from the main activity
     * @param context
     * 		Context of the activity
     * 
     */
    public void setAllLocations(final Bundle args, final Context context){
    	Log.d("MapView", "setAllLocations");
    	getActivity().runOnUiThread(new Runnable(){
    		
    		@Override
    		public void run(){
    			Log.d("setAllLocations", "run");
    			client.getAllLocations(args.getString("username_history_checkin"), context, googleMap);
    		}
    	});
    }
    
    /**
     * @Function: getMap
     * @Access:  public
     * @Return: GoogleMap
     * @Description:
     * 		Code to return GoogleMap variable
     * @return
     * 		GoogleMap variable
     * 
     */
    public GoogleMap getMap(){
    	return googleMap;
    }
}
