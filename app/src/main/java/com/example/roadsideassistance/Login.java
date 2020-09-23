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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    ImageView top_curve;
    EditText editTextemail,editTextpassword;
    TextView email_text, password_text, login_title;
    Button login_button;
    TextView logo;
    LinearLayout new_user_layout;
    CardView login_card;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        top_curve = findViewById(R.id.top_curve);
        editTextemail = findViewById(R.id.email);
        editTextpassword = findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.login_button);
        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);
        login_title = findViewById(R.id.login_text);
        logo = findViewById(R.id.logo);
        new_user_layout = findViewById(R.id.new_user_text);
        login_card = findViewById(R.id.login_card);

        mAuth = FirebaseAuth.getInstance();


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Log.i("UserValue",user.toString());
                if(user!=null){
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    reference.child("Customers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(user.getUid())){
                                Intent intent = new Intent(Login.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                            else
                            {
                                reference.child("Mechanics").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(user.getUid())){
                                            Intent intent = new Intent(Login.this,MechanicDashboard.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                        else{
                                            Toast.makeText(Login.this, "User doesn't exist!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        };

        editTextemail.addTextChangedListener(new TextWatcher() {
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
        editTextpassword.addTextChangedListener(new TextWatcher() {
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
        login_button.setOnClickListener(this);




        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edittext_anim);
        editTextemail.startAnimation(editText_anim);
        editTextpassword.startAnimation(editText_anim);

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



    public void login(){
        final String user_Email = editTextemail.getText().toString().trim();
        final String user_Password = editTextpassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(user_Email,user_Password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(Login.this, "Sing In Error", Toast.LENGTH_SHORT).show();
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_LOGIN,new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);

                                if(obj.getBoolean("Success")){
                                    JSONObject objData = obj.getJSONObject("Data");
                                    if(objData.getString("Table").equals("UserMaster")){
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                                objData.getInt("UserId"),
                                                objData.getString("UserName"),
                                                objData.getString("UserEmail")
                                        );
                                        Toast.makeText(getApplicationContext(),"User login successful",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.i("error",error.toString());
                                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("UserEmail", user_Email);
                            params.put("UserPassword",user_Password);
                            Log.i("param",params.toString());
                            return params;
                        }
                    };
                    RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });

    }

    public void loginbtn(View view){

        final LoadingDialog loadingDialog = new LoadingDialog(Login.this);
        login();
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadingDialog.dismissDialog();
            }
        },5000);


        //startActivity(new Intent(this, CustomerMapActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this,RegistrationWith.class));
    }

    public void checkInputs()
    {
        if(!TextUtils.isEmpty(editTextemail.getText())){
            if(!TextUtils.isEmpty((editTextpassword.getText()))){
                login_button.setEnabled(true);
                login_button.setTextColor(Color.rgb(255,255,255));
            }else{
                login_button.setEnabled(false);
                login_button.setTextColor(Color.argb(50,255,233,255));
            }
        }else{
            login_button.setEnabled(false);
            login_button.setTextColor(Color.argb(50,255,233,255));
        }
    }
    public void ForgotPassword(View view){
        startActivity(new Intent(this,ForgotPassword.class));
    }

    public  void MechanicLogin(View view){
        startActivity(new Intent(this,MechanicLogin.class));
    }

    @Override
    public void onClick(View view) {
        if(view == login_button)
            login();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}