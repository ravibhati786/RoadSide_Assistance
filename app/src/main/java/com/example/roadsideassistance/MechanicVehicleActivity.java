package com.example.roadsideassistance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MechanicVehicleActivity extends AppCompatActivity {

    FloatingActionButton btnfab;
    AlertDialog dialog;
    ListView listView;

    String[] VehicleName = {"Scooty"};
    String[] VehicleNumber = {"RJ24AB1208"};
    String[] VehicleModel ={"Honda Activa 6G"};
    String[] VehicleMake = {"Make"};

    Integer[] VehicleImage =  {R.drawable.profileimg};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_vehicle);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Document");


        FloatingActionButton btnfab = findViewById(R.id.mechvehiclefabbtn);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Relace with your own action", Toast.LENGTH_SHORT).show();

                UploadVehicleMechanic();
            }
        });

           listView = findViewById(R.id.mechvehiclelistview);

           AdapterMechanicVehicleListView adapter = new AdapterMechanicVehicleListView(this,VehicleName,VehicleNumber,VehicleModel,VehicleMake,VehicleImage);
           listView.setAdapter(adapter);




    }

    private void UploadVehicleMechanic() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
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

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}