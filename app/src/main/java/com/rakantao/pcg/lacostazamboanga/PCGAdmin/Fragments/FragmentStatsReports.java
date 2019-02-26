package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rakantao.pcg.lacostazamboanga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatsReports extends Fragment {

    View view;

    public FragmentStatsReports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_statsreports, container, false);



        return view;
    }

}
