package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.network.restmodels.RestService;
import com.softdesign.devintensive.data.network.restmodels.ServiceGenerator;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import retrofit2.Call;

/**
 * Created by anray on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;

    private Context mContext;
    private PreferencesManager mPreferencesManager;

    private RestService mRestService;


    private DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);

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

    public Context getContext(){
        return mContext;
    }

    //region =====================Network===================
    public Call<UserModelResponse> loginUser(UserLoginRequest userLoginRequest){
        return mRestService.loginUser(userLoginRequest);
    }
    //endregion

    //region =====================Database===================

    //endregion

}
