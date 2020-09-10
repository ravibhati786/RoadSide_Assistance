package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    ImageView top_curve;
    EditText email,password;
    TextView email_text, password_text, login_title;
    TextView logo;
    LinearLayout new_user_layout;
    CardView login_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        top_curve = findViewById(R.id.top_curve);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);
        login_title = findViewById(R.id.login_text);
        logo = findViewById(R.id.logo);
        new_user_layout = findViewById(R.id.new_user_text);
        login_card = findViewById(R.id.login_card);




        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edittext_anim);
        email.startAnimation(editText_anim);
        password.startAnimation(editText_anim);

        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        email_text.startAnimation(field_name_anim);
        password_text.startAnimation(field_name_anim);
        logo.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        login_card.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);
        new_user_layout.startAnimation(new_user_anim);



    }



public void loginbtn(View view){

    final LoadingDialog loadingDialog = new LoadingDialog(Login.this);

    loadingDialog.startLoadingDialog();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {

            loadingDialog.dismissDialog();
        }
    },5000);

    startActivity(new Intent(this, MainActivity.class));
}

    public void register(View view) {
        startActivity(new Intent(this,RegistrationWith.class));
    }


}