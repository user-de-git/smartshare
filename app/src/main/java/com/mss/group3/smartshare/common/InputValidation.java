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
    public static void loginInputValidation(String uname, String pword, Context applicationContext) {


        if (uname.isEmpty()){
            String text = "Please input the username";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return;

        }

        else if (pword.isEmpty()){

            String text = "Please input the password";
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
            return;
        }

    }

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
}
