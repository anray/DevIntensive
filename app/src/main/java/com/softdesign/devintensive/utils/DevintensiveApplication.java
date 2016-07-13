package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by anray on 28.06.2016.
 */
public class DevintensiveApplication extends Application {

    private static Context sContext;
    public static SharedPreferences sSharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;
        if (ConstantManager.DEBUG == true) {
            Log.d(ConstantManager.TAG_PREFIX + "DevintensiveApplication class", this.getClass().getName());
        }


    }

    public static SharedPreferences getSharedPreferences() {

        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

}
