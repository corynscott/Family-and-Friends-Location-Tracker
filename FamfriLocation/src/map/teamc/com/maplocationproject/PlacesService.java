package map.teamc.com.maplocationproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by alex64 on 06/11/2014.
 */
public class PlacesService {
    private String API_KEY;

    public PlacesService(String apikey){
        this.API_KEY = apikey;
    }

    public void setApiKey(String apikey){
        this.API_KEY = apikey;
    }

    public ArrayList<Place> findPlaces(double latitude, double longitude,
                                       String placeSpecification){
        String urlString = makeURL (latitude, longitude, placeSpecification);
        try{
            String json = getJSON(urlString);
            Log.d("JSON", json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");

            ArrayList<Place> arrayList = new ArrayList<Place>();
            for(int i = 0; i < array.length(); i++){
                try{
                    Place place = Place.jsonReference((JSONObject)array.get(i));
                    Log.d("Places Services","" + place);
                    arrayList.add(place);
                }
                catch(Exception e){
                    Log.d("EXCEPTION", "findPlaces - for");
                }
            }
            return arrayList;
        }
        catch(JSONException ex){
            Log.d("EXCEPTION","findPlaces - json");
        }
        return null;
    }

    private String makeURL(double latitude, double longitude, String place){
        String searchType="search";
        StringBuilder urlString =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/" + searchType + "/json?");
        if (place.equals("")) {
            //urlString.append("&query=93202fd6eb9d16e7fa1f7831521b2ec363c8688f");
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=100");
            urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
            //urlString.append("&key=" + API_KEY);
            Log.d("PLACESERVICE","No Place");
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=100");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false");
            urlString.append("&key=" + API_KEY);
            Log.d("PLACESERVICE","PLACE");
        }
        Log.d("URL",urlString.toString());
        return urlString.toString();
    }

    protected String getJSON(String url){
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
