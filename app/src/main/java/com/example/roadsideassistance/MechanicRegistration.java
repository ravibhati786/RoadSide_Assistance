package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MechanicRegistration extends AppCompatActivity {

    ImageView top_curve;
    EditText name,shop_name, email, password,mobnumber,address;
    TextView name_text,shop_text, email_text, password_text,mobile_text , login_title,address_text;
    TextView logo;
    LinearLayout new_user;
    //LinearLayout already_have_account_layout;
    CardView register_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_registration);

        top_curve = findViewById(R.id.top_curve);
        name = findViewById(R.id.name);
        name_text = findViewById(R.id.name_text);
        shop_name = findViewById(R.id.shopname);
        shop_text = findViewById(R.id.shop_text);
        email = findViewById(R.id.email);
        email_text = findViewById(R.id.email_text);
        password = findViewById(R.id.password);
        password_text = findViewById(R.id.password_text);
        mobile_text = findViewById(R.id.mobnumber_text);
        mobnumber = findViewById(R.id.mobnumber);
        address  = findViewById(R.id.address_mechanic);
        address_text = findViewById(R.id.address_text);
        logo = findViewById(R.id.logo);
        login_title = findViewById(R.id.registration_title);
        new_user =findViewById(R.id.new_user_text);
        //already_have_account_layout = findViewById(R.id.already_have_account_text);
        register_card = findViewById(R.id.register_card);



        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        //name_text.startAnimation(field_name_anim);
        //shop_text.startAnimation(field_name_anim);
       // email_text.startAnimation(field_name_anim);
        //password_text.startAnimation(field_name_anim);
        //mobile_text.startAnimation(field_name_anim);
        //address_text.startAnimation(field_name_anim);
        logo.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        register_card.startAnimation(center_reveal_anim);
        new_user.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);

        //already_have_account_layout.startAnimation(new_user_anim);


    }

    public void btnmechanic(View view){
        startActivity(new Intent(this,MechanicMapActivity.class));
    }
}