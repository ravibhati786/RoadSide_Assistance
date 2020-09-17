package com.example.roadsideassistance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RequestMechanicFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_mechanic, container, false);

      Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        //activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setSubtitle("Request Mechanic");
        return view;

    }

    public boolean onSupportNavigateUp() {
        getActivity().onBackPressed();
        return true;
    }

}