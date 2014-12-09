package map.teamc.com.maplocationproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

/**
 * 		Class that contains the fragment to create a new checkin
 * 		It also contains a private class to populate a Spinner with the places obtained from Google Places
 *
 */
public class CheckinFragment extends Fragment {

	/*
	 * The array wil cotain places information obtained from GooglePlaces API
	 */
	private String[] array = {"Begining","End"};
	
	/*
	 * The array list that will contain the names of the places from GooglePlaces API
	 */
    private ArrayList<String> location = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("CheckinFragment", "onCreate: savedInstanceState = " + savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	Log.d("CheckinFragment", "onCreateView: inflater = " + inflater + 
    			"\ncontainer = " + container + "\nsavedInstanceState = " + savedInstanceState);
    	
        View view = inflater.inflate(R.layout.checkin_fragment, container,
                false);
        
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	
    	Log.d("CheckinFragment", "onActivityCreated: savedInstanceState = " + savedInstanceState);
    	
    	double longitude = getArguments().getDouble("longitude");;
    	
    	double latitude = getArguments().getDouble("latitude");
    	
    	new GetPlaces(getActivity(), "", longitude, latitude).execute();
    	
    }

    /**
     * 		Class that populate a Spinner with the places from Google Places API
     *
     */
    private class GetPlaces extends AsyncTask<String,String,ArrayList<Place>> {

    	/*
    	 * It is used to show a progress dialog animation while executing the action
    	 */
        private ProgressDialog dialog;
        
        /*
         * Context of the activity to draw elements in the layout
         */
        private Context context;
        
        /*
         * Variable that will contain the name of the place
         */
        private String places = "";
        
        /*
         * One of the elements to send the localization information through the URL 
         */
        private double longitude = 0.0;
        
        /*
         * One of the elements to send the localization information through the URL 
         */
        private double latitude = 0.0;

        public GetPlaces(Context context, String places, double longitude, double latitude) {
            this.context = context;
            this.places = places;
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);
            
            Log.d("GetPlaces", "onPostExecute: result = " + result);
            
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            
            Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);

            for (int i = 0; i < result.size(); i++) {
                location.add(result.get(i).getName());
            }
            
            
            location.add("Other");
            
            array = location.toArray(array);
            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, array);
            
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            
            spinner.setAdapter(adapter);
            
            spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.d("OnItemSelectedListener", "onItemSelected: arg0 = " + arg0 + 
							"\narg1 = " + arg1 + " \narg2 = " + arg2 + "\narg3 = " + arg3);
					
					Object item = arg0.getItemAtPosition(arg2);
					
					if(item != null){
						Log.d("OnItemSelected","if true: item is not null");
						
						EditText newPlace = (EditText) getActivity().findViewById(R.id.name_location);
						
						if(item.toString().compareTo("Other") == 0){
							Log.d("OnItemSelected", "if true: item = " + item);
							
							if(newPlace.getVisibility() != View.VISIBLE){
								Log.d("OnItemSelected", "if true: newPlace = " + newPlace.getVisibility());
								
								newPlace.setVisibility(View.VISIBLE);
							}
						}
						else{
							Log.d("OnItemSelected", "if false: item = " + item);
							
							if(newPlace.getVisibility() != View.INVISIBLE){
								Log.d("OnItemSelected", "if true: newPlace = " + newPlace.getVisibility());
								
				                newPlace.setVisibility(View.INVISIBLE);
				            }
						}
					}
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					
				}

            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            Log.d("GetPlaces", "onPreExecute");
            
            dialog = new ProgressDialog(context);
            
            dialog.setCancelable(false);
            
            dialog.setMessage("Loading..");
            
            dialog.isIndeterminate();
            
            dialog.show();
        }

        @Override
        protected ArrayList<Place> doInBackground(String... arg0) {
        	Log.d("GetPlaces", "doInBackground: arg0 = " + arg0);
        	
            PlacesService service = new PlacesService(
                    "AIzaSyBRytPQuXPFSsFdDiKfpUXBkJsZpZbxXG4");
            
            ArrayList<Place> findPlaces = service.findPlaces(this.latitude,
                    this.longitude, this.places);
            
            return findPlaces;
        }
    }

}
