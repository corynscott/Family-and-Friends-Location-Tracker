package map.teamc.com.maplocationproject;


import com.google.android.gms.maps.GoogleMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 		Class that contains the fragment to view friends and request their location and checkins
 *
 */
public class LocationFriendFragment extends Fragment{
    
	/*
	 * The object of the client makes the calls to the server
	 */
    Client client = new Client();
    
    /*
     * LayoutInflater to create a new Layout within the code
     */
    LayoutInflater infl;
    
    /*
     * String that contains the name of the application SharedPreferences
     */
    public static final String MyPREFERENCES = "MyPrefs";
    
    /*
	 * GoogleMap variable
	 */
    private GoogleMap map;
    
    /*
	 * Fragment that contains the map
	 */
    private Fragment fragmentMap;

    public LocationFriendFragment(GoogleMap map, Fragment fragmentMap) {
    	this.map = map;
    	this.fragmentMap = fragmentMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("LocationFriendFragment", "onCreateView");
        View view = inflater.inflate(R.layout.friend_layout, container, false);
        
        infl = inflater;

        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	Log.d("LocationFriendFragment", "onActivityCreated: savedInstanceState = " + savedInstanceState);
    	super.onActivityCreated(savedInstanceState);

    	getListFriend();
    }
    
    /**
     * 		Call to Client class to get the list of friends of the server
     */
    private void getListFriend(){
    	client.getListFriend(this.getActivity(), getUserName(), infl, map, fragmentMap);
    }
    
    /**
     * 		Get the username from SharedPreferences
     * @return
     * 		Returns the username stored in SharedPreferences
     */
    private String getUserName(){
    	Log.d("LocationFriendFragment", "getUserName");
    	
    	SharedPreferences userName = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	return userName.getString("user_famfri", "");
    }
}

