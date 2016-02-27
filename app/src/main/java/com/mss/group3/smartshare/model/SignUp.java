package com.mss.group3.smartshare.model;


import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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

    private void setResult(boolean result)
    {
        resultofQuery = result;
    }

    public boolean  registerUser()
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
                    int a = 9;
                    setResult(true);

                } else {
                    setResult(false);

                }
            }
        });

        return resultofQuery;
    }




}
