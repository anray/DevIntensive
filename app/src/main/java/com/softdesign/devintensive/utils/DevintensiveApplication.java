package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by anray on 28.06.2016.
 */
public class DevintensiveApplication extends Application {

    public static SharedPreferences sSharedPreferences;




    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public static SharedPreferences getSharedPreferences() {

        return sSharedPreferences;
    }

}
