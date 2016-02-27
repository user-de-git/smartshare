package com.mss.group3.smartshare.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mss.group3.smartshare.*;
import com.mss.group3.smartshare.controller.OwnerController;

public class UserType extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usertype);
    }

    public void postVehicle(View view) {
        Toast.makeText(UserType.this, "Click", Toast.LENGTH_LONG).show();
        Intent screenthree = new Intent(UserType.this, OwnerController.class);
        startActivity(screenthree);

    }
}
