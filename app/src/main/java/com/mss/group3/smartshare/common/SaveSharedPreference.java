package com.mss.group3.smartshare.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by inder on 2016-03-25.
 */
public class SaveSharedPreference
{
    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_Password = "password";
    static final String PREF_USER_Terms = "terms";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setPassword(Context ctx, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_Password, password);
        editor.commit();
    }

    public static String getPassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_Password, "");
    }

    public static void setTerms(Context ctx, String terms)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_Terms, terms);
        editor.commit();
    }

    public static String getTerms(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_Terms, "");
    }
}
