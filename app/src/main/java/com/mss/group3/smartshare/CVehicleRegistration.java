package com.mss.group3.smartshare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseObject;

public class CVehicleRegistration extends Activity {
    ParseObject testObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvehicleregistration);
    }

    public void addVehicle(View view) {


        String  name = ((EditText)findViewById(R.id.name)).getText().toString();
        Integer age  =  Integer.parseInt(((EditText) findViewById(R.id.age)).getText().toString());
        String city  = ((EditText)findViewById(R.id.city)).getText().toString();
        testObject = new ParseObject("VehicleTable");
        testObject.put("name", name);
        testObject.put("age", age);
        testObject.put("city", city);
        testObject.saveInBackground();

    }
}
