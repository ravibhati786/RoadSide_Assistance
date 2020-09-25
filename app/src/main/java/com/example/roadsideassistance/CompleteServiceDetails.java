package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import android.location.LocationManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;
import java.util.HashMap;
import java.util.Map;

public class CompleteServiceDetails extends AppCompatActivity {
    TextView customerName,mechanicName, serviceName, vehicleName, serviceLocation;
    EditText serviceStatus;
    Button btnSaveServiceDetails;
    String userId,userEmail,serviceId,vehicleId,serviceLocationLatitude,serviceLocationLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_service_details);
        customerName = findViewById(R.id.txtCustomerName);
        mechanicName = findViewById(R.id.txtMechanicName);
        serviceName = findViewById(R.id.txtServiceName);
        vehicleName = findViewById(R.id.txtVehicleName);
        serviceLocation = findViewById(R.id.txtServiceLocation);
        serviceStatus = findViewById(R.id.etServiceStatus);
        btnSaveServiceDetails = findViewById(R.id.btnServiceDetailsHistory);

        Bundle extra = getIntent().getExtras();
        userEmail = extra.getString("userEmail");
        serviceId = extra.getString("serviceId");
        vehicleId = extra.getString("vehicleId");
        serviceLocationLatitude = extra.getString("ServiceLocationLatitude");
        serviceLocationLongitude = extra.getString("ServiceLocationLongitude");
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(Double.parseDouble(serviceLocationLatitude),Double.parseDouble(serviceLocationLongitude),getApplicationContext(),new GeocoderHandler());

        fillServiceDetails();
        btnSaveServiceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveServiceHistory();
            }
        });
    }

    private void saveServiceHistory() {
        Toast.makeText(this, "Hello Button Click", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SAVE_SERVICE_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("Success"))
                            {
                                Toast.makeText(CompleteServiceDetails.this, "Service Completed Successfully!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId",userId);
                params.put("AssistantId",String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));
                params.put("VehicleId",vehicleId);
                params.put("ServiceLocation",serviceLocationLatitude+","+serviceLocationLongitude);
                params.put("ServiceId",serviceId);
                params.put("ServiceStatus",serviceStatus.getText().toString());
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void fillServiceDetails(){
        Toast.makeText(this, "In the fillServiceDetailsBlock", Toast.LENGTH_SHORT).show();
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ServiceNameVehicleNameUserName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("Response",response);
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("Success"))
                            {
                                Toast.makeText(CompleteServiceDetails.this, "Success block", Toast.LENGTH_SHORT).show();
                                JSONObject dataObject = obj.getJSONObject("Data");
                                customerName.setText(dataObject.getString("UserName"));
                                serviceName.setText(dataObject.getString("ServiceName"));
                                vehicleName.setText(dataObject.getString("VehicleName"));
                                mechanicName.setText(new SharedPrefManager(getApplicationContext()).getLoggedName());
                                userId = dataObject.getString("UserId");
                                loadingDialog.dismissDialog();
                            }
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
            {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserEmail",userEmail);
                params.put("VehicleId",vehicleId);
                params.put("ServiceId",serviceId);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            serviceLocation.setText(locationAddress);
        }
    }

}