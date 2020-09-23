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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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

    private LinearLayout mMechanicInfo;
    private TextView mMechanicName, mMechanicPhone;


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

        /*
        Button requestbtn = v.findViewById(R.id.newservicerequest);
        requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     Toast.makeText(getContext(), "its working", Toast.LENGTH_SHORT).show();

              //  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
               // fragmentTransaction.replace(R.id.fragmentLayout,new RequestMechanicFragment());
                //fragmentTransaction.commit();
                //Bundle args = new Bundle();
                //BottomSheet bottomSheet = new BottomSheet();
               // args.putParcelable("mlastlocation",mLastLocation);
               // bottomSheet.setArguments(args);
               // bottomSheet.show(getActivity().getSupportFragmentManager(), "bottomSheet");

               final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),
                        R.style.BottomSheetDialogTheme);

                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext()).inflate(
                        R.layout.bottom_sheet_diolog,(LinearLayout)v.findViewById(R.id.bottomSheetContainer));
                        bottomSheetView.findViewById(R.id.bottmsheetrequestbtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "its working", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });*/
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

                    pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here"));

                    requestbtn.setText("Getting Your Mechanic ...");

                    getClosestMechanic();
                }
                /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout,new RequestMechanicFragment());
                fragmentTransaction.addToBackStack(null).commit();
                */

            }
        });

        Spinner spinnerservice = v.findViewById(R.id.spinnerservicerquest);
        String[] value = {"Select service","Towing service","Accidental towing","Battery Jumpstart","Flat tire","Minor on-site repair","Lost or locked key","Fuel problem","Medical coordination.","24x7 roadside assistance","Taxi services"};
        ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.style_spinner,arrayList);
        spinnerservice.setAdapter(arrayAdapter);

        Spinner spinnervehicle = v.findViewById(R.id.spinnervehicle);
        String[] value2 = {"Select vehicle","Royal Enfield Classic 350","Honda Activa 6G","Bajaj Pulsar","TVS Apache RTR 160"};
        ArrayList<String> arrayList2 = new  ArrayList<>(Arrays.asList(value2));
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(),R.layout.style_spinner,arrayList2);
        spinnervehicle.setAdapter(arrayAdapter2);




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