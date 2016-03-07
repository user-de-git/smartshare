package com.mss.group3.smartshare.model;

/**
 * Created by Bhupinder on 3/6/2016.
 */
public class UserSingleton {
    private static UserSingleton ourInstance = new UserSingleton();

    public static UserSingleton getInstance() {
        return ourInstance;
    }

    public String emailAddress;

    private UserSingleton() {
    }
}
