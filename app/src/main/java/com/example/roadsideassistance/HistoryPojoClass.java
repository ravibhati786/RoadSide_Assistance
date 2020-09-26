package com.example.roadsideassistance;

public class HistoryPojoClass {
    private String name, vehicleName, serviceName, serviceStatus, serviceLocation;

    public HistoryPojoClass(String name, String vehicleName,
                            String serviceName, String serviceStatus,
                            String serviceLocation){
        this.name = name;
        this.vehicleName = vehicleName;
        this.serviceName = serviceName;
        this.serviceStatus = serviceStatus;
        this.serviceLocation =serviceLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceLocation() {
        return serviceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        this.serviceLocation = serviceLocation;
    }
}
