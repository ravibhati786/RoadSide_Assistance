package com.example.roadsideassistance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CustomerServiceFragment extends Fragment {

    TextView name,slocation,vname,sname,servicestatus;
    ListView listView;
    List<HistoryPojoClass> historyPojoClassList;
    AdapterRecentDetailsListView adapterRecentDetailsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_customer_service, container, false);


        historyPojoClassList = new ArrayList<>();
        listView = v.findViewById(R.id.custrecentdetailslistview);

        View mview = getLayoutInflater().inflate(R.layout.recent_details_add,null);

        name = mview.findViewById(R.id.name);
        slocation = mview.findViewById(R.id.custservicelocation);
        vname = mview.findViewById(R.id.custvehicle);
        sname = mview.findViewById(R.id.custservice);
        servicestatus = mview.findViewById(R.id.custservicestatus);

        fillHistoryServicesListView();

        return v;
    }


    public void fillHistoryServicesListView(){

        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
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
                                HistoryPojoClass historyPojoClass = new HistoryPojoClass(historyObject.getString("AssistantName"),
                                        historyObject.getString("VehicleName"),
                                        historyObject.getString("ServiceName"),
                                        historyObject.getString("ServiceStatus"),
                                        historyObject.getString("ServiceLocation"));


                                //adding the hero to vehicleList
                                historyPojoClassList.add(historyPojoClass);
                            }

                            //creating custom adapter object
                            adapterRecentDetailsListView = new AdapterRecentDetailsListView(historyPojoClassList, getContext());

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

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}