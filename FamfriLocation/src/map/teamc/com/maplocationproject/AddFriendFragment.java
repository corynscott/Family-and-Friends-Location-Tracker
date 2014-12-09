package map.teamc.com.maplocationproject;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 		Fragment class - Add new friends view
 */
public class AddFriendFragment extends Fragment{
    
	/*
	 * The object of the client makes the calls to the server
	 */
    private Client client = new Client();
    
    /*
     * LayoutInflater to create a new Layout within the code
     */
    private LayoutInflater infl;
    
    /*
     * String that contains the name of the application SharedPreferences
     */
    public static final String MyPREFERENCES = "MyPrefs";

    public AddFriendFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("AddFriendFragment", "onCreateView: inflater = " + inflater + "\ncontainer = " + container + 
    			"\nsavedInstanceState = " + savedInstanceState);
    	
        View view = inflater.inflate(R.layout.add_friend_layout, container, false);
        
        infl = inflater;

        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	Log.d("AddFriendFragment", "onActivityCreated: savedInstanceState = " + savedInstanceState);
    	
    	super.onActivityCreated(savedInstanceState);
    	
    	getContact();
    }
    
    /**
     * 		Get the contacts from Contact list
     * 
     */
    private void getContact(){
    	Log.d("AddFriendFragment", "getContact");
    	
    	ArrayList<ContactItem> contact = new ArrayList<ContactItem>();
    	
    	Cursor cursor = null;
    	
    	try{
    		cursor = getActivity().getContentResolver().query(Phone.CONTENT_URI, null, null, null,null);
    		
    		int nameId = cursor.getColumnIndex(Phone.DISPLAY_NAME);
    		
    		int phoneId = cursor.getColumnIndex(Phone.NUMBER);
    		
    		cursor.moveToFirst();
			
    		do{
    			
    			
    			String nameContact = cursor.getString(nameId);
    			
    			String phoneContact = cursor.getString(phoneId);
    			
    			phoneContact = phoneContact.replaceAll(" ", "");
    			
    			phoneContact = phoneContact.replaceAll("-", "");
    			
    			Log.d("Contact", nameContact + " " + phoneContact);
    			
    			contact.add(new ContactItem(nameContact, phoneContact));
    			
    		}
    		while(cursor.moveToNext());
			
    		cursor.close();
    		
    		this.getListFriends(contact);
    		
    	}
    	catch(Exception e){
    		Log.d("Exception", "getContact: No contacts");
    		
    		Toast.makeText(this.getActivity(), "There are no contacts in your Contact list", 
    				Toast.LENGTH_LONG).show();
    	}
    }
    
    /**
     * 		Call to Client class to get the list of friends of the server
     * @param contact
     * 			An ArrayList of ContactItems, contains the contacts from the Contact list
     * 
     */
    private void getListFriends(ArrayList<ContactItem> contact){
    	Log.d("AddFriendFragment", "getListFriends");
    	
    	client.getFriends(this.getActivity(), getUserName(), contact, infl);
    }
    
    /**
     * 		Get the username from SharedPreferences
     * @return
     * 		Returns the username stored in SharedPreferences
     * 
     */
    public String getUserName(){
    	Log.d("AddFriendFragment", "getUserName");
    	
    	SharedPreferences userName = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    	
    	return userName.getString("user_famfri", "");
    }
}
