package com.example.roadsideassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MechanicRecentDetails extends AppCompatActivity {

TextView name,slocation,vname,sname,servicestatus;
ListView listView;
List<HistoryPojoClass> historyPojoClassList;
AdapterRecentDetailsListView adapterRecentDetailsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_recent_details);

        View mview = getLayoutInflater().inflate(R.layout.recent_details_add,null);

        historyPojoClassList = new ArrayList<>();
        name = mview.findViewById(R.id.name);
        slocation = mview.findViewById(R.id.custservicelocation);
        vname = mview.findViewById(R.id.custvehicle);
        sname = mview.findViewById(R.id.custservice);
        servicestatus = mview.findViewById(R.id.custservicestatus);
        listView = findViewById(R.id.mechrecentdetailslistview);

        fillHistoryServicesListView();

    }

    public void fillHistoryServicesListView(){

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_SERVICE_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            historyPojoClassList.clear();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray historyArray = obj.getJSONArray("Data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < historyArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject historyObject = historyArray.getJSONObject(i);

                                //creating a vehicle object and giving them the values from json object
                                HistoryPojoClass historyPojoClass = new HistoryPojoClass(historyObject.getString("UserName"),
                                        historyObject.getString("VehicleName"),
                                        historyObject.getString("ServiceName"),
                                        historyObject.getString("ServiceStatus"),
                                        historyObject.getString("ServiceLocation"));


                                //adding the hero to vehicleList
                                historyPojoClassList.add(historyPojoClass);
                            }

                            //creating custom adapter object
                            adapterRecentDetailsListView = new AdapterRecentDetailsListView(historyPojoClassList, getApplicationContext());

                            //adding the adapter to listview
                            listView.setAdapter(adapterRecentDetailsListView);
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
                params.put("AssistantId", String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }



}