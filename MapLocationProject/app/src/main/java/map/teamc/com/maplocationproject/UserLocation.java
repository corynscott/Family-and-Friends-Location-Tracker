package map.teamc.com.maplocationproject;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex64 on 30/10/2014.
 */
public class UserLocation {
    private String uli;
    private String username;
    private String date;
    private double longitude;
    private double latitude;

    public UserLocation(String username, String time, double longitude, double latitude){
        this.uli = generateULI(time);
        this.username = username;
        this.date = stringDate(time);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public UserLocation(String uli, String username, String date, double longitude, double latitude){
        this.uli = uli;
        this.username = username;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setULI(String uli){
        this.uli = uli;
    }

    public String getULI(){
        return uli;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public String generateULI(String time){
        Date d = new Date(Long.parseLong(time));
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int month, day, hour, minute,second;
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        return "" + cal.get(Calendar.YEAR) +
                ((month < 10) ?  "0" + month :   month) +
                ((day < 10) ?  "0" + day :   day) +
                ((hour < 10) ?  "0" + hour :   hour) +
                ((minute < 10) ?  "0" + minute :   minute) +
                ((second < 10) ?  "0" + second :   second);
    }

    public String stringDate(String time){
        Date d = new Date(Long.parseLong(time));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:sss'Z'");
        String date = dateFormat.format(d);
        Log.d("Date", date);
        return date;
    }
}
