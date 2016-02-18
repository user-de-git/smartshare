package com.mss.group3.smartshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

public class CLogin extends AppCompatActivity {

    static int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clogin);
        if(counter==0) {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this);
            ParseUser.enableRevocableSessionInBackground();
            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();
            ParseACL.setDefaultACL(defaultACL, true);
        }
        counter++;
    }

    public void buttonPress(View view) {
        String uname = ((EditText)findViewById(R.id.uname)).getText().toString();
        String pword = ((EditText)findViewById(R.id.pword)).getText().toString();
        ParseUser.logInInBackground(uname, pword, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent CUserType = new Intent(CLogin.this, CUserType.class);
                    startActivity(CUserType);
                } else {
                    String text = "Login failed";
                    Toast.makeText(CLogin.this, text, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * The following method will get triggered on sign up click
     */
    public void signUpEvent( View view )
    {
        //go to user registration page
        startActivity(new Intent(CLogin.this, CUserRegistration.class));
    }


}
