package com.mss.group3.smartshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


/**
 * Created by group three on 2016-02-07.
 */
public class CUserRegistration extends Activity {

    String currentAddressGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //get user location if available
        getLocation(currentAddressGPS);
    }


    private void getLocation(String address) {

        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, Enter address manually or Press refresh after enabling location.", Toast.LENGTH_SHORT).show();
            return;

        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();

            address = mGPSService.getLocationAddress();
            //add default location
            ((EditText) findViewById(R.id.addressLineOneText)).setText(mGPSService.getLineOneAddress());
            ((EditText) findViewById(R.id.cityNameText)).setText(mGPSService.getLocationCity());
            ((EditText) findViewById(R.id.countryNameText)).setText(mGPSService.getLocationCountry());
            ((EditText) findViewById(R.id.postalCodeText)).setText(mGPSService.getPostalCode());

        }

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
    }


    /**
     * *
     * refresh location
     */
    public void refreshLocation(View view) {
        //get user location if available
        getLocation(currentAddressGPS);
    }


    /**
     * *
     * this method will save the user information into database
     */
    public void userRegistrationButtonPress(View view) {

        String userFirstName = ((EditText) findViewById(R.id.firstNameText)).getText().toString();
        String userLastName = ((EditText) findViewById(R.id.lastNameText)).getText().toString();
        String userPasswordFirst = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        String userPasswordSecond = ((EditText) findViewById(R.id.verifyPasswordText)).getText().toString();
        String userEmailAddress = ((EditText) findViewById(R.id.emailText)).getText().toString();
        String userContactNumber = ((EditText) findViewById(R.id.contactText)).getText().toString();
        String userAddressLineOne = ((EditText) findViewById(R.id.addressLineOneText)).getText().toString();
        String userCityName = ((EditText) findViewById(R.id.cityNameText)).getText().toString();
        String userCountryName = ((EditText) findViewById(R.id.countryNameText)).getText().toString();
        String userPostalCode = ((EditText) findViewById(R.id.postalCodeText)).getText().toString();

        if ( !userPasswordFirst.equals(userPasswordSecond) )
        {
            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Please correct password.", Toast.LENGTH_SHORT).show();
            ((EditText) findViewById(R.id.passwordText)).setHighlightColor(Color.RED);
            ((EditText) findViewById(R. id.verifyPasswordText)).setHighlightColor(Color.RED);
            return;
        }

        //get table
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        ParseUser userTable = new ParseUser();

        //store values
        userTable.setUsername(userEmailAddress);
        userTable.setPassword(userPasswordSecond);
        userTable.setEmail(userEmailAddress);

        userTable.put("userFirstName", userFirstName);
        userTable.put("userLastName", userLastName);
        userTable.put("userContactNumber", userContactNumber);
        userTable.put("userAddressLineOne", userAddressLineOne);
        userTable.put("userCityName", userCityName);
        userTable.put("userCountryName", userCountryName);
        userTable.put("userCountryName", userCountryName);
        userTable.put("userPostalCode", userPostalCode);

        userTable.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(CUserRegistration.this, CLogin.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });




    }
}
