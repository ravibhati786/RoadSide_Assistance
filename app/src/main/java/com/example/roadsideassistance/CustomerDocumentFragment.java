package com.example.roadsideassistance;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CustomerDocumentFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_document, container, false);



        //CardView cardView = v.findViewById(R.id.cvcustomermap);
        //Button button = v.findViewById(R.id.newservicerequest);
       // button.setVisibility(v.INVISIBLE);
        return v;
    }
}