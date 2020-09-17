package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomSheet extends BottomSheetDialogFragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_diolog,container,false);

        Button requestbtn = v.findViewById(R.id.requestbtn);


        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout,new RequestMechanicFragment());
                fragmentTransaction.addToBackStack(null).commit();


dismiss();
            }
        });

        Spinner spinnerservice = v.findViewById(R.id.spinnerservicerquest);
        String[] value = {"Select service","Towing service","Accidental towing","Battery Jumpstart","Flat tire","Minor on-site repair","Lost or locked key","Fuel problem","Medical coordination.","24x7 roadside assistance","Taxi services"};
        ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.style_spinner,arrayList);
        spinnerservice.setAdapter(arrayAdapter);

        Spinner spinnervehicle = v.findViewById(R.id.spinnervehicle);
        String[] value2 = {"Select vehicle","Royal Enfield Classic 350","Honda Activa 6G","Bajaj Pulsar","TVS Apache RTR 160"};
        ArrayList<String> arrayList2 = new  ArrayList<>(Arrays.asList(value2));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(),R.layout.style_spinner,arrayList2);
        spinnervehicle.setAdapter(arrayAdapter2);



        return v;
    }
}
