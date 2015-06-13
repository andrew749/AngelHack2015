package com.krimea.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.krimea.R;

/**
 * Created by Marwan on 15-06-13.
 */
public class MapFragment extends Fragment {

    static MapFragment newInstance() {
        MapFragment frag = new MapFragment();
        return(frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_map, container, false);

        return(result);
    }

    private void setUpMap() {

    }
}
