package com.mss.group3.smartshare.model;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created on 2016-02-20.
 */
public class Login {

    private String sUserName;
    private String sUserPassword;

    public String getUserName()
    {
        return sUserName;
    }

    public String getsUserPassword()
    {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public void setsUserPassword(String sUserPassword) {
        this.sUserPassword = sUserPassword;
    }

    public boolean verifyUserDetails()
    {
        final boolean[] _success = new boolean[1];
        ParseUser.logInInBackground(getUserName(), getsUserPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    _success[0] = true;
                } else {
                    _success[0] = false;
                }
            }
        });
        return  _success[0];
    }
}
