package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.InputValidation;
import com.mss.group3.smartshare.model.SignUp;
import com.mss.group3.smartshare.utility.LocationServices;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * Created by group three on 2016-02-07.
 */
public class UserRegistrationController extends Activity {


    SignUp signUpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userregistration);

        //get model
        signUpModel = new SignUp();

        //get user location if available
        getLocation(signUpModel.currentAddressGPS);
    }


    private void getLocation(String address) {

        LocationServices mLocationServices = new LocationServices(this);
        mLocationServices.getLocation();

        if (mLocationServices.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, " +
                    "Enter address manually or Press refresh after enabling location.", Toast.LENGTH_SHORT).show();
            return;

        } else {

            // Getting location co-ordinates
            double latitude = mLocationServices.getLatitude();
            double longitude = mLocationServices.getLongitude();

            address = mLocationServices.getLocationAddress();
            //add default location
            ((EditText) findViewById(R.id.addressLineOneText)).setText(mLocationServices.getLineOneAddress());
            ((EditText) findViewById(R.id.cityNameText)).setText(mLocationServices.getLocationCity());
            ((EditText) findViewById(R.id.countryNameText)).setText(mLocationServices.getLocationCountry());
            ((EditText) findViewById(R.id.postalCodeText)).setText(mLocationServices.getPostalCode());

        }

        // make sure you close the gps after using it. Save user's battery power
        mLocationServices.closeGPS();
    }


    /**
     * *
     * refresh location
     */
    public void refreshLocation(View view) {
        //get user location if available
        getLocation(signUpModel.currentAddressGPS);
    }

    /**
     * *
     * this method will save the user information into database
     */
    public void userRegistrationButtonPress(View view) {

        signUpModel.userFirstName = ((EditText) findViewById(R.id.firstNameText)).getText().toString();
        signUpModel.userLastName = ((EditText) findViewById(R.id.lastNameText)).getText().toString();
        signUpModel.userPasswordFirst = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        signUpModel.userPasswordSecond = ((EditText) findViewById(R.id.verifyPasswordText)).getText().toString();
        signUpModel.userEmailAddress = ((EditText) findViewById(R.id.emailText)).getText().toString();
        signUpModel.userContactNumber = ((EditText) findViewById(R.id.contactText)).getText().toString();
        signUpModel.userAddressLineOne = ((EditText) findViewById(R.id.addressLineOneText)).getText().toString();
        signUpModel.userCityName = ((EditText) findViewById(R.id.cityNameText)).getText().toString();
        signUpModel.userCountryName = ((EditText) findViewById(R.id.countryNameText)).getText().toString();
        signUpModel.userPostalCode = ((EditText) findViewById(R.id.postalCodeText)).getText().toString();

//        if ( !signUpModel.userPasswordFirst.equals(signUpModel.userPasswordSecond) )
//        {
//            // Here you can ask the user to try again, using return; for that
//            Toast.makeText(getApplicationContext(), "Please correct password.", Toast.LENGTH_SHORT).show();
//            ((EditText) findViewById(R.id.passwordText)).setHighlightColor(Color.RED);
//            ((EditText) findViewById(R. id.verifyPasswordText)).setHighlightColor(Color.RED);
//            return;
//        }


        if (InputValidation.signupInputValidation(signUpModel.userFirstName, signUpModel.userLastName, signUpModel.userEmailAddress, signUpModel.userContactNumber, signUpModel.userPasswordFirst, signUpModel.userPasswordSecond, signUpModel.userAddressLineOne, signUpModel.userCityName, signUpModel.userCountryName, signUpModel.userPostalCode, this)){

            return;

        }
        signUpModel.context = getApplicationContext();
        signUpModel.uRegisterationCon = this;
        signUpModel.registerUser();


    }

    public  void moveToLogin()
    {
        startActivity(new Intent(UserRegistrationController.this, MainController.class));
    }
}
