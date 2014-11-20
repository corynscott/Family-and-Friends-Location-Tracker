package map.teamc.com.maplocationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by alex64 on 03/11/2014.
 */
public class JsonAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JsonAdapter(Context context, LayoutInflater inflater){
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.result_get, null);

            holder = new ViewHolder();
            holder.usernameView = (TextView)convertView.findViewById(R.id.username);
            holder.latitudeView = (TextView)convertView.findViewById(R.id.latitude);
            holder.longitudeView = (TextView)convertView.findViewById(R.id.longitude);
            holder.dateView = (TextView)convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = (JSONObject) getItem(position);
        String username = "";
        String latitude = "";
        String longitude = "";
        String date = "";

        //username needs @
        if(jsonObject.has("username")){
            username = jsonObject.optString("username");
        }

        if(jsonObject.has("latitude")){
            latitude = jsonObject.optString("latitude");
        }

        if(jsonObject.has("longitude")){
            longitude = jsonObject.optString("longitude");
        }

        if(jsonObject.has("time")){
            date = jsonObject.optString("time");
        }

        holder.usernameView.setText(username);
        holder.latitudeView.setText(latitude);
        holder.longitudeView.setText(longitude);
        holder.dateView.setText(date);

        return convertView;
    }

    private static class ViewHolder{
        public TextView usernameView;
        public TextView latitudeView;
        public TextView longitudeView;
        public TextView dateView;
    }
}
