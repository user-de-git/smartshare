package com.mss.group3.smartshare.model;

import java.util.Date;

/**
 * Created by inder on 2016-03-07.
 */
public class FindVehicleDataStore {
    private String  id;
    private String  vehicle_type;
    private Integer capacity;
    private Date  date;

    public FindVehicleDataStore(String id, String vehicle_type, Integer capacity, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
