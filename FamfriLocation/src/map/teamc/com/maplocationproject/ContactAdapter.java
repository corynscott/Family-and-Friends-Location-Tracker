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
 * 		Class that creates the layout for adding a new friend
 *
 */
public class ContactAdapter extends ArrayAdapter<ContactItem>{
	
	/*
     * Context of the activity to draw elements in the layout
     */
	Context context;
	
	/*
	 * The id of the parent layout
	 */
	int layoutResourceId;
	
	/*
	 * The layout that will display the information of the potential friends
	 */
	LinearLayout linear;
	
	/*
	 * ArrayList that will contain the information of the potential friends
	 */
	ArrayList<ContactItem> data = new ArrayList<ContactItem>();
	
	/*
     * LayoutInflater to create a new Layout within the code
     */
	LayoutInflater infl;
	
	/*
     * String variable for the username of the application
     */
	String userName;
	
	/*
	 * ArrayList that will contain the list of potential friends who have made 
	 * 		friend requests to this user or that the user has made friend requests
	 */
	ArrayList<String> pendingSendRequest = new ArrayList<String>();
	
	public ContactAdapter(Context context, int id, ArrayList<ContactItem> data, LayoutInflater infl, 
			String userName, ArrayList<String> pendingSendRequest){
		super(context, id, data);
		this.layoutResourceId = id;
		this.context = context;
		this.data = data;
		this.infl = infl;
		this.userName = userName;
		this.pendingSendRequest = pendingSendRequest;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		Log.d("ContactAdapter", "getView: position = " + position + "\nconvertView = " + convertView +
				"\nparent = " + parent);
		View row = convertView;
		if(row == null){
			LayoutInflater inflater = infl;
			row = inflater.inflate(layoutResourceId, parent, false);
			
			linear = (LinearLayout) row.findViewById(R.id.linear);
			
			ContactItem item = data.get(position);
			
			LinearLayout list = new LinearLayout(context);
			
			list.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView label = new TextView(context);
			
			label.setText(item.getUserName() + "\n" + item.getPhoneNumber());
			
			Log.d("Test", item.getUserName() + "\n" + item.getPhoneNumber());
			
			Button button = new Button(context);
			
			button.setText("Send Request");
			
			if(pendingSendRequest.contains(item.getUserName())){
				Log.d("pendingSendRequest", "if true: already sent request");
				
				button.setEnabled(false);	
			}
			else{
				Log.d("pendingSendRequest", "if false: no request sent");
				
				button.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v) {
						Log.d("button", "onClickView: v = " + v);
						Client client = new Client();
						Log.d("Test", data.get(position).getUserName());
					
						client.sendFriendRequest(context, v, data.get(position).getUserName(), userName);
					}
					
				});
			}
			
			
			list.addView(label);
			list.addView(button);
			linear.addView(list);
		}
		return row;
	}
	
}
