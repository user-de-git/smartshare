package com.mss.group3.smartshare.model;

import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;

import com.mss.group3.smartshare.common.User;
import com.mss.group3.smartshare.interfaces.*;
import com.mss.group3.smartshare.utility.DistanceAndTimeApiCall;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by inder on 2016-02-20.
 */
public class FindVehicle extends User implements ILocation, ISchedule, IVehicle {


    private Date   departureDate;
    private Date   arrivalDate;
    private Calendar departureTime;
    private Calendar   arrivalTime;
    private double distnaceInMeters;
    private double timeInMinutes;
    private String departureAddressLineOneText;
    private String departureAddressCityNameText;
    private String departureAddressCountryNameText;
    private String departureAddressPostalCodeText;

    private String arrivalAddressLineOneText;
    private String arrivalAddressCityNameText;
    private String arrivalAddressCountryNameText;
    private String arrivalAddressPostalCodeText;

    public String getDepartureAddressLineOneText() {
        return departureAddressLineOneText;
    }

    public void setDepartureAddressLineOneText(String departureAddressLineOneText) {
        this.departureAddressLineOneText = departureAddressLineOneText;
    }

    public String getDepartureAddressCityNameText() {
        return departureAddressCityNameText;
    }

    public void setDepartureAddressCityNameText(String departureAddressCityNameText) {
        this.departureAddressCityNameText = departureAddressCityNameText;
    }

    public String getDepartureAddressCountryNameText() {
        return departureAddressCountryNameText;
    }

    public void setDepartureAddressCountryNameText(String departureAddressCountryNameText) {
        this.departureAddressCountryNameText = departureAddressCountryNameText;
    }

    public String getDepartureAddressPostalCodeText() {
        return departureAddressPostalCodeText;
    }

    public void setDepartureAddressPostalCodeText(String departureAddressPostalCodeText) {
        this.departureAddressPostalCodeText = departureAddressPostalCodeText;
    }

    public String getArrivalAddressLineOneText() {
        return arrivalAddressLineOneText;
    }

    public void setArrivalAddressLineOneText(String arrivalAddressLineOneText) {
        this.arrivalAddressLineOneText = arrivalAddressLineOneText;
    }

    public String getArrivalAddressCityNameText() {
        return arrivalAddressCityNameText;
    }

    public void setArrivalAddressCityNameText(String arrivalAddressCityNameText) {
        this.arrivalAddressCityNameText = arrivalAddressCityNameText;
    }

    public String getArrivalAddressCountryNameText() {
        return arrivalAddressCountryNameText;
    }

    public void setArrivalAddressCountryNameText(String arrivalAddressCountryNameText) {
        this.arrivalAddressCountryNameText = arrivalAddressCountryNameText;
    }

    public String getArrivalAddressPostalCodeText() {
        return arrivalAddressPostalCodeText;
    }

    public void setArrivalAddressPostalCodeText(String arrivalAddressPostalCodeText) {
        this.arrivalAddressPostalCodeText = arrivalAddressPostalCodeText;
    }



    public double getDistnaceInMeters() {
        return distnaceInMeters;
    }

    public void setDistnaceInMeters(double distnaceInMeters) {
        this.distnaceInMeters = distnaceInMeters;
    }

    public double getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(double timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public boolean findDistanceAndDuration(Geocoder geoCoder)
    {
        //la lattitude
        //ln longitude
        double la1 = 0, la2 = 0, ln1 = 0, ln2 = 0;

        boolean result;
        Point point;


        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(getDepartureAddressLineOneText() + "," +
                            getDepartureAddressCityNameText() + "," +
                            getDepartureAddressCountryNameText() + "," +
                            getDepartureAddressPostalCodeText(), 3);

            if (addresses.size() > 0) {

                la1 = addresses.get(0).getLatitude();
                ln1 = addresses.get(0).getLongitude();

            }

            List<Address> addressesArrival =
                    geoCoder.getFromLocationName(getArrivalAddressLineOneText() + "," +
                            getArrivalAddressCityNameText() + "," +
                            getArrivalAddressCountryNameText() + "," +
                            getArrivalAddressPostalCodeText(), 3);

            if (addressesArrival.size() > 0) {
                la2 = addressesArrival.get(0).getLatitude();
                ln2 = addressesArrival.get(0).getLongitude();

            }

            DistanceAndTimeApiCall apiCall = new DistanceAndTimeApiCall(la1,ln1,la2,  ln2);
            apiCall.calculate();

            setDistnaceInMeters(apiCall.getDistance());
            setTimeInMinutes(apiCall.getDuration());

            result = true;

        } catch (IOException ew) {
            ew.printStackTrace();
            result = false;
        }

        return result;
    }


}
