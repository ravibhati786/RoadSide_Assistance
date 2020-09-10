package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MechanicRegistration extends AppCompatActivity implements View.OnClickListener {

    ImageView top_curve;
    EditText name,shop_name, email, password,mobnumber,address;
    TextView name_text,shop_text, email_text, password_text,mobile_text , login_title,address_text;
    Button buttonMachanicRegister;
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private String mobilePattern = "[0-9]{10}";
    private ProgressDialog progressDialog;
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

        logo = findViewById(R.id.logo);
        login_title = findViewById(R.id.registration_title);
        new_user =findViewById(R.id.new_user_text);
        buttonMachanicRegister = findViewById(R.id.register_button);
        //already_have_account_layout = findViewById(R.id.already_have_account_text);
        register_card = findViewById(R.id.register_card);

        progressDialog = new ProgressDialog(this);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        shop_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mobnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        buttonMachanicRegister.setOnClickListener(this);




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

    private void registerMechanic() {

        final String mechanic_Name = name.getText().toString().trim();
        final String mechanic_ShopName = shop_name.getText().toString().trim();
        final String mechanic_email = email.getText().toString().trim();
        final String mechanic_password = password.getText().toString().trim();
        final String mechanic_MobileNumber = mobnumber.getText().toString().trim();


        progressDialog.setMessage("Registering Mechanic.....");
        progressDialog.show();
        buttonMachanicRegister.setEnabled(false);
        buttonMachanicRegister.setTextColor(Color.argb(50,255,233,255));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MECHANIC_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    //if there will not be any error this method     executed
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    //for the error this method will be executed
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        buttonMachanicRegister.setEnabled(true);
                        buttonMachanicRegister.setTextColor(Color.rgb(255,255,255));
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        )//here we are override a method
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("AssistantName",mechanic_Name);
                params.put("AssitantShopName",mechanic_ShopName);
                params.put("AsistantEmail",mechanic_email);
                params.put("AssistantPassword",mechanic_password);
                params.put("AssistantMobile",mechanic_MobileNumber);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    @Override
    public void onClick(View view) {
        if(view == buttonMachanicRegister){
            checkEmailAndMobile();
        }
    }
    private void checkEmailAndMobile(){
        if(email.getText().toString().matches(emailPattern)){
            if(mobnumber.getText().toString().matches((mobilePattern))){
                registerMechanic();
            }else {
                mobnumber.setError("Invalid Mobile number");
            }
        }else{
            email.setError("Invalid Email");
        }
    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(shop_name.getText())) {
                if (!TextUtils.isEmpty(email.getText())) {
                    if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                        if (!TextUtils.isEmpty(mobnumber.getText())) {
                            buttonMachanicRegister.setEnabled(true);
                            buttonMachanicRegister.setTextColor(Color.rgb(255, 255, 255));
                        }else {
                            buttonMachanicRegister.setEnabled(false);
                            buttonMachanicRegister.setTextColor(Color.argb(50, 255, 233, 255));
                        }
                    } else {
                        buttonMachanicRegister.setEnabled(false);
                        buttonMachanicRegister.setTextColor(Color.argb(50, 255, 233, 255));
                    }
                } else {
                    buttonMachanicRegister.setEnabled(false);
                    buttonMachanicRegister.setTextColor(Color.argb(50, 255, 233, 255));
                }
            }else{
                buttonMachanicRegister.setEnabled(false);
                buttonMachanicRegister.setTextColor(Color.argb(50, 255, 233, 255));
            }
        }else{
            buttonMachanicRegister.setEnabled(false);
            buttonMachanicRegister.setTextColor(Color.argb(50,255,233,255));
        }

    }
}