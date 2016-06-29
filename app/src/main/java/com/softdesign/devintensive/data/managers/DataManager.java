package com.softdesign.devintensive.data.managers;

/**
 * Created by anray on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;


    private PreferencesManager mPreferencesManager;


    private DataManager() {
        this.mPreferencesManager = new PreferencesManager();

    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();

        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

}
