package com.belivit.todoonline.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String mSharedName = "loginPref";
    public static final String USER_ID = "loginCustId";
    public static final String USER_NAME = "loginCustName";
    public static final String USER_EMAIL = "loginCustEmail";
    public static final String IS_LOGGED_IN = "isLogin";



    public static void saveShared(Context c, String type, String val) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed.putString(type, val);
        ed.commit();
    }

    public static void saveSharedBoolean(Context c, String type, boolean val) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed.putBoolean(type, val);
        ed.apply();
    }


    public static void removeShared(Context c, String type) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed.remove(type);
        ed.commit();
    }

    public static void removeSharedAll(Context c) {
        SharedPreferences.Editor ed = c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).edit();
        ed.clear();
        ed.commit();
    }

    public static String getShared(Context c, String type) {
        return c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).getString(type, "");
    }

    public static boolean getIsLoggedIn(Context c, String type) {
        return c.getSharedPreferences(mSharedName, Context.MODE_PRIVATE).getBoolean(type, false);
    }
}

