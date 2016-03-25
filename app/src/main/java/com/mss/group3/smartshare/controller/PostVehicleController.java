package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.Login;
import com.mss.group3.smartshare.model.PostVehicle;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.utility.LocationServices;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostVehicleController extends Activity{

    ParseObject testObject;
    Spinner spinner_vehicletype;
    ArrayAdapter<CharSequence> adaptor_vehicletype;
    Spinner spinner_vehiclecapacity;
    ArrayAdapter<CharSequence> adaptor_vehiclecapacity;
    Spinner spinner_vehiclerange;
    ArrayAdapter<CharSequence> adaptor_vehiclerange;

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    TextView get_StartDateTime,get_EndDateTime;
    Button button;
    Button delete;
    String vehicle_type;
    int vehicle_capacity;
    int vehicle_share_range;
    String StartdateString;
    String EnddateString;
    Geocoder geoCoder;

    PostVehicle pv = new PostVehicle();

    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoCoder = new Geocoder(this, Locale.getDefault());

        setContentView(R.layout.vehicleregistration);
        get_StartDateTime = (TextView) findViewById(R.id.get_StartDateTime);
        get_EndDateTime = (TextView) findViewById(R.id.get_EndDateTime);
        delete = (Button) findViewById(R.id.button_deleteVehicle);
        delete.setVisibility(View.GONE);
        spinner_vehicletype = (Spinner) findViewById(R.id.spinner_vehicletype);
        adaptor_vehicletype = ArrayAdapter.createFromResource(this,R.array.vehicle_type,android.R.layout.simple_spinner_item);
        adaptor_vehicletype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehicletype.setAdapter(adaptor_vehicletype);
        button = (Button) findViewById(R.id.button_addModifyVehicle);
        button.setText("Add Vehicle");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addVehicle(v);
            }
        });

        spinner_vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                pv.setVehicle_type((String) parent.getItemAtPosition(pos));
                //vehicle_type = (String) parent.getItemAtPosition(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_vehiclecapacity = (Spinner) findViewById(R.id.spinner_vehiclecapacity);
        adaptor_vehiclecapacity = ArrayAdapter.createFromResource(this,R.array.vehicle_capacity,android.R.layout.simple_spinner_item);
        adaptor_vehiclecapacity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehiclecapacity.setAdapter(adaptor_vehiclecapacity);
        spinner_vehiclecapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                pv.setVehicle_capacity(Integer.parseInt((String) parent.getItemAtPosition(pos)));
                //vehicle_capacity = Integer.parseInt((String) parent.getItemAtPosition(pos));
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    public void setDateTime(View v) {

        calendar = Calendar.getInstance();

        switch (v.getId()){

            case R.id.button_setEndDateTime:{
                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view,
                                                            int year, int month, int day) {
                                EnddateString = (month+1) +"/" + day + "/" + year;
                                new TimePickerDialog(PostVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int hour, int min) {
                                                EnddateString+=" "+hour + ":"+min;
                                                get_EndDateTime.setText(EnddateString);
                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(PostVehicleController.this)).show();

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }

            case R.id.button_setStartDateTime:{
                new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view,
                                                            int year, int month, int day) {
                                StartdateString = (month+1) +"/" + day + "/" + year;
                                new TimePickerDialog(PostVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int hour, int min) {
                                                StartdateString+=" "+hour + ":"+min;
                                                get_StartDateTime.setText(StartdateString);
                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(PostVehicleController.this)).show();

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
        }
    }

    public void addVehicle(View view) {
        UserSingleton userSingleton = UserSingleton.getInstance();

        pv.setVin( ((EditText) findViewById(R.id.et_vinnumber)).getText().toString());
        pv.setPlate_number(((EditText) findViewById(R.id.et_platenumber)).getText().toString());
        pv.setPrice_km(Double.parseDouble(((EditText) findViewById(R.id.et_pricekm)).getText().toString()));
        pv.setStartDateTime(((EditText) findViewById(R.id.get_StartDateTime)).getText().toString());
        pv.setEndDateTime(((EditText) findViewById(R.id.get_EndDateTime)).getText().toString());
        pv.setCurrent_location(((EditText) findViewById(R.id.et_city)).getText().toString() + ", " +
                ((EditText) findViewById(R.id.et_postalCode)).getText().toString());
        pv.setAddress(((EditText) findViewById(R.id.et_address)).getText().toString());
        pv.setCity(((EditText) findViewById(R.id.et_city)).getText().toString());
        pv.setPostal_code(((EditText) findViewById(R.id.et_postalCode)).getText().toString());
        pv.setProvince(((EditText) findViewById(R.id.et_province)).getText().toString());
        pv.setVehicle_share_range(Integer.parseInt(((EditText) findViewById(R.id.et_vehiclerange)).getText().toString()));

        testObject = new ParseObject("VehicleTable");
        testObject.put("VIN", pv.getVin());
        testObject.put("Plate_number",pv.getPlate_number());
        testObject.put("Price_km", pv.getPrice_km());
        testObject.put("Capacity", pv.getVehicle_capacity());
        testObject.put("Vehicle_type", pv.getVehicle_type());
        testObject.put("vehicle_range", pv.getVehicle_share_range());
        testObject.put("Address",pv.getAddress());
        testObject.put("City",pv.getCity());
        testObject.put("Province",pv.getProvince());
        testObject.put("Postal_Code",pv.getPostal_code());
        testObject.put("PostalCode",pv.getCurrent_location()); // This is the combined address
        testObject.put("FromDate",pv.getStartDateTime());
        testObject.put("ToDate", pv.getEndDateTime());
        testObject.put("isViewed", false);
        testObject.put("Owner_email", userSingleton.emailAddress);
        
        
          String sourceAddress = pv.getAddress();


        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocationName(pv.getCurrent_location(), 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double la1 = 0, ln1 = 0;
        if (addresses.size() > 0) {

            la1 = addresses.get(0).getLatitude();
            ln1 = addresses.get(0).getLongitude();

        }





        ParseGeoPoint point = new ParseGeoPoint(la1, ln1);
        testObject.put("geopoint", point);
        
        testObject.saveInBackground();

    }

    //refresh current location
    public void getCurrentLocationButtonClick(View v) {
        String currentAddress = null;
        getLocation(currentAddress);
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
                ((EditText) findViewById(R.id.et_address)).setText(mLocationServices.getLineOneAddress());
                ((EditText) findViewById(R.id.et_city)).setText(mLocationServices.getLocationCity());
                ((EditText) findViewById(R.id.et_province)).setText(mLocationServices.getLocationCountry());
                ((EditText) findViewById(R.id.et_postalCode)).setText(mLocationServices.getPostalCode());

            } catch (Exception e) {

            }
        }
        //close the gps
        mLocationServices.closeGPS();
    }


}
