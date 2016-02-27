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

        calendar = Calendar.getInstance();

        switch (v.getId()){
            case R.id.button_setStartTime:{

                timePickerDialog = new TimePickerDialog(FindVehicleController.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        get_start_time.setText(hourOfDay + ":"+minute);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(FindVehicleController.this));

                timePickerDialog.show();
                break;
            }
            case R.id.button_setEndTime:{

                timePickerDialog = new TimePickerDialog(FindVehicleController.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        get_end_time.setText(hourOfDay + ":"+minute);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(FindVehicleController.this));

                timePickerDialog.show();
                break;
            }

            case R.id.button_setDate:{

                datePickerDialog = new DatePickerDialog(FindVehicleController.this, new DatePickerDialog.OnDateSetListener() {
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


}
