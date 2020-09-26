package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

public class CustomerProfileActivity extends AppCompatActivity {

    TextView pname,pemail,ppassword,pnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);


        //View cview = getLayoutInflater().inflate(R.layout.profile_add_item,null);

        pname = findViewById(R.id.Profilename);
        pemail = findViewById(R.id.ProfileEmail);
        ppassword = findViewById(R.id.ProfilePassword);
        pnumber = findViewById(R.id.ProfileMobileNumber);

        fillCustomerProfile();
    }

    public void fillCustomerProfile(){

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject objData = obj.getJSONObject("Data");
                            pname.setText(objData.getString("UserName"));
                            pemail.setText(objData.getString("UserEmail"));
                            pnumber.setText(objData.getString("UserMobile"));
                            //ppassword.setText(objData.getString(""));
                            loadingDialog.dismissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}