package com.example.roadsideassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegistration extends AppCompatActivity implements View.OnClickListener {


    ImageView top_curve;
    private EditText name, email, password, mobnumber;
    private Button buttonUserRegister;
    private ProgressDialog progressDialog;
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private String mobilePattern = "[0-9]{10}";
    TextView name_text, email_text, password_text,mobile_text , login_title;
    TextView logo;
    LinearLayout already_have_account_layout;
    CardView register_card;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        mAuth = FirebaseAuth.getInstance();
        //*programming code
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        mobnumber = (EditText)findViewById(R.id.mobnumber);

        buttonUserRegister = (Button)findViewById(R.id.register_button);

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

        buttonUserRegister.setOnClickListener(this);


        //*






        top_curve = findViewById(R.id.top_curve);
        name = findViewById(R.id.name);
        name_text = findViewById(R.id.name_text);
        email = findViewById(R.id.email);
        email_text = findViewById(R.id.email_text);
        password = findViewById(R.id.password);
        password_text = findViewById(R.id.password_text);
        mobile_text = findViewById(R.id.mobnumber_text);
        mobnumber = findViewById(R.id.mobnumber);
        logo = findViewById(R.id.logo);
        login_title = findViewById(R.id.registration_title);
        already_have_account_layout = findViewById(R.id.already_have_account_text);
        register_card = findViewById(R.id.register_card);


        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edittext_anim);
        name.startAnimation(editText_anim);
        email.startAnimation(editText_anim);
        password.startAnimation(editText_anim);
        mobnumber.startAnimation(editText_anim);

        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        name_text.startAnimation(field_name_anim);
        email_text.startAnimation(field_name_anim);
        password_text.startAnimation(field_name_anim);
        mobile_text.startAnimation(field_name_anim);
        logo.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        register_card.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);
        

    }

    public void login(View view) {
        finish();
    }
    public void registerButton(View view) {


        final LoadingDialog loadingDialog = new LoadingDialog(CustomerRegistration.this);

        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadingDialog.dismissDialog();
            }
        },5000);

        startActivity(new Intent(this,OtpVerification.class));

    }

    private void registerUser() {

        final String user_Name = name.getText().toString().trim();
        final String user_Email = email.getText().toString().trim();
        final String user_Password = password.getText().toString().trim();
        final String user_MobileNumber = mobnumber.getText().toString().trim();

        progressDialog.setMessage("Registering user.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    //if there will not be any error this method     executed
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("Success"))
                            {
                                Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent(this,)
                                mAuth.createUserWithEmailAndPassword(user_Email, user_Password)
                                        .addOnCompleteListener(CustomerRegistration.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(CustomerRegistration.this, "Registration Failed!", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    String uid = mAuth.getCurrentUser().getUid();
                                                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(uid);
                                                    Log.i("check",current_user_db.toString());
                                                    current_user_db.setValue(true);
                                                }
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(CustomerRegistration.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

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
                        buttonUserRegister.setEnabled(true);
                        buttonUserRegister.setTextColor(Color.rgb(255,255,255));
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        )//here we are override a method
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("UserName",user_Name);
                params.put("UserEmail",user_Email);
                params.put("UserPassword",user_Password);
                params.put("UserMobile",user_MobileNumber);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    //
    @Override
    public void onClick(View view) {
        if(view == buttonUserRegister) {
            checkEmailAndMobile();

        }
    }
    private void checkEmailAndMobile(){
        if(email.getText().toString().matches(emailPattern)){
            if(mobnumber.getText().toString().matches((mobilePattern))){
                registerUser();
                
            }else {
                mobnumber.setError("Invalid Mobile number");
            }
        }else{
            email.setError("Invalid Email");
        }
    }
    private void checkInputs(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(email.getText())){
                if(!TextUtils.isEmpty(password.getText()) && password.length() >= 8 ){
                    if(!TextUtils.isEmpty(mobnumber.getText())){
                            buttonUserRegister.setEnabled(true);
                            buttonUserRegister.setTextColor(Color.rgb(255,255,255));
                    }else{
                        buttonUserRegister.setEnabled(false);
                        buttonUserRegister.setTextColor(Color.argb(50,255,233,255));
                    }
                }else{
                    buttonUserRegister.setEnabled(false);
                    buttonUserRegister.setTextColor(Color.argb(50,255,233,255));
                }
            }else{
                buttonUserRegister.setEnabled(false);
                buttonUserRegister.setTextColor(Color.argb(50,255,233,255));
            }
        }else{
                buttonUserRegister.setEnabled(false);
                buttonUserRegister.setTextColor(Color.argb(50,255,233,255));
        }

    }
}