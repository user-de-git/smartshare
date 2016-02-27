package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.UserType;
import com.mss.group3.smartshare.model.Login;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created on 2016-02-20.
 * The login controller handle request from login, signup and forgot password view
 */
public class LoginController extends Activity {

    public Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //the first screen to be initialized is login
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //initialize login
        login = new Login();
    }

    public void loginButtonClick(View view ) {

        login.setUserName(((EditText) findViewById(R.id.userName)).getText().toString());
        login.setUserPassword(((EditText) findViewById(R.id.userPassword)).getText().toString());

        ParseUser.logInInBackground(login.getUserName(), login.getUserPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    //login screen initialization
                    Intent myIntent = new Intent(LoginController.this, UserType.class);
                    startActivity(myIntent);

                } else {

                }
            }
        });

    }

    public void signUpButtonClick(View view ) {

        //login screen initialization
            Intent myIntent = new Intent(LoginController.this, UserRegistrationController.class);
            startActivity(myIntent);
    }

    public void forgotPasswordButtonClick(View view ) {
        //need to implement

    }

}
