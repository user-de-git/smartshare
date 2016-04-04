package com.mss.group3.smartshare.model;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mss.group3.smartshare.controller.MainController;
import com.mss.group3.smartshare.controller.UserRegistrationController;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by inder on 2016-02-20.
 */
public class SignUp {

    public String userFirstName;
    public String userLastName;
    public String userPasswordFirst;
    public String userPasswordSecond;
    public String userEmailAddress;
    public String userContactNumber;
    public String userAddressLineOne;
    public String userCityName;
    public String userCountryName;
    public String userPostalCode;
    public String currentAddressGPS;
    public boolean resultofQuery;
    public static Context context;
    public static UserRegistrationController uRegisterationCon;
    private ParseObject parseObject;

    private void setResult(boolean result)
    {
        resultofQuery = result;
    }

    public void  registerUser()
    {


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

                    parseObject = new ParseObject("LocationTracking");
                    parseObject.put("Email", userEmailAddress);
                    parseObject.saveInBackground();
                    Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show();
                    uRegisterationCon.moveToLogin();



                } else {

                    Toast.makeText( context, "Registration Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }




}
