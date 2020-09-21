package com.example.roadsideassistance;

import android.content.Intent;
import android.location.Location;
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

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomSheet extends BottomSheetDialogFragment {

    private LatLng pickupLocation;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_diolog,container,false);

        Button requestbtn = v.findViewById(R.id.bottmsheetrequestbtn);


        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid = FirebaseAuth.getInstance().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequests");
                GeoFire geoFire = new GeoFire(ref);
                Bundle mArgs = getArguments();
                Location mlastLocation = mArgs.getParcelable("mlastlocation");
                geoFire.setLocation(uid, new GeoLocation(mlastLocation.getLatitude(), mlastLocation.getLongitude()));

                pickupLocation = new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude());


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
