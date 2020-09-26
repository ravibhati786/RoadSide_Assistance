package com.example.roadsideassistance;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterRecentDetailsListView extends ArrayAdapter {


    private List<HistoryPojoClass> recentList;
        // private Integer[] VehicleImage; // Integer using for Vehicle Image
    private  Context context;

    public AdapterRecentDetailsListView(List<HistoryPojoClass> recentList, Context context){

        super(context,R.layout.recent_details_add,recentList);
        this.context = context;
        this.recentList = recentList;
     //   this.VehicleImage = VehicleImage;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        View ress = convertView;
        AdapterRecentDetailsListView.ViewHolder viewHolder = null;

        if(ress == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            ress = layoutInflater.inflate(R.layout.recent_details_add,null,true );
            viewHolder = new AdapterRecentDetailsListView.ViewHolder(ress);
            ress.setTag(viewHolder);
        }else{

            viewHolder = (AdapterRecentDetailsListView.ViewHolder) ress.getTag();
        }

        HistoryPojoClass history = recentList.get(position);
        viewHolder.Name.setText(history.getName());
        String[] latlng = history.getServiceLocation().split(",");
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latlng[0]),
                    Double.parseDouble(latlng[1]), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        viewHolder.ServiceLocation.setText(address);
        viewHolder.VehicleName.setText(history.getVehicleName());
        viewHolder.ServiceName.setText(history.getServiceName());
        viewHolder.ServiceStatus.setText(history.getServiceStatus());
        //Toast.makeText(context, add, Toast.LENGTH_SHORT).show();
       return ress;


    }


    static class ViewHolder
    {
        TextView Name;
        TextView ServiceLocation ;
        TextView VehicleName;
        TextView ServiceName;
        TextView ServiceStatus;

        ViewHolder(View v){
            Name=v.findViewById(R.id.name);
            ServiceLocation=v.findViewById(R.id.custservicelocation);
            VehicleName =v.findViewById(R.id.custvehicle);
            ServiceName= v.findViewById(R.id.custservice);
            ServiceStatus = v.findViewById(R.id.custservicestatus);
        }

    }



}
