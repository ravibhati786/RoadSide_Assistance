package com.example.roadsideassistance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterCustomerVehicleListView extends ArrayAdapter {


    private List<Vehicle> vehicleList;

   // private Integer[] VehicleImage; // Integer using for Vehicle Image
    private  Context context;

    public AdapterCustomerVehicleListView(List<Vehicle> vehicleList, Context context){

        super(context,R.layout.custom_custvehicle_listview,vehicleList);
        this.context = context;
        this.vehicleList = vehicleList;
     //   this.VehicleImage = VehicleImage;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        View ress = convertView;
        AdapterCustomerVehicleListView.ViewHolder viewHolder = null;

        if(ress == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            ress = layoutInflater.inflate(R.layout.custom_custvehicle_listview,null,true );
            viewHolder = new AdapterCustomerVehicleListView.ViewHolder(ress);
            ress.setTag(viewHolder);
        }else{

            viewHolder = (AdapterCustomerVehicleListView.ViewHolder) ress.getTag();
        }

        Vehicle vehicle = vehicleList.get(position);
        viewHolder.VehicleName.setText(vehicle.getvName());
        viewHolder.VehicleNumber.setText(vehicle.getvNumber());
        viewHolder.VehicleModel.setText(vehicle.getvModel());
        viewHolder.VehicleMake.setText(vehicle.getvMake());
       // viewHolder.VehicleImage.setImageResource(VehicleImage[position]);

       return ress;
    }

    static class ViewHolder
    {
        TextView VehicleName;
        TextView VehicleNumber;
        TextView VehicleModel ;
        TextView VehicleMake;
        ImageView VehicleImage;

        ViewHolder(View v){
            VehicleName =v.findViewById(R.id.custvehicleName);
            VehicleNumber=v.findViewById(R.id.custvehicleNumber);
            VehicleModel=v.findViewById(R.id.custvehicleModal);
            VehicleMake =v.findViewById(R.id.custvehicleMake);
          //  VehicleImage = v.findViewById(R.id.custvehicleImg);

        }

    }



}
