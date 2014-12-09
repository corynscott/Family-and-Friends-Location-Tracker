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
 * 		Class that creates the layout for the friend's request list and the buttons for accepting or rejecting the request
 *
 */
public class RequestAdapter extends ArrayAdapter<RequestItem>{
	
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
	 * ArrayList that will contain the information of the requests
	 */
	ArrayList<RequestItem> data = new ArrayList<RequestItem>();
	
	/*
     * LayoutInflater to create a new Layout within the code
     */
	LayoutInflater infl;
	
	/*
     * String variable for the username of the application
     */
	String username;
	
	/*
	 * LinearLayout that will display the username and the accept and reject buttons
	 */
	LinearLayout list;
	
	public RequestAdapter(Context context, int id, ArrayList<RequestItem> data, LayoutInflater infl, String username){
		super(context, id, data);
		this.layoutResourceId = id;
		this.context = context;
		this.data = data;
		this.infl = infl;
		this.username = username;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		Log.d("RequestAdapter", "getView: position = " + position + "\nconvertView = " + convertView +
				"\nparent = " + parent);
		View row = convertView;
		if(row == null){
			LayoutInflater inflater = infl;
			row = inflater.inflate(layoutResourceId, parent, false);
			
			linear = (LinearLayout) row.findViewById(R.id.requestLinear);
			
			RequestItem item = data.get(position);
			
			list = new LinearLayout(context);
			
			list.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView label = new TextView(context);
			
			label.setText(item.getUserName());
			
			Log.d("Test", item.getUserName());
			
			Button buttonAccept = new Button(context);
			
			buttonAccept.setText("Accept");
			
			Button buttonReject = new Button(context);
			
			buttonReject.setText("Reject");
			
			buttonAccept.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("buttonAccept", "onClickView: v = " + v);
					
					Client client = new Client();
					
					client.acceptRequest(context, linear, list, data.get(position).getUserName(), username);
					
				}
			});
			buttonReject.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("buttonReject", "onClickView: v = " + v);
					
					Client client = new Client();
					
					client.rejectRequest(context, linear, list, data.get(position).getUserName(), username);
				}
			});
			
			
			list.addView(label);
			list.addView(buttonAccept);
			list.addView(buttonReject);
			linear.addView(list);
		}
		return row;
	}
}
