package com.mss.group3.smartshare.controller;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mss.group3.smartshare.common.InputValidation;
import com.mss.group3.smartshare.common.SaveSharedPreference;
import com.mss.group3.smartshare.model.Login;
import com.mss.group3.smartshare.model.PostVehicle;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.utility.LocationServices;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostVehicleController extends AppCompatActivity{


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
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    PostVehicle pv = new PostVehicle();

    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        geoCoder = new Geocoder(this, Locale.getDefault());

        setContentView(R.layout.vehicleregistration);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

                    try {
                        addVehicle(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    public void addVehicle(View view) throws InterruptedException, ParseException {
        UserSingleton userSingleton = UserSingleton.getInstance();
        String[] fields = new String[10];
        fields[0] = ((EditText) findViewById(R.id.et_vinnumber)).getText().toString();
        fields[1] = ((EditText) findViewById(R.id.et_platenumber)).getText().toString();
        fields[2] = ((EditText) findViewById(R.id.et_pricekm)).getText().toString();
        fields[3] = ((EditText) findViewById(R.id.get_StartDateTime)).getText().toString();
        fields[4] = ((EditText) findViewById(R.id.get_EndDateTime)).getText().toString();
        fields[5] = ((EditText) findViewById(R.id.et_address)).getText().toString();
        fields[6] = ((EditText) findViewById(R.id.et_city)).getText().toString();
        fields[7] = ((EditText) findViewById(R.id.et_postalCode)).getText().toString();
        fields[8] = ((EditText) findViewById(R.id.et_province)).getText().toString();
        fields[9] = ((EditText) findViewById(R.id.et_vehiclerange)).getText().toString();


        for (String str : fields) {
            if(str == null || str.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Date start= InputValidation.DateSetter(fields[3]);
        Date end = InputValidation.DateSetter(fields[4]);

        if(start.before(new Date(System.currentTimeMillis()-24*60*60*1000))) {
            Toast.makeText(getApplicationContext(), "Please enter valid start date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(end.before(start)) {
            Toast.makeText(getApplicationContext(), "Please enter valid end date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(start.equals(end)){
            Toast.makeText(getApplicationContext(), "Start and End date are same", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(fields[2])<0) {
            Toast.makeText(getApplicationContext(), "Price can't be below Zero", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.parseInt(fields[9])<0) {
            Toast.makeText(getApplicationContext(), "Share range can't be below Zero", Toast.LENGTH_SHORT).show();
            return;
        }

        pv.setVin(fields[0]);
        pv.setPlate_number(fields[1]);
        pv.setPrice_km(Double.parseDouble(fields[2]));
        pv.setStartDateTime(fields[3]);
        pv.setEndDateTime(fields[4]);
        pv.setAddress(fields[5]);
        pv.setCity(fields[6]);
        pv.setPostal_code(fields[7]);
        pv.setProvince(fields[8]);
        pv.setVehicle_share_range(Integer.parseInt(fields[9]));

        pv.setCurrent_location(((EditText) findViewById(R.id.et_city)).getText().toString() + ", " +
                ((EditText) findViewById(R.id.et_postalCode)).getText().toString());

        ParseObject testObject = new ParseObject("VehicleTable");
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
        
        testObject.save();

        //Thread.sleep(1500);

        Intent myIntent = new Intent(PostVehicleController.this, OwnerController.class);
        startActivity(myIntent);

    }

    //refresh current location
    public void getCurrentLocationButtonClick(View v) {

        if (Build.VERSION.SDK_INT < 23) {
            String currentAddress = null;
            getLocation(currentAddress);
        }
        else {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_post) {
            Intent screen = new Intent(PostVehicleController.this,PostVehicleController.class);
            startActivity(screen);

        } else if(id==R.id.action_search) {
            Intent screen = new Intent(PostVehicleController.this,FindVehicleController.class);
            startActivity(screen);
        } else if(id==R.id.action_manage) {
            Intent screen = new Intent(PostVehicleController.this,MyAccountController.class);
            startActivity(screen);
        } else if(id==R.id.action_logout){

            SaveSharedPreference.setUserName(PostVehicleController.this, "");
            SaveSharedPreference.setPassword(PostVehicleController.this, "");
            Intent ownerlayout = new Intent(PostVehicleController.this, LoginController.class);

            startActivity(ownerlayout);
            finish();
        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        switch(requestCode) {

            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    String currentAddress = null;
                    getLocation(currentAddress);
                }
                else {

                }
                break;
        }
    }
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(ContextCompat.checkSelfPermission(this, perm)==
                PackageManager.PERMISSION_GRANTED);
    }
}
