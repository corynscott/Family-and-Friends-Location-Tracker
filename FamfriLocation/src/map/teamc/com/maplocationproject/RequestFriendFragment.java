package map.teamc.com.maplocationproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 		Class that contains the fragment to view friends' requests
 *
 */
public class RequestFriendFragment extends Fragment{
    
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

    public RequestFriendFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("RequestFriendFragment", "onCreateView");
        View view = inflater.inflate(R.layout.request_friend_layout, container, false);
        
        infl = inflater;

        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	Log.d("RequestFriendFragment", "onActivityCreated: savedInstanceState = " + savedInstanceState);
    	super.onActivityCreated(savedInstanceState);

    	getRequest();
    }
    
    /**
     * 		Call to Client class to get the list of friends' requests
     */
    private void getRequest(){
    	client.getRequest(this.getActivity(), getUserName(), infl);
    }
    
    /**
     * 		Get the username from SharedPreferences
     * @return
     * 		Returns the username stored in SharedPreferences
     */
    private String getUserName(){
    	Log.d("RequestFriendFragment", "getUserName");
    	
    	SharedPreferences userName = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	return userName.getString("user_famfri", "");
    }
}

