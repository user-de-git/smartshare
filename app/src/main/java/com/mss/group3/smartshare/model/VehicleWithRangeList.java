package com.mss.group3.smartshare.model;

import com.parse.ParseGeoPoint;

import java.util.Date;

/**
 * Created by inder on 2016-03-03.
 */
public class VehicleWithRangeList {

    public String  id;
    public Integer plateNumber;
    public String  vehicle_type;
    public Integer capacity;
    public Integer kmRange;
    public Date    fromDate;
    public Date    toDate;
    public String postalCode;
    public Integer pricePerKm;
    public String  email;
    public ParseGeoPoint point;





    public VehicleWithRangeList(String id,Integer plateNumber, String vehicle_type,
                                Integer capacity, Integer kmRange, String postalCode,
                                Date fromDate, Date toDate, Integer pricePerKm,
                                String email,ParseGeoPoint point) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.vehicle_type = vehicle_type;
        this.capacity = capacity;
        this.kmRange = kmRange;
        this.postalCode = postalCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pricePerKm = pricePerKm;
        this.email = email;
        this.point = point;
    }


}
