package map.teamc.com.maplocationproject;

import java.util.ArrayList;

import android.content.Context;
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
 * 		Class that creates the layout for the friend's list and a button for deleting them
 *
 */
public class FriendDeleteAdapter extends ArrayAdapter<Friend>{
	
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
	 * LinearLayout that will display the username and the delete button
	 */
	LinearLayout list;
	
	public FriendDeleteAdapter(Context context, int id, ArrayList<Friend> data, LayoutInflater infl, 
			String userName){
		super(context, id, data);
		this.layoutResourceId = id;
		this.context = context;
		this.data = data;
		this.infl = infl;
		this.userName = userName;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		Log.d("FriendDeleteAdapter", "getView: position = " + position + "\nconvertView = " + convertView +
				"\nparent = " + parent);
		View row = convertView;
		if(row == null){
			LayoutInflater inflater = infl;
			row = inflater.inflate(layoutResourceId, parent, false);
			
			linear = (LinearLayout) row.findViewById(R.id.friendLinear);
			
			Friend item = data.get(position);
			
			list = new LinearLayout(context);
			
			list.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView label = new TextView(context);
			
			label.setText(item.getUsername() + "\n" + item.getPhoneNumber() + "\n" + item.getName());
			
			Log.d("Test", item.getUsername() + "\n" + item.getPhoneNumber() + "\n" + item.getName());
			
			Button button = new Button(context);
			
			button.setText("Delete");
			
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("buttonLocation", "onClickView: v = " + v);
					
					Client client = new Client();
					
					client.deleteFriend(context, linear, list, data.get(position).getUsername(), userName);
				}
			});
			
			
			list.addView(label);
			list.addView(button);
			linear.addView(list);
		}
		return row;
	}
	
}

