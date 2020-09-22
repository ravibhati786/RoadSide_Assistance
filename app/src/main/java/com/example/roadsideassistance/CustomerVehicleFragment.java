package com.example.roadsideassistance;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CustomerVehicleFragment extends Fragment {

    FloatingActionButton btnfab;
    AlertDialog dialog;
    ListView listView;

    String[] VehicleName = {"Scooty"};
    String[] VehicleNumber = {"RJ24AB1208"};
    String[] VehicleModel ={"Honda Activa 6G"};
    String[] VehicleMake = {"Make"};

    Integer[] VehicleImage =  {R.drawable.profileimg};


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_customer_vehicle, container, false);



        FloatingActionButton btnfab = v.findViewById(R.id.mechvehiclefabbtn);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Relace with your own action", Toast.LENGTH_SHORT).show();

                UploadVehicleMechanic();
            }
        });

        listView = v.findViewById(R.id.custvehiclelistview);

        AdapterMechanicVehicleListView adapter = new AdapterMechanicVehicleListView(getActivity(),VehicleName,VehicleNumber,VehicleModel,VehicleMake,VehicleImage);
        listView.setAdapter(adapter);




   return v;
    }

    private void UploadVehicleMechanic() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.custome_mechanic_vehicle_add,null);

        // Spinner mspinner = mview.findViewById(R.id.spinnerdoctype);
        //String[] value = {"Select document type","Pan Card","Adhar Card"," Driving Licence","Registration Certificate(RC)"};
        //ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.style_spinner,arrayList);
        // mspinner.setAdapter(arrayAdapter);

        mBuilder.setView(mview);
        dialog = mBuilder.create();
        dialog.show();



    }



}