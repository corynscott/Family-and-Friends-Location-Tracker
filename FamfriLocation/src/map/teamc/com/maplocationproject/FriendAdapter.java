package map.teamc.com.maplocationproject;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 		Class that creates the layout for the friend's list and the buttons for viewing location or checkins
 *
 */
public class FriendAdapter extends ArrayAdapter<Friend>{
	
	/*
     * Context of the activity to draw elements in the layout
     */
	Context context;
	
	/*
	 * The id of the parent layout
	 */
	int layoutResourceId;
	
	/*
	 * The layout that will display the information of the friends
	 */
	LinearLayout linear;
	
	/*
	 * ArrayList that will contain the information of the friends
	 */
	ArrayList<Friend> data = new ArrayList<Friend>();
	
	/*
     * LayoutInflater to create a new Layout within the code
     */
	LayoutInflater infl;
	
	/*
     * String variable for the username of the application
     */
	String userName;
	
	/*
	 * GoogleMap variable
	 */
	GoogleMap map;
	
	/*
	 * Fragment that contains the map
	 */
	Fragment fragmentMap;
	
	public FriendAdapter(Context context, int id, ArrayList<Friend> data, LayoutInflater infl, 
			String userName, GoogleMap map, Fragment fragmentMap){
		super(context, id, data);
		this.layoutResourceId = id;
		this.context = context;
		this.data = data;
		this.infl = infl;
		this.userName = userName;
		this.map = map;
		this.fragmentMap = fragmentMap;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		Log.d("ContactAdapter", "getView: position = " + position + "\nconvertView = " + convertView +
				"\nparent = " + parent);
		View row = convertView;
		if(row == null){
			LayoutInflater inflater = infl;
			row = inflater.inflate(layoutResourceId, parent, false);
			
			linear = (LinearLayout) row.findViewById(R.id.friendLinear);
			
			Friend item = data.get(position);
			
			LinearLayout list = new LinearLayout(context);
			
			list.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView label = new TextView(context);
			
			label.setText(item.getUsername() + "\n" + item.getPhoneNumber() + "\n" + item.getName());
			
			Log.d("Test", item.getUsername() + "\n" + item.getPhoneNumber() + "\n" + item.getName());
			
			Button buttonLocation = new Button(context);
			
			buttonLocation.setText("Last Location");
			
			Button buttonCheckin = new Button(context);
			
			buttonCheckin.setText("Checkins");
			
			buttonLocation.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("buttonLocation", "onClickView: v = " + v);
					
					Client client = new Client();
					
					client.getFriendLocation(context, data.get(position).getUsername(), userName, map, fragmentMap);
				}
			});
			
			buttonCheckin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("buttonCheckin", "onClickView: v = " + v);
					
					Client client = new Client();
					
					client.getFriendCheckin(context, data.get(position).getUsername(), userName, map, fragmentMap);
				}
			});
			
			
			list.addView(label);
			list.addView(buttonLocation);
			list.addView(buttonCheckin);
			linear.addView(list);
		}
		return row;
	}
	
}

