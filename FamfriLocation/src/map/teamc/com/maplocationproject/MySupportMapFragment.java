package map.teamc.com.maplocationproject;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

public class MySupportMapFragment extends SupportMapFragment{
	
	private MySupportMapFragmentListener listener;
	
	public interface MySupportMapFragmentListener{
		public void onMapCreated(GoogleMap googleMap);
	}
	
	private static final String MAP_OPTIONS = "MapOptions";
	
	public static MySupportMapFragment newInstance(){
		MySupportMapFragment f = new MySupportMapFragment();
		return f;
	}
	
	public static MySupportMapFragment newInstance(GoogleMapOptions options){
		MySupportMapFragment f = new MySupportMapFragment();
		Bundle args = new Bundle();
		args.putParcelable(MAP_OPTIONS, options);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		if(listener != null){
			listener.onMapCreated(getMap());
		}
	}
	
	public MySupportMapFragmentListener getListener(){
		return listener;
	}
	
	public void setListener(MySupportMapFragmentListener listener){
		this.listener = listener;
	}
}
