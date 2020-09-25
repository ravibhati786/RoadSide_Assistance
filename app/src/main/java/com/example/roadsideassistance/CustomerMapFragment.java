package com.example.roadsideassistance;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.geofire.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    // TODO: Rename and change types of parameters
    private GoogleMap mMap;
    private LatLng pickupLocation;
    private Boolean requestBool = false;
    private Marker pickupMarker;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    BottomSheetDialog bottomSheetDialog;
    FragmentManager fragmentManager;

    private SupportMapFragment mapFragment;
    private Button requestbtn;
    Button btn2;
    View v;
    Spinner spinnerServiceName, spinnerVehicleName;

    private LinearLayout mMechanicInfo;
    private TextView mMechanicName, mMechanicPhone;

    private List<String> listServices;
    private List<VehiclePojoForSpinner> vehicleList;
    String serviceId,vehicleId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_customer_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(CustomerMapFragment.this);

        mMechanicInfo = v.findViewById(R.id.mechanicInfo);
        mMechanicName = v.findViewById(R.id.mechanicName);
        mMechanicPhone = v.findViewById(R.id.mechanicPhone);

        listServices = new ArrayList<>();
        vehicleList = new ArrayList<>();

        spinnerServiceName = v.findViewById(R.id.spinnerservicerquest);
        spinnerVehicleName = v.findViewById(R.id.spinnervehicle);
        loadServiceSpinnerData();
        loadVehicleSpinnerData();

        requestbtn = v.findViewById(R.id.bottmsheetrequestbtn);


        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestBool)
                {
                    requestBool = false;
                    geoQuery.removeAllListeners();
                    if(mechanicLocationRefListener!=null)
                        mechanicLocationRef.removeEventListener(mechanicLocationRefListener);

                    if(mechanicFoundId != null){
                        DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("customerRequests");
                        mechanicRef.removeValue();
                        mechanicFoundId = null;
                    }
                    mechanicFound = false;
                    radius = 1;
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequests");
                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.removeLocation(uid, new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    if(pickupMarker != null){
                        pickupMarker.remove();
                    }
                    if(mMechanicMarker!= null){
                        mMechanicMarker.remove();
                    }
                    requestbtn.setText("Call Mechanic!");
                    mMechanicInfo.setVisibility(View.GONE);
                    mMechanicName.setText("");
                    mMechanicPhone.setText("");
                }
                else{
                    requestBool = true;
                    String uid = FirebaseAuth.getInstance().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequests");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(uid, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    DatabaseReference refRequest = FirebaseDatabase.getInstance().getReference("customerRequests").child(uid);
                    Map serviceInfo = new HashMap();
                    serviceInfo.put("serviceId",serviceId);
                    serviceInfo.put("vehicleId",vehicleId);
                    refRequest.updateChildren(serviceInfo);

                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                    requestbtn.setText("Getting Your Mechanic ...");

                    getClosestMechanic();
                }

            }
        });

        spinnerServiceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    Toast.makeText(getContext(), "Please Select Service", Toast.LENGTH_SHORT).show();
                }
                else{
                    serviceId = String.valueOf(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerVehicleName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                VehiclePojoForSpinner vehiclePojoForSpinner = (VehiclePojoForSpinner)adapterView.getSelectedItem();
                if(vehiclePojoForSpinner.getvId().equals("0")){
                    Toast.makeText(getContext(), "Please Select a vehicle!!!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    vehicleId = vehiclePojoForSpinner.getvId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;

    }

    private int radius = 1;
    private Boolean mechanicFound = false;
    private String mechanicFoundId;
    GeoQuery geoQuery;
    private void getClosestMechanic(){
        DatabaseReference mechanicLocation = FirebaseDatabase.getInstance().getReference().child("mechanicsAvailable");
        GeoFire geoFire = new GeoFire(mechanicLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mechanicFound && requestBool){

                    mechanicFound = true;
                    mechanicFoundId = key;
                    DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("customerRequests");
                    String customerId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("customerId",customerId);
                    mechanicRef.updateChildren(map);

                    getMechanicLocation();
                    getMechanicInfo();
                    getHasServiceEnded();
                    requestbtn.setText("Looking for Mechanic Location....");
                }

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!mechanicFound){
                    radius++;
                    getClosestMechanic();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    private void loadServiceSpinnerData() {

        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();


                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_FETCH_SERVICES,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    listServices.clear();
                                    JSONObject obj = new JSONObject(response);

                                    JSONArray serviceArray = obj.getJSONArray("Data");

                                    listServices.add("--Select Service--");

                                    for (int i = 0; i < serviceArray.length(); i++) {
                                        JSONObject serviceObject = serviceArray.getJSONObject(i);
                                        listServices.add(serviceObject.getString("ServiceName"));
                                    }

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),R.layout.style_spinner, listServices);
                                    dataAdapter.setDropDownViewResource(R.layout.style_spinner);
                                    spinnerServiceName.setAdapter(dataAdapter);
                                    loadingDialog.dismissDialog();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void loadVehicleSpinnerData() {

        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_VEHICLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //vehicleList.clear();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);


                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray vehicleArray = obj.getJSONArray("Data");

                            //vehicleList.add("--Select Service--");
                            vehicleList.add(0,new VehiclePojoForSpinner("0","--Select Vehicle--"));

                            //now looping through all the elements of the json array
                            for (int i = 0; i < vehicleArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject vehicleObject = vehicleArray.getJSONObject(i);

                                //creating a vehicle object and giving them the values from json object
                                VehiclePojoForSpinner vehicle = new VehiclePojoForSpinner(vehicleObject.getString("VehicleId"),
                                        vehicleObject.getString("VehicleName"));

                                //adding the hero to herolist
                                vehicleList.add(vehicle);
                            }

                            // Creating adapter for spinner
                            ArrayAdapter<VehiclePojoForSpinner> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.style_spinner, vehicleList);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(R.layout.style_spinner);

                            spinnerVehicleName.setAdapter(dataAdapter);

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
                        Log.i("Connection",error.getMessage());
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.i("UserId",String.valueOf(new SharedPrefManager(getContext()).getLoggedUserId()));
                params.put("UserId", String.valueOf(new SharedPrefManager(getContext()).getLoggedUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private DatabaseReference serviceHasEndedRef;
    private ValueEventListener mechanicHasCompletedServiceRefListener;
    private void getHasServiceEnded(){
        serviceHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("customerRequests").child("customerId");
        mechanicHasCompletedServiceRefListener = serviceHasEndedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                }else{
                    endService();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void endService(){
        requestBool = false;
        geoQuery.removeAllListeners();
        mechanicLocationRef.removeEventListener(mechanicLocationRefListener);
        serviceHasEndedRef.removeEventListener(mechanicHasCompletedServiceRefListener);

        if (mechanicFoundId != null){
            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("customerRequest");
            driverRef.removeValue();
            mechanicFoundId = null;

        }
        mechanicFound = false;
        radius = 1;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

            }
        });

        if(pickupMarker != null){
            pickupMarker.remove();
        }
        if (mMechanicMarker != null){
            mMechanicMarker.remove();
        }
        requestbtn.setText("Call Mechanic");

        mMechanicInfo.setVisibility(View.GONE);
        mMechanicName.setText("");
        mMechanicPhone.setText("");
    }


    private void getMechanicInfo() {
        Activity  act = (Activity)getContext();
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMechanicInfo.setVisibility(View.VISIBLE);
            }
        });
        DatabaseReference mCustomerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId);
        mCustomerDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0)
                {
                    Map<String,Object> map = (Map<String, Object>)snapshot.getValue();
                    Log.i("Mechanicvisibility", String.valueOf(mMechanicInfo.getVisibility()));
                    if(map.get("name") != null){
                        mMechanicName.setText(map.get("name").toString());
                    }
                    if(map.get("phone") != null){
                        mMechanicPhone.setText(map.get("phone").toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Marker mMechanicMarker;
    private DatabaseReference mechanicLocationRef;
    private ValueEventListener mechanicLocationRefListener;
    private void getMechanicLocation(){
        mechanicLocationRef = FirebaseDatabase.getInstance().getReference().child("mechanicsWorking").child(mechanicFoundId).child("l");
        mechanicLocationRefListener = mechanicLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && requestBool){
                    List<Object> map = (List<Object>)snapshot.getValue();
                    double locationLat = 0;
                    double locationLong = 0;
                    requestbtn.setText("Mechanic Found");
                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString()) ;
                    }

                    if(map.get(1) != null){
                        locationLong = Double.parseDouble(map.get(1).toString()) ;
                    }

                    LatLng mechanicLatLng = new LatLng(locationLat,locationLong);
                    if(mMechanicMarker !=null)
                    {
                        mMechanicMarker.remove();
                    }
                    Location location1 = new Location("");
                    location1.setLatitude(pickupLocation.latitude);
                    location1.setLongitude(pickupLocation.longitude);

                    Location location2 = new Location("");
                    location2.setLatitude(mechanicLatLng.latitude);
                    location2.setLongitude(mechanicLatLng.longitude);

                    float distance = location1.distanceTo(location2);
                    if(distance<100){
                        requestbtn.setText("Mechanic is Here");
                    }
                    else{
                        requestbtn.setText("Mechanic is Found : "+String.valueOf(distance));
                    }

                    mMechanicMarker = mMap.addMarker(new MarkerOptions().position(mechanicLatLng).title("Your Mechanic").icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_mechanic_48)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);


        // mMap.getUiSettings().setZoomControlsEnabled(true);

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(),R.raw.roadside_assistance_stylemap));
            if (!success)
                Log.e("Error","Sytle parsing error");
        }catch (Resources.NotFoundException ex)
        {
            ex.printStackTrace();
        }


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        mMap.setMyLocationEnabled(true);
        buildGoogleApiClient();

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        //if (mCurrLocationMarker = null) {
        //  mCurrLocationMarker.remove();
        //}

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        //    MarkerOptions markerOptions = new MarkerOptions();
        //  markerOptions.position(latLng);
        // markerOptions.title("Current Position");
        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        // mCurrLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));




    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    final int LOCATION_REQUEST_CODE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mapFragment.getMapAsync(this);
                }else {
                    Toast.makeText(getContext(),"Please Provide the permission",Toast.LENGTH_LONG).show();
                }
            }
        }

    }



}