package com.mss.group3.smartshare.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.controller.LoginController;

/**
 * Created by user on 3/15/16.
 */
public class Success extends Activity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        SaveSharedPreference.setUserName(Success.this, "");
        SaveSharedPreference.setPassword(Success.this, "");
        String text = "Email sent";
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        Intent myIntent = new Intent(Success.this, LoginController.class);
        startActivity(myIntent);



    }
}
