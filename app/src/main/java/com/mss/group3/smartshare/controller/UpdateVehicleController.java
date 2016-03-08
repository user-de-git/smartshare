package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;

public class UpdateVehicleController extends Activity{

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

    String vehicle_type;
    int vehicle_capacity;
    int vehicle_share_range;
    String StartdateString;
    String EnddateString;

    PostVehicle pv = new PostVehicle();

    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicleregistration);
        String Vehicle_Id = getIntent().getStringExtra(OwnerController.VEHICLE_ID);
        Toast.makeText(getApplicationContext(), "Clicked product id =" + Vehicle_Id, Toast.LENGTH_SHORT).show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
        query.getInBackground(Vehicle_Id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ((EditText) findViewById(R.id.et_vinnumber)).setText(object.getInt("VIN"));
                    ((EditText) findViewById(R.id.et_platenumber)).setText(object.getInt("Plate_number"));
                    ((EditText) findViewById(R.id.et_pricekm)).setText(object.getInt("Price_km"));
                    ((EditText) findViewById(R.id.get_StartDateTime)).setText(object.getDate("FromDate").toString());
                    ((EditText) findViewById(R.id.get_EndDateTime)).setText(object.getDate("ToDate").toString());

                    ((EditText) findViewById(R.id.et_address)).setText(object.getString("Address"));
                    ((EditText) findViewById(R.id.et_city)).setText(object.getString("City"));
                    ((EditText) findViewById(R.id.et_postalCode)).setText(object.getString("Province"));
                    ((EditText) findViewById(R.id.et_province)).setText(object.getString("Postal_Code"));

                } else {
                    // something went wrong
                }
            }
        });

        setContentView(R.layout.vehicleregistration);
        get_StartDateTime = (TextView) findViewById(R.id.get_StartDateTime);
        get_EndDateTime = (TextView) findViewById(R.id.get_EndDateTime);

        spinner_vehicletype = (Spinner) findViewById(R.id.spinner_vehicletype);
        adaptor_vehicletype = ArrayAdapter.createFromResource(this,R.array.vehicle_type,android.R.layout.simple_spinner_item);
        adaptor_vehicletype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehicletype.setAdapter(adaptor_vehicletype);

        button = (Button) findViewById(R.id.button_addModifyVehicle);
        button.setText("Edit Vehicle");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateVehicle(v);
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
        spinner_vehiclerange = (Spinner) findViewById(R.id.spinner_vehiclerange);
        adaptor_vehiclerange = ArrayAdapter.createFromResource(this,R.array.vehicle_range,android.R.layout.simple_spinner_item);
        adaptor_vehiclerange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehiclerange.setAdapter(adaptor_vehiclerange);
        spinner_vehiclerange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                pv.setVehicle_share_range(Integer.parseInt(((String) parent.getItemAtPosition(pos))));
                //vehicle_share_range = Integer.parseInt(((String) parent.getItemAtPosition(pos)));
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
                                new TimePickerDialog(UpdateVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int hour, int min) {
                                                EnddateString+=" "+hour + ":"+min;
                                                get_EndDateTime.setText(EnddateString);
                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(UpdateVehicleController.this)).show();

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
                                new TimePickerDialog(UpdateVehicleController.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override public void onTimeSet(TimePicker view,
                                                                            int hour, int min) {
                                                StartdateString+=" "+hour + ":"+min;
                                                get_StartDateTime.setText(StartdateString);
                                            }
                                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(UpdateVehicleController.this)).show();

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
        }
    }

    public void UpdateVehicle(View view) {
        UserSingleton userSingleton = UserSingleton.getInstance();

        pv.setVin(Integer.parseInt(((EditText) findViewById(R.id.et_vinnumber)).getText().toString()));
        pv.setPlate_number(Integer.parseInt(((EditText) findViewById(R.id.et_platenumber)).getText().toString()));
        pv.setPrice_km(Double.parseDouble(((EditText) findViewById(R.id.et_pricekm)).getText().toString()));
        pv.setStartDateTime(((EditText) findViewById(R.id.get_StartDateTime)).getText().toString());
        pv.setEndDateTime(((EditText) findViewById(R.id.get_EndDateTime)).getText().toString());
        pv.setCurrent_location(((EditText) findViewById(R.id.et_city)).getText().toString() + ", " +
                ((EditText) findViewById(R.id.et_postalCode)).getText().toString());

        testObject = new ParseObject("VehicleTable");
        testObject.put("VIN", pv.getVin());
        testObject.put("Plate_number",pv.getPlate_number());
        testObject.put("Price_km", pv.getPrice_km());
        testObject.put("Capacity", pv.getVehicle_capacity());
        testObject.put("Vehicle_type", pv.getVehicle_type());
        testObject.put("vehicle_range", pv.getVehicle_share_range());
        testObject.put("PostalCode",pv.getCurrent_location());
        testObject.put("FromDate",pv.getStartDateTime());
        testObject.put("ToDate", pv.getEndDateTime());
        testObject.put("Owner_email", userSingleton.emailAddress);
        testObject.saveInBackground();

    }
}
