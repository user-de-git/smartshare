package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mss.group3.smartshare.*;
import com.mss.group3.smartshare.common.SaveSharedPreference;

public class UserTypeController extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.usertype);
    }

    public void postVehicle(View view) {

        Intent ownerlayout = new Intent(UserTypeController.this, OwnerController.class);

        startActivity(ownerlayout);

    }

    public void findVehicleButtonClick(View view) {

        Intent entervehiclelayout = new Intent(UserTypeController.this, FindVehicleController.class);

        startActivity(entervehiclelayout);
    }



    public void manageAcountButtonClick(View view) {

        Intent entervehiclelayout = new Intent(UserTypeController.this, MyAccountController.class);

        startActivity(entervehiclelayout);
    }



}
