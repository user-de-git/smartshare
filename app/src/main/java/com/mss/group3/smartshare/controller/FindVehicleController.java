package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.FindVehicle;
import com.mss.group3.smartshare.utility.DistanceAndTimeApiCall;
import com.mss.group3.smartshare.utility.LocationServices;
import java.io.IOException;
import java.sql.Date;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
            case R.id.setStartTimeButton: {

                timePickerDialog = new TimePickerDialog(FindVehicleController.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((TextView) findViewById(R.id.getStartTime)).setText(hourOfDay + ":" + minute);
                        Calendar C = Calendar.getInstance();
                        C.set(0,0,0,hourOfDay,minute);

                        findVehicle.setArrivalTime(C);

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(FindVehicleController.this));

                timePickerDialog.show();
                break;
            }
            case R.id.setEndTimeButton: {

                timePickerDialog = new TimePickerDialog(FindVehicleController.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((TextView) findViewById(R.id.getEndTime)).setText(hourOfDay + ":" + minute);
                        Calendar C = Calendar.getInstance();
                        C.set(0,0,0,hourOfDay,minute);

                        findVehicle.setDepartureTime(C);

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(FindVehicleController.this));

                timePickerDialog.show();
                break;
            }

            case R.id.getToDateButton: {

                datePickerDialog = new DatePickerDialog(FindVehicleController.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        ((TextView) findViewById(R.id.getToDate)).setText(dateString);

                        Calendar C = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        findVehicle.setArrivalDate(calendar.getTime());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
            }

            case R.id.getFromDateButton: {

                datePickerDialog = new DatePickerDialog(FindVehicleController.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        ((TextView) findViewById(R.id.getFromDate)).setText(dateString);

                        Calendar C = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        findVehicle.setDepartureDate(calendar.getTime());

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
            }
        }
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
