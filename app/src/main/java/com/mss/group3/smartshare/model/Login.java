package com.mss.group3.smartshare.model;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created on 2016-02-20.
 */
public class Login {

    private String userName;
    private String userPassword;

    public String getUserName()
    {
        return userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserName(String sUserName) {
        this.userName = sUserName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
