package com.mss.group3.smartshare.model;


import java.util.Date;

public class VehicleDataStore {
    private String  id;
    private String  vehicle_type;
    private Integer capacity;
    private Date dateFrom;
    private Date dateTo;
    private Double price;

    public VehicleDataStore(String id, String vehicle_type, Integer capacity, Date dateFrom,Date dateTo,Double price) {
        this.id = id;
        this.vehicle_type = vehicle_type;
        this.capacity = capacity;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date date) {
        this.dateFrom = date;
    }
}
