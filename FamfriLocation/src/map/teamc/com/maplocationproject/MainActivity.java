package map.teamc.com.maplocationproject;

import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    String [] Titles = new String[]{"Login", "Map" , "CheckIn", "Friends"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,Titles));


        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_launcher, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
               supportInvalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        Log.d("Creado1", "Creado");
        if (savedInstanceState == null) {
            SelectItem(0);
            //setUpFragments();
        }
        Log.d("Creado4", "Creado");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private GoogleMap mMap;
    private Fragment visible;
    private SupportMapFragment mMapFragment;

    public void SelectItem(int possition) {
        Log.d("Creado2", "Creado");
        Fragment fragment = null;
        Bundle args = new Bundle();
        Log.d("Test","" + "position = " + possition + " " + mDrawerList.isItemChecked(possition));
        //if(!mDrawerList.isItemChecked(possition)){
            switch (possition) {
                case 0:
                    fragment = new FragmentOne();
                    args.putString(FragmentOne.ITEM_NAME, Titles[possition]);
                    break;
                case 1:
                    /*fragment = new FragmentTwo();
                    args.putString(FragmentTwo.ITEM_NAME, Titles[possition]);*/

                    break;
                case 2:
                    //fragment = new FragmentThree();
                    fragment = new CheckinFragment();
                    //args.putString(FragmentThree.ITEM_NAME, Titles[possition]);
                    break;
                case 3:
                    fragment = new FragmentOne();
                    args.putString(FragmentOne.ITEM_NAME, Titles[possition]);

                    break;
                default:
                    break;
            }
            Log.d("Creado3", "Creado");
            fragment.setArguments(args);
            FragmentManager frgManager = getSupportFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .commit();

            mDrawerList.setItemChecked(possition, true);

            setTitle(Titles[possition]);
            mDrawerLayout.closeDrawer(mDrawerList);
        //}


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MFragment extends SupportMapFragment {
        public static final String TAG = "map";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static MFragment newInstance() {

            return new MFragment();
        }

        public MFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.map, container,
                    false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

    private void setUpFragments(){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mMapFragment = (MFragment) getSupportFragmentManager().findFragmentByTag(MFragment.TAG);
        if (mMapFragment == null) {
            mMapFragment = MFragment.newInstance();
            ft.add(R.id.content_frame, mMapFragment, MFragment.TAG);
        }
        ft.show(mMapFragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_layout_2))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.setIndoorEnabled(true);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

        }
    }

    private String[] array = new String[]{"Some","Thing"};
    private ArrayList<String> location = new ArrayList<String>();

    public void checkIn(View view){
        new GetPlaces(MainActivity.this, "").execute();
    }

    //Delete because the information will be sent to the screen
    private class GetPlaces extends AsyncTask<String,String,ArrayList<Place>> {

        private ProgressDialog dialog;
        private Context context;
        private String places = "";

        public GetPlaces(Context context, String places) {
            Log.d("Algo", "Algo");
            this.context = context;
            this.places = places;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Spinner spinner = (Spinner) findViewById(R.id.spinner);

            for (int i = 0; i < result.size(); i++) {
                //
                location.add(result.get(i).getName());
                //
                //
                /*googleMap.addMarker(new MarkerOptions()
                        .title(result.get(i).getName())
                        .position(
                                new LatLng(result.get(i).getLatitude(), result
                                        .get(i).getLongitude()))
                        .snippet(result.get(i).getVicinity()));*/
            }
            /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(result.get(0).getLatitude(), result
                            .get(0).getLongitude())) */// Sets the center of the map to
            // Mountain View
            array = location.toArray(array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(50.869988, -0.090715))
                    .zoom(14) // Sets the zoom
                    .tilt(30) // Sets the tilt of the camera to 30 degrees
                    .build(); // Creates a CameraPosition from the builder*/
            /*googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));*/
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected ArrayList<Place> doInBackground(String... arg0) {
            PlacesService service = new PlacesService(
                    "AIzaSyBRytPQuXPFSsFdDiKfpUXBkJsZpZbxXG4");
            //Log.d("COORDINATES","" + loc.getLongitude() + " " +loc.getLatitude());
            Log.d("BACK", "ALGO");
            //ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(), // 28.632808
            //        loc.getLongitude(), places); // 77.218276
            Log.d("Place", places);
            ArrayList<Place> findPlaces = service.findPlaces(50.869988, // 28.632808
                    -0.090715, places); // 77.218276

            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                Log.e("TAG", "places : " + placeDetail.getName());
            }
            return findPlaces;
        }
    }

}