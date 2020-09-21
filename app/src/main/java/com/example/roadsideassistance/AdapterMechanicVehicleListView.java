package com.example.roadsideassistance;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdapterMechanicVehicleListView extends ArrayAdapter {


    private String[] VehicleName;
    private String[] VehicleNumber;
    private String[] VehicleModel;
    private String[] VehicleMake;

    private Integer[] VehicleImage; // Integer using for Vehicle Image
    private  Activity context;

    public AdapterMechanicVehicleListView(Activity context, String[] VehicleName, String[] VehicleNumber,
                                          String[] VehicleModel, String[] VehicleMake, Integer[] VehicleImage){

        super(context,R.layout.custom_mechvehicle_listview,VehicleName);
        this.context = context;
        this.VehicleName = VehicleName;
        this.VehicleNumber = VehicleNumber;
        this.VehicleModel = VehicleModel;
        this.VehicleMake = VehicleMake;
        this.VehicleImage = VehicleImage;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        View ress = convertView;
        AdapterMechanicVehicleListView.ViewHolder viewHolder = null;

        if(ress == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            ress = layoutInflater.inflate(R.layout.custom_mechvehicle_listview,null,true );
            viewHolder = new AdapterMechanicVehicleListView.ViewHolder(ress);
            ress.setTag(viewHolder);
        }else{

            viewHolder = (AdapterMechanicVehicleListView.ViewHolder) ress.getTag();
        }

        viewHolder.VehicleName.setText(VehicleName[position]);
        viewHolder.VehicleNumber.setText(VehicleNumber[position]);
        viewHolder.VehicleModel.setText(VehicleModel[position]);
        viewHolder.VehicleMake.setText(VehicleMake[position]);
        viewHolder.VehicleImage.setImageResource(VehicleImage[position]);

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
            VehicleName =v.findViewById(R.id.MechvehicleName);
            VehicleNumber=v.findViewById(R.id.MechvehicleNumber);
            VehicleModel=v.findViewById(R.id.MechvehicleModal);
            VehicleMake =v.findViewById(R.id.MechvehicleMake);
            VehicleImage = v.findViewById(R.id.MechvehicleImg);

        }

    }



}
