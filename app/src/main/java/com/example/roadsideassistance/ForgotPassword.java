package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final CardView cardView = findViewById(R.id.cv);
        final CardView cardView1 = findViewById(R.id.cv1);

        final Button button = findViewById(R.id.sendotp);
        final Button button1 = findViewById(R.id.verifyotp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                button1.setVisibility(View.VISIBLE);
            }
        });
    }



}