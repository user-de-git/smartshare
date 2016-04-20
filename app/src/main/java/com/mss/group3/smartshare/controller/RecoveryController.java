package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.InputValidation;
import com.mss.group3.smartshare.common.Success;
import com.parse.ParseException;
import com.parse.RequestPasswordResetCallback;


import static com.parse.ParseUser.requestPasswordResetInBackground;

/**
 * Created by user on 3/9/16.
 */
public class RecoveryController extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery);

    }



    public void recoveryButtonPress(View view) {

        String email=((EditText) findViewById(R.id.emailText)).getText().toString().toLowerCase();

        if(InputValidation.recoveryInputValidation(email,this)){

            return;
        }

        requestPasswordResetInBackground(email,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent myIntent = new Intent(RecoveryController.this, Success.class);
                            startActivity(myIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Email not registered!", Toast.LENGTH_SHORT).show();

                            return;

                        }
                    }

                });


    }

    public void loginButtonClick(View view ) {
        //recovery screen initialization
        Intent myIntent = new Intent(RecoveryController.this, LoginController.class);
        startActivity(myIntent);

    }



}
