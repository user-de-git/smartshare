package com.mss.group3.smartshare.model;

import java.util.Date;

/**
 * Created by Bhupinder on 4/11/2016.
 */
public class ShareDataStore {
    private String  id;
    private String plateNumber;
    private String startAddress;
    private String endAddress;
    private Date startDateTime;
    private Date endDateTime;
    private Double baseCost;
    private String renterEmail;
    private Boolean tripStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShareDataStore(String id,
                          String plateNumber,
                          String startAddress,
                          String endAddress,
                          Date startDateTime,
                          Date endDateTime,
                          Double BaseCost,
                          String RenterEmail,
                          Boolean TripStatus
                        )
    {
        this.id = id;
        this.plateNumber = plateNumber;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.baseCost = BaseCost;
        this.tripStatus = TripStatus;
        this.renterEmail = RenterEmail;
    }

    public Boolean getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(Boolean tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getRenterEmail() {
        return renterEmail;
    }

    public void setRenterEmail(String renterEmail) {
        this.renterEmail = renterEmail;
    }

    public Double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(Double baseCost) {
        this.baseCost = baseCost;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }
}
