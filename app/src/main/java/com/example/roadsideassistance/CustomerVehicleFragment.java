package com.example.roadsideassistance;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerVehicleFragment extends Fragment {

    FloatingActionButton btnfab;
    AlertDialog dialog;
    ListView listView;

    EditText vName,vNumber,vModel,vMake;
    AdapterCustomerVehicleListView vehicleAdapter;
    List<Vehicle> vehicleList;
    TextView txtMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_vehicle, container, false);

        FloatingActionButton btnfab = v.findViewById(R.id.custvehiclefabbtn);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Relace with your own action", Toast.LENGTH_SHORT).show();

                UploadVehicleCustomer();
            }
        });
        txtMessage = v.findViewById(R.id.txtMessage);
        vehicleList = new ArrayList<>();

        listView = v.findViewById(R.id.custvehiclelistview);

        fillListView();


    return v;
    }

    private void UploadVehicleCustomer() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.custom_customer_vehicle_add,null);

        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        // Spinner mspinner = mview.findViewById(R.id.spinnerdoctype);
        //String[] value = {"Select document type","Pan Card","Adhar Card"," Driving Licence","Registration Certificate(RC)"};
        //ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.style_spinner,arrayList);
        // mspinner.setAdapter(arrayAdapter);

        mBuilder.setView(mview);
        dialog = mBuilder.create();
        dialog.show();
        vName = mview.findViewById(R.id.edittext_vehicleName);
        vNumber = mview.findViewById(R.id.edittext_vehicleNumber);
        vModel = mview.findViewById(R.id.edittext_vehicleModel);
        vMake = mview.findViewById(R.id.edittext_vehicleMMake);

        mview.findViewById(R.id.uploadbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,Constants.URL_SAVE_VEHICLE,new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                            Toast.makeText(getContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            fillListView();
                            dialog.dismiss();
                            loadingDialog.dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.i("error",error.toString());
                                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("VehicleNumber", vNumber.getText().toString());
                        params.put("VehicleName", vName.getText().toString());
                        params.put("VehicleModel", vModel.getText().toString());
                        params.put("VehicleMake", vMake.getText().toString());
                        params.put("UserId", String.valueOf(new SharedPrefManager(getContext()).getLoggedUserId()));
                        Log.i("param",params.toString());
                        return params;
                    }
                };
                RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

            }
        });



    }

    private void fillListView() {
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_VEHICLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            vehicleList.clear();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray documentArray = obj.getJSONArray("Data");

                            if (documentArray.length() > 0) {
                                txtMessage.setVisibility(View.GONE);

                            }

                            //now looping through all the elements of the json array
                            for (int i = 0; i < documentArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject vehicleObject = documentArray.getJSONObject(i);

                                //creating a vehicle object and giving them the values from json object
                                Vehicle vehicle = new Vehicle(vehicleObject.getString("VehicleId"),
                                        vehicleObject.getString("VehicleNumber"),
                                        vehicleObject.getString("VehicleName"),
                                        vehicleObject.getString("VehicleModel"),
                                        vehicleObject.getString("VehicleMake"));


                                //adding the hero to vehicleList
                                vehicleList.add(vehicle);
                            }

                            //creating custom adapter object
                            vehicleAdapter = new AdapterCustomerVehicleListView(vehicleList, getContext());

                            //adding the adapter to listview
                            listView.setAdapter(vehicleAdapter);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", String.valueOf(new SharedPrefManager(getContext()).getLoggedUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}