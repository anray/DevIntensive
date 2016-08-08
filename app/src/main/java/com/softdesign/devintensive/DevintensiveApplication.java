package com.softdesign.devintensive;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.softdesign.devintensive.data.storage.models.DaoMaster;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.utils.ConstantManager;

import org.greenrobot.greendao.database.Database;

/**
 * Created by anray on 28.06.2016.
 */
public class DevintensiveApplication extends Application {

    private static Context sContext;
    public static SharedPreferences sSharedPreferences;
    private static DaoSession sDaoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devintensive-db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);



        if (ConstantManager.DEBUG == true) {
            Log.d(ConstantManager.TAG_PREFIX, "DevintensiveApplication class" + this.getClass().getName());
        }



    }

    public static SharedPreferences getSharedPreferences() {

        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
