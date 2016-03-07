package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mss.group3.smartshare.R;

import java.util.Calendar;

/**
 * Created by inder on 2016-02-20.
 */
public class FindVehicleController extends Activity {


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
        setContentView(R.layout.findvehicle);
    }


    public void setDateTime(View v) {


    }


}
