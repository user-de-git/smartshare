package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.FindVehicle;
import com.mss.group3.smartshare.model.FindVehicleList;
import com.mss.group3.smartshare.model.FindVehiclelistSingleton;
import com.mss.group3.smartshare.utility.LocationServices;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by inder on 2016-02-20.
 * This controller will trigger the find vehicle layout
 * The purpose is to validate information and find matching vehicle as per schedule
 */
public class FindVehicleController extends Activity {

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    FindVehicle findVehicle;
    String EnddateString;
    int month1;
    int day1;
    int year1;
    int a1,a2,a3,a4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findvehicle);
        findVehicle = new FindVehicle();
    }

    //set date and time for to and from button clicks
    public void setDateTime(View v) {

        calendar = Calendar.getInstance();

        switch (v.getId()) {

            case R.id.getToDateButton: {

                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view,
                                                            int year, int month, int day) {
                                EnddateString = (month+1) +"/" + day + "/" + year;
                                month1 = month;
                                day1 = day;
                                year1 = year;
                                new TimePickerDialog(FindVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int hour, int min) {
                                                EnddateString+=" "+hour + ":"+min;
                                                ((TextView) findViewById(R.id.getToDate)).setText(EnddateString);    // Text View
                                                Calendar C = new GregorianCalendar();

                                                C.set(year1, month1, day1, hour, min, 0);
                                                findVehicle.setArrivalDate(C);

                                                String a = findVehicle.getArrivalDate().getTime().toString();

                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                        android.text.format.DateFormat.is24HourFormat(FindVehicleController.this)).show();

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }

            case R.id.getFromDateButton: {

                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view,
                                                            int year, int month, int day) {
                                EnddateString = (month+1) +"/" + day + "/" + year;
                                month1 = month;
                                day1 = day;
                                year1 = year;
                                new TimePickerDialog(FindVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int hour, int min) {
                                                EnddateString+=" "+hour + ":"+min;
                                                ((TextView) findViewById(R.id.getFromDate)).setText(EnddateString);    // Text View
                                                Calendar C = new GregorianCalendar();
                                                C.set(year1, month1, day1, hour, min, 0);
                                                findVehicle.setDepartureDate(C);

                                                String a = findVehicle.getDepartureDate().getTime().toString();

                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                        android.text.format.DateFormat.is24HourFormat(FindVehicleController.this)).show();

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            }
        }
    }

    public static Calendar convertCalendar(final Calendar calendar, final TimeZone timeZone) {
        Calendar ret = new GregorianCalendar(timeZone);
        ret.setTimeInMillis(calendar.getTimeInMillis() +
                timeZone.getOffset(calendar.getTimeInMillis()) -
                TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
        ret.getTime();
        return ret;
    }

    //refresh current location
    public void getCurrentLocationButtonClick(View v) {
        String currentAddress = null;
        getLocation(currentAddress);
    }

    //find list of vehicles
    public void processFindVehicleButtonClick(View v) {

        findVehicle.setDepartureAddressLineOneText(((EditText) findViewById(R.id.departureAddressLineOneText)).getText().toString());
        findVehicle.setDepartureAddressCityNameText(((EditText) findViewById(R.id.departureAddressCityNameText)).getText().toString());
        findVehicle.setDepartureAddressCountryNameText(((EditText) findViewById(R.id.departureAddressCountryNameText)).getText().toString());
        findVehicle.setDepartureAddressPostalCodeText(((EditText) findViewById(R.id.departureAddressPostalCodeText)).getText().toString());

        findVehicle.setArrivalAddressLineOneText(((EditText) findViewById(R.id.arrivalAddressLineOneText)).getText().toString());
        findVehicle.setArrivalAddressCityNameText(((EditText) findViewById(R.id.arrivalAddressCityNameText)).getText().toString());
        findVehicle.setArrivalAddressCountryNameText(((EditText) findViewById(R.id.arrivalAddressCountryNameText)).getText().toString());
        findVehicle.setArrivalAddressPostalCodeText(((EditText) findViewById(R.id.arrivalAddressPostalCodeText)).getText().toString());



        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        findVehicle.findDistanceAndDuration(geoCoder);

        Toast.makeText(getApplicationContext(), "Distance km  " +findVehicle.getDistnaceInMeters()+
                "Duration minutes "+ findVehicle.getTimeInMinutes(), Toast.LENGTH_SHORT).show();

        FindVehiclelistSingleton obj = FindVehiclelistSingleton.getInstance();

        String a = findVehicle.getArrivalDate().getTime().toString();

        obj.arrivalDate = findVehicle.getArrivalDate();
        obj.departureDate = findVehicle.getDepartureDate();
        obj.departureAddressPostalCodeText = findVehicle.getDepartureAddressCityNameText()+
                                             findVehicle.getDepartureAddressCountryNameText()+
                                             findVehicle.getDepartureAddressPostalCodeText();
        obj.arrivalAddressDepartureCode    = findVehicle.getArrivalAddressCountryNameText()+
                                             findVehicle.getArrivalAddressCityNameText() +
                                             findVehicle.getArrivalAddressPostalCodeText();

        Intent screenfour = new Intent(FindVehicleController.this, FindVehicleList.class);
        startActivity(screenfour);

    }

    //get current location
    private void getLocation(String address) {

        LocationServices mLocationServices = new LocationServices(this);
        mLocationServices.getLocation();

        if (mLocationServices.isLocationAvailable == false) {

            //try again
            Toast.makeText(getApplicationContext(), "Your location is not available, " +
                    "Enter address manually or Press refresh after enabling location.", Toast.LENGTH_SHORT).show();
            return;

        } else {
            // Getting location co-ordinates
            double latitude = mLocationServices.getLatitude();
            double longitude = mLocationServices.getLongitude();
            address = mLocationServices.getLocationAddress();

            try {
                ((EditText) findViewById(R.id.departureAddressLineOneText)).setText(mLocationServices.getLineOneAddress());
                ((EditText) findViewById(R.id.departureAddressCityNameText)).setText(mLocationServices.getLocationCity());
                ((EditText) findViewById(R.id.departureAddressCountryNameText)).setText(mLocationServices.getLocationCountry());
                ((EditText) findViewById(R.id.departureAddressPostalCodeText)).setText(mLocationServices.getPostalCode());

            } catch (Exception e) {

            }
        }

        //close the gps
        mLocationServices.closeGPS();
    }

}
