package map.teamc.com.maplocationproject;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
 
/**
 * 		Class that creates the layout for the DrawMenu list
 *
 */
public class MyAdapter extends ArrayAdapter<Model> {
 
	/*
     * Context of the activity to draw elements in the layout
     */
	private final Context context;
	
	/*
	 * ArrayList that will contain the information of the elements of the side menu
	 */
    private final ArrayList<Model> modelsArrayList;
 
        public MyAdapter(Context context, ArrayList<Model> modelsArrayList) {
 
            super(context, R.layout.target_item, modelsArrayList);
 
            this.context = context;
            this.modelsArrayList = modelsArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
 
            View rowView = null;
            if(!modelsArrayList.get(position).isGroupHeader()){
                rowView = inflater.inflate(R.layout.target_item, parent, false);
 
                // 3. Get icon,title & counter views from the rowView
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
 
                // 4. Set the text for textView 
                titleView.setText(modelsArrayList.get(position).getTitle());
            }
            else{
                    rowView = inflater.inflate(R.layout.group_header_item, parent, false);
                    TextView titleView = (TextView) rowView.findViewById(R.id.header);
                    titleView.setText(modelsArrayList.get(position).getTitle());
 
            }
 
            // 5. retrn rowView
            return rowView;
        }
}