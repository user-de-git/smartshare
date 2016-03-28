package com.mss.group3.smartshare.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by user on 2/18/16.
 */
public class InputValidation {


    public static Date DateSetter(String DateTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = null;
        try
        {
            date = simpleDateFormat.parse(DateTime);
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
        return  date;
    }



    public static boolean isValidPhone(String phone) {
        String ePattern = "^[0-9]{10}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(phone);
        return m.matches();
    }


    public static boolean isInt(String inputs) {
        String ePattern = "^[0-9]{1,3}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(inputs);
        return m.matches();
    }



    public static boolean isValidVin(String vin) {
        String ePattern = "^[A-HJ-NPR-Za-hj-npr-z\\\\d]{8}[\\\\dX][A-HJ-NPR-Za-hj-npr-z\\\\d]{2}\\\\d{6}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(vin);
        return m.matches();
    }


    public static boolean isValidPlate(String plate) {
        String ePattern = "^[A-Za-z0-9]{1,7}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(plate);
        return m.matches();
    }





    public static boolean isValidCityCountry(String cityCountry) {
        String ePattern = "[a-zA-Z]{3,}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(cityCountry);
        return m.matches();
    }

    public static boolean isValidPostalCode(String email) {
        String ePattern = "^(?!.*[DFIOQU])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean isDouble(String inputs) {
        String ePattern = "[0-9]{1,13}(\\\\.[0-9]*)?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(inputs);
        return m.matches();
    }






    public static boolean loginInputValidation(String uname, String pword, Context applicationContext) {

        String text;
        if (uname.isEmpty()){
            text = "Please input the username";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if (!isValidEmailAddress(uname)){

            text = "Username is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (pword.isEmpty()){

            text = "Please input the password";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }



    public static boolean signupInputValidation(String firstName, String lastName, String email, String phone,
                                                String firstPass, String secondPass,  String address,
                                                String city, String country, String postalCode,
                                                Context applicationContext) {

        String text;

        if (firstName.isEmpty()){
            text = "Please input the first name";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if (lastName.isEmpty()){

            text = "Please input the last name";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (email.isEmpty()){

            text = "Please input the email address";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidEmailAddress(email)){

            text = "Email address is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (phone.isEmpty()){

            text = "Please input the phone number";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (!isValidPhone(phone)){

            text = "Phone number is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (firstPass.isEmpty()){

            text = "Please input the password";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (secondPass.isEmpty()){

            text = "Please confirm the password";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (!firstPass.equals(secondPass)){

            text = "Password does not match";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (address.isEmpty()){

            text = "Please input the address";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (city.isEmpty()){

            text = "Please input the city";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(city)){

            text = "city is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (country.isEmpty()){

            text = "Please input the country";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (!isValidCityCountry(country)){

            text = "Country is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (postalCode.isEmpty()){

            text = "Please input the postal code";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (!isValidPostalCode(postalCode)){

            text = "Postal code number is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        return false;

    }


    public static boolean recoveryInputValidation(String email, Context applicationContext) {


        if (email.isEmpty()){

            String text = "Please input the email";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if (!isValidEmailAddress(email)){

            String text = "Email address is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }


    public static boolean searchInputValidation(String addressDeparture, String cityDeparture, String countryDeparture, String postalDeparture, String addressArrival, String cityArrival, String countryArrival, String postalArrival, Context applicationContext) {

        String text;

        if (addressDeparture.isEmpty()){
            text = "Please input the Departure address";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (cityDeparture.isEmpty()){

            text = "Please input the Departure city";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(cityDeparture)){

            text = "Departure city is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }



        else if (countryDeparture.isEmpty()){

            text = "Please input the Departure country";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(countryDeparture)){

            text = "Departure country is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (postalDeparture.isEmpty()){

            text = "Please input the Departure postal code";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidPostalCode(postalDeparture)){

            text = "Postal code number is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (addressArrival.isEmpty()){
            text = "Please input the arrival address";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (cityArrival.isEmpty()){

            text = "Please input the Arrival city";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(cityArrival)){

            text = "Arrival city is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (countryArrival.isEmpty()){

            text = "Please input the Arrival country";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(countryArrival)){

            text = "Arrival country is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (postalArrival.isEmpty()){

            text = "Please input the Arrival postal code";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidPostalCode(postalArrival)) {

            text = "Arrival Postal code number is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }


    public static boolean postInputValidation(String vinNumber, String plate, String range, String price, String addressArrival, String cityArrival, String countryArrival, String postalArrival, Context applicationContext) {

        String text;

        if (vinNumber.isEmpty()){
            text = "Please input the Vin number";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if(!isValidVin(vinNumber)){

            text = "Vin number not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if (plate.isEmpty()){

            text = "Please input the plate";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if(!isValidPlate(plate)){

            text = "Plate number not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if (range.isEmpty()){

            text = "Please input the range";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if(!isInt(range)){

            text = "Range not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (price.isEmpty()){

            text = "Please input the price";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if(!isDouble(price)){

            text = "Price not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }

        else if (addressArrival.isEmpty()){
            text = "Please input the arrival address";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (cityArrival.isEmpty()){

            text = "Please input the Arrival city";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(cityArrival)){

            text = "Arrival city is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (countryArrival.isEmpty()){

            text = "Please input the Arrival country";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidCityCountry(countryArrival)){

            text = "Arrival country is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;

        }


        else if (postalArrival.isEmpty()){

            text = "Please input the Arrival postal code";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }


        else if (!isValidPostalCode(postalArrival)) {

            text = "Arrival Postal code number is not valid";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }





    public static boolean resetPass(String firstPass, String secondPass, Context applicationContext) {

        String text;

        if (firstPass.isEmpty()){

            text = "Please input the password";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (secondPass.isEmpty()){

            text = "Please confirm the password";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        else if (!firstPass.equals(secondPass)){

            text = "Password does not match";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }






}
