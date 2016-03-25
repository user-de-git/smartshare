package com.mss.group3.smartshare.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.mss.group3.smartshare.R;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class MainController extends AppCompatActivity {

    static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.termsandconditions);

        if(savedInstanceState== null && count == 0){
            //initialize parse
            count++;
            Parse.enableLocalDatastore(this);
            Parse.initialize(this);
            ParseUser.enableRevocableSessionInBackground();
            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();
            defaultACL.setPublicReadAccess(true);
            defaultACL.setPublicWriteAccess(true);
            ParseACL.setDefaultACL(defaultACL, true);
        }


    }


    public void AcceptAgreementButtonClick(View view)
    {
        //login screen initialization
        Intent myIntent = new Intent(MainController.this, LoginController.class);
        startActivity(myIntent);
    }

    public void RejectAgreementButtonClick(View view)
    {
        //closing application
    }

}
