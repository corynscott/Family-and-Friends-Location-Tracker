package map.teamc.com.maplocationproject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by alex64 on 06/11/2014.
 */
public class Place  {

    private String id;
    private String icon;
    private String name;
    private String vicinity;
    private Double latitude;
    private Double longitude;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

     static Place jsonReference(JSONObject reference){
         Place result =  new Place();
        try{

            JSONObject geometry = (JSONObject) reference.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatitude((Double) location.get("lat"));
            Log.d("lat", "" + result.getLatitude());
            result.setLongitude((Double) location.get("lng"));
            Log.d("long", "" + result.getLongitude());
            result.setIcon(reference.getString("icon"));
            Log.d("icon", "" + result.getIcon());
            result.setName(reference.getString("name"));
            Log.d("name", "" + result.getName());
            if(reference.getString("vicinity") != null){
                result.setVicinity(reference.getString("vicinity"));
                Log.d("vicinity", "" + result.getVicinity());
            }
            if(reference.getString("id") != null){
                result.setId(reference.getString("id"));
                Log.d("id", "" + result.getId());
            }
        }
        catch(JSONException ex){
            Log.d("PLACES_ERRROR", "ERROR IN PLACES");
        }
        return result;
    }

    @Override
    public String toString(){
        return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
}
