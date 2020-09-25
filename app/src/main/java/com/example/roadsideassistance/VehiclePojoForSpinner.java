package com.example.roadsideassistance;

public class VehiclePojoForSpinner {
    String vId, vName;

    public VehiclePojoForSpinner(String vId,String vName){
        this.vId = vId;
        this.vName = vName;
    }

    public void setId(String id){
        this.vId = id;
    }

    public String getvId(){
        return this.vId;
    }
    public String getvName(){
        return vName;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return vName;
    }
}

