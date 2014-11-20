package map.teamc.com.maplocationproject;

/**
 * Created by alex64 on 09/11/2014.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class DetailFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";
    TextView text;

    public DetailFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkin_fragment, container, false);
        //String menu = getArguments().getString("Menu");
        //text= (TextView) view.findViewById(R.id.check_in);
        //text.setText(menu);

        return view;
    }
}
