package com.mss.group3.smartshare.model;

import java.util.Calendar;

/**
 * Created by inder on 2016-03-01.
 */
public class FindVehiclelistSingleton {
    private static FindVehiclelistSingleton ourInstance = new FindVehiclelistSingleton();

    public static FindVehiclelistSingleton getInstance() {
        return ourInstance;
    }
    public Calendar departureDate;
    public Calendar arrivalDate;
    public double distance;
    public int capacity;
    public String    departureAddressPostalCodeText;
    public String    arrivalAddressDepartureCode;
    private FindVehiclelistSingleton() {
    }
}
