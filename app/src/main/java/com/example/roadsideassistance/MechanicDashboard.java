package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MechanicDashboard extends AppCompatActivity {

    GridLayout mechanicDashboardGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashboard);


        mechanicDashboardGrid = findViewById(R.id.mechdeshgridlayout);

        setSingleEvent(mechanicDashboardGrid);

        ImageView logOut = findViewById(R.id.mechanicSignOut);
        TextView txtMechanicName = findViewById(R.id.txtMechanicName);
        txtMechanicName.setText(new SharedPrefManager(getApplicationContext()).getLoggedName());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MechanicDashboard.this,Login.class));
                finish();
            }
        });

    }

    private void setSingleEvent(GridLayout mechanicDashboardGrid) {

        for(int i = 0;i<mechanicDashboardGrid.getChildCount();i++){

            CardView cardView = (CardView) mechanicDashboardGrid.getChildAt(i);
            final int item = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    switch (item){
                        case 0 :Intent intent =  new Intent(MechanicDashboard.this,MechanicMapActivity.class);
                            startActivity(intent);
                            break;
                        case 1 : Intent intent1 =  new Intent(MechanicDashboard.this,MechanicProfileActivity.class);
                            startActivity(intent1);
                            break;
                        case 2 :Intent intent2 =  new Intent(MechanicDashboard.this,MechanicDocumentActivity.class);
                            startActivity(intent2);
                            break;
                        case 3 :Intent intent3 =  new Intent(MechanicDashboard.this,MechanicRecentDetails.class);
                            startActivity(intent3);
                            break;


                        case 4 :Intent intent4 =  new Intent(MechanicDashboard.this,MechanicVehicleActivity.class);
                            startActivity(intent4);
                            break;
                    }

                }
            });


        }

    }


}