package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateVehicleController extends AppCompatActivity{

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
    String Vehicle_Id;
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
        Vehicle_Id = getIntent().getStringExtra(OwnerController.VEHICLE_ID);
        //Toast.makeText(getApplicationContext(), "Clicked product id =" + Vehicle_Id, Toast.LENGTH_SHORT).show();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);




        ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
        query.getInBackground(Vehicle_Id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ((EditText) findViewById(R.id.et_vinnumber)).setText(object.getString("VIN"));

                    ((EditText) findViewById(R.id.et_vehiclerange)).setText(String.valueOf(object.getInt("vehicle_range")));


                    ((EditText) findViewById(R.id.et_platenumber)).setText(object.getString("Plate_number"));
                    String dd = new DecimalFormat("0.##").format(object.getDouble("Price_km"));
                    ((EditText) findViewById(R.id.et_pricekm)).setText(dd);
                    ((EditText) findViewById(R.id.get_StartDateTime)).setText(Utility(object.getDate("FromDate")).toString());
                    ((EditText) findViewById(R.id.get_EndDateTime)).setText(Utility(object.getDate("ToDate")).toString());

                    ((EditText) findViewById(R.id.et_address)).setText(object.getString("Address"));
                    ((EditText) findViewById(R.id.et_city)).setText(object.getString("City"));
                    ((EditText) findViewById(R.id.et_postalCode)).setText(object.getString("Province"));
                    ((EditText) findViewById(R.id.et_province)).setText(object.getString("Postal_Code"));

                } else {
                    // something went wrong
                }
            }
        });

        //setContentView(R.layout.vehicleregistration);
        ((TextView) findViewById(R.id.registerVehicle)).setText("Edit Info");
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

    public String Utility(Date _Date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return formatter.format(_Date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_post) {
            Intent screen = new Intent(UpdateVehicleController.this,PostVehicleController.class);
            startActivity(screen);

        } else if(id==R.id.action_search) {
            Intent screen = new Intent(UpdateVehicleController.this,FindVehicleController.class);
            startActivity(screen);
        } else if(id==R.id.action_manage) {
            Intent screen = new Intent(UpdateVehicleController.this,MyAccountController.class);
            startActivity(screen);
        } else {
            SaveSharedPreference.setUserName(UpdateVehicleController.this, "");
            SaveSharedPreference.setPassword(UpdateVehicleController.this, "");
            Intent ownerlayout = new Intent(UpdateVehicleController.this, LoginController.class);
            startActivity(ownerlayout);
        }
        return super.onOptionsItemSelected(item);
    }

    public void UpdateVehicle(View view) {
        final UserSingleton userSingleton = UserSingleton.getInstance();

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


        if(Double.parseDouble(fields[2])<0) {
            Toast.makeText(getApplicationContext(), "Price can't be below Zero", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.parseInt(fields[9])<0) {
            Toast.makeText(getApplicationContext(), "Share range can't be below Zero", Toast.LENGTH_SHORT).show();
            return;
        }
        for (String str : fields) {
            if(str == null || str.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Date start= InputValidation.DateSetter(fields[3]);
        Date end = InputValidation.DateSetter(fields[4]);
        //Date now = new Date();

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


        testObject = new ParseObject("VehicleTable");


        final ParseQuery<ParseObject> testObject = ParseQuery.getQuery("VehicleTable");

// Retrieve the object by id
        testObject.getInBackground(Vehicle_Id, new GetCallback<ParseObject>() {
            public void done(ParseObject tObject, ParseException e) {
                if (e == null) {
                    tObject.put("VIN", pv.getVin());
                    tObject.put("Plate_number", pv.getPlate_number());
                    tObject.put("Price_km", pv.getPrice_km());
                    tObject.put("Capacity", pv.getVehicle_capacity());
                    tObject.put("Vehicle_type", pv.getVehicle_type());
                    tObject.put("vehicle_range", pv.getVehicle_share_range());
                    tObject.put("Address",pv.getAddress());
                    tObject.put("City",pv.getCity());
                    tObject.put("Province",pv.getProvince());
                    tObject.put("Postal_Code",pv.getPostal_code());
                    tObject.put("PostalCode",pv.getCurrent_location()); // This is the combined address
                    tObject.put("FromDate",pv.getStartDateTime());
                    tObject.put("ToDate", pv.getEndDateTime());
                    tObject.put("Owner_email", userSingleton.emailAddress);
                    try {
                        tObject.save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    Intent myIntent = new Intent(UpdateVehicleController.this, OwnerController.class);
                    startActivity(myIntent);
                }
            }
        });

    }

    public void deleteVehicle(View view) {

        final ParseQuery<ParseObject> testObject = ParseQuery.getQuery("VehicleTable");

        testObject.getInBackground(Vehicle_Id, new GetCallback<ParseObject>() {
            public void done(ParseObject tObject, ParseException e) {
                if (e == null) {
                    try {
                        tObject.delete();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    Intent myIntent = new Intent(UpdateVehicleController.this, OwnerController.class);
                    startActivity(myIntent);
                }
            }
        });
    }
}
