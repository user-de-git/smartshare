package com.mss.group3.smartshare.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

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
}
