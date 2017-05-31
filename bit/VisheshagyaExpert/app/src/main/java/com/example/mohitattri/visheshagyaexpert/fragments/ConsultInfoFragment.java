package com.example.mohitattri.visheshagyaexpert.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohitattri.visheshagyaexpert.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultInfoFragment extends Fragment {


    public ConsultInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consult_info, container, false);
    }

}
