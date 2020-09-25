package com.example.roadsideassistance;

public class Vehicle {
    String vId, vNumber, vName, vModel, vMake;

    public Vehicle(String vId, String vNumber,String vName, String vModel, String vMake ){
        this.vId = vId;
        this.vNumber = vNumber;
        this.vName = vName;
        this.vModel = vModel;
        this.vMake = vMake;

    }

    public void setId(String id){
        this.vId = id;
    }

    public String getvId(){
        return this.vId;
    }


    public String getvNumber(){
        return vNumber;
    }

    public String getvName(){
        return vName;
    }

    public String getvModel(){
        return vModel;
    }

    public String getvMake(){
        return vMake;
    }
}
