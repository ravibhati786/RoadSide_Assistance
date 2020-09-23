package com.example.roadsideassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MechanicLogin extends AppCompatActivity {

    EditText etEmail, etPassword;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    Button loginMechanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_login);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        loginMechanic = findViewById(R.id.mechanic_login_button);
        mAuth = FirebaseAuth.getInstance();



        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(MechanicLogin.this,MechanicDashboard.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        loginMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MechanicLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MechanicLogin.this, "Sign In Mechanic Problem", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_LOGIN,new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        if(obj.getBoolean("Success")){
                                            JSONObject objData = obj.getJSONObject("Data");
                                            if(objData.getString("Table").equals("AssistanceMaster")){
                                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                                        objData.getInt("AssistantId"),
                                                        objData.getString("AssistantName"),
                                                        objData.getString("AssistantEmail")
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
                                    params.put("UserEmail", email);
                                    params.put("UserPassword",password);
                                    Log.i("param",params.toString());
                                    return params;
                                }
                            };
                            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                        }
                    }
                });
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
}