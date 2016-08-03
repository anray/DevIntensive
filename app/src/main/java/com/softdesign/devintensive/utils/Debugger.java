package com.softdesign.devintensive.utils;

import android.support.design.BuildConfig;
import android.util.Log;

/**
 * Created by anray on 03.08.2016.
 */
public class Debugger {

    public static void writeLog(String message) {
        if (!BuildConfig.DEBUG) {
            Log.d(ConstantManager.TAG_PREFIX, message);
        }
    }

}
