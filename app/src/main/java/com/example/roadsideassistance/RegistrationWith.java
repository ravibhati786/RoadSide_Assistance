package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegistrationWith extends AppCompatActivity {

    ImageView top_curve;
    TextView logo,login_title;
    LinearLayout new_user_layout,already_have_account_text;
    CardView login_card,login_card1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_with);


        top_curve = findViewById(R.id.top_curve);
        login_title = findViewById(R.id.login_text);
        logo = findViewById(R.id.logo);
        new_user_layout = findViewById(R.id.new_user_text);
        already_have_account_text = findViewById(R.id.already_have_account_text);
        login_card = findViewById(R.id.login_card);
        login_card1 = findViewById(R.id.login_card1);


        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        logo.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        login_card.startAnimation(center_reveal_anim);
        login_card1.startAnimation(center_reveal_anim);
        already_have_account_text.startAnimation(center_reveal_anim);

    }



    public void btncustomer(View view){

        final LoadingDialog loadingDialog = new LoadingDialog(RegistrationWith.this);

        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadingDialog.dismissDialog();
            }
        },5000);
        startActivity(new Intent(this, CustomerRegistration.class));
    }

    public void btnmechanic(View view) {

        final LoadingDialog loadingDialog = new LoadingDialog(RegistrationWith.this);

        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadingDialog.dismissDialog();
            }
        },5000);

        startActivity(new Intent(this,MechanicRegistration.class));
    }


    public void login (View view){

        startActivity( new Intent(this,Login.class));
    }

}

