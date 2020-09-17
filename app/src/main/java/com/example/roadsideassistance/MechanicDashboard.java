package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class MechanicDashboard extends AppCompatActivity {

GridLayout mechanicDashboardGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dashboard);


        mechanicDashboardGrid = findViewById(R.id.mechdeshgridlayout);

        setSingleEvent(mechanicDashboardGrid);

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
                        
                    }

                }
            });


        }

    }
}