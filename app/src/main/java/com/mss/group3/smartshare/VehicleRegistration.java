package com.mss.group3.smartshare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.ParseObject;

import java.util.Calendar;

public class VehicleRegistration extends Activity{
    ParseObject testObject;
    Spinner spinner_vehicletype;
    ArrayAdapter<CharSequence> adaptor_vehicletype;
    Spinner spinner_vehiclecapacity;
    ArrayAdapter<CharSequence> adaptor_vehiclecapacity;
    Spinner spinner_vehiclerange;
    ArrayAdapter<CharSequence> adaptor_vehiclerange;

    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    TextView get_date,get_start_time,get_end_time;
    String vehicle_type;
    int vehicle_capacity;
    int vehicle_share_range;

    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_cvehicleregistration);
        get_date = (TextView) findViewById(R.id.get_date);
        get_start_time = (TextView) findViewById(R.id.get_start_time);
        get_end_time = (TextView) findViewById(R.id.get_end_time);
        spinner_vehicletype = (Spinner) findViewById(R.id.spinner_vehicletype);
        adaptor_vehicletype = ArrayAdapter.createFromResource(this,R.array.vehicle_type,android.R.layout.simple_spinner_item);
        adaptor_vehicletype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_vehicletype.setAdapter(adaptor_vehicletype);
        spinner_vehicletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                vehicle_type = (String) parent.getItemAtPosition(pos);
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
                vehicle_capacity = Integer.parseInt((String) parent.getItemAtPosition(pos));
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
                vehicle_share_range = Integer.parseInt(((String) parent.getItemAtPosition(pos)));
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void setDateTime(View v) {

        calendar = Calendar.getInstance();

        switch (v.getId()){
            case R.id.button_setStartTime:{

                timePickerDialog = new TimePickerDialog(VehicleRegistration.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        get_start_time.setText(hourOfDay + ":"+minute);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(VehicleRegistration.this));

                timePickerDialog.show();
                break;
            }
            case R.id.button_setEndTime:{

                timePickerDialog = new TimePickerDialog(VehicleRegistration.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        get_end_time.setText(hourOfDay + ":"+minute);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(VehicleRegistration.this));

                timePickerDialog.show();
                break;
            }

            case R.id.button_setDate:{

                datePickerDialog = new DatePickerDialog(VehicleRegistration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = (monthOfYear+1) +"/" + dayOfMonth + "/" + year;
                        get_date.setText(dateString);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
            }
        }
    }

    public void addVehicle(View view) {

        Integer vin          =  Integer.parseInt(((EditText)findViewById(R.id.et_vinnumber)).getText().toString());
        Integer plate_number =  Integer.parseInt(((EditText) findViewById(R.id.et_platenumber)).getText().toString());
        Double  price_km      =  Double.parseDouble(((EditText) findViewById(R.id.et_pricekm)).getText().toString());
        String date = ((EditText)findViewById(R.id.get_date)).getText().toString();
        String start_time = ((EditText)findViewById(R.id.get_start_time)).getText().toString();
        String end_time = ((EditText)findViewById(R.id.get_end_time)).getText().toString();

        testObject = new ParseObject("VehicleTable");
        testObject.put("VIN", vin);
        testObject.put("Plate_number", plate_number);
        testObject.put("Price_km", price_km);
        testObject.put("Capacity", vehicle_capacity);
        testObject.put("Vehicle_type", vehicle_type);
        testObject.put("vehicle_range", vehicle_share_range);
        testObject.put("Date",date);
        testObject.put("Start_time", start_time);
        testObject.put("End_time", end_time);
        testObject.saveInBackground();

    }
}
