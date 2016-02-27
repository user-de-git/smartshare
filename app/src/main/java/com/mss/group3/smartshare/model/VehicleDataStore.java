package com.mss.group3.smartshare.model;


public class VehicleDataStore {
    private String  id;
    private String  vehicle_type;
    private Integer capacity;
    private String  date;

    public VehicleDataStore(String id, String vehicle_type, Integer capacity, String date) {
        this.id = id;
        this.vehicle_type = vehicle_type;
        this.capacity = capacity;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
