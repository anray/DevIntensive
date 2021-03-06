package com.softdesign.devintensive.data.managers;

import android.content.Context;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponseByToken;
import com.softdesign.devintensive.data.network.restmodels.RestService;
import com.softdesign.devintensive.data.network.restmodels.ServiceGenerator;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by anray on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;

    private Context mContext;
    private PreferencesManager mPreferencesManager;
    private Picasso mPicasso;

    private RestService mRestService;

    private DaoSession mDaoSession;


    private DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession = DevintensiveApplication.getDaoSession();

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

    public Context getContext() {
        return mContext;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    //region =====================Network===================
    public Call<UserModelResponse> loginUser(UserLoginRequest userLoginRequest) {
        return mRestService.loginUser(userLoginRequest);
    }

    public Call<UserModelResponseByToken> loginUserByToken(String userId) {
        return mRestService.loginUserByToken(userId);
    }

    public Call<ResponseBody> uploadPhoto (String userId, MultipartBody.Part file) {
        return mRestService.uploadPhoto(userId, file);
    }


    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }


    //endregion

    //region =====================Database===================


    public DaoSession getDaoSession() {
        return mDaoSession;
    }

//    public List<User> getUserListFromDb() {
//        List<User> userList = new ArrayList<>();
//
//        try {
//
//            userList = mDaoSession.queryBuilder(User.class)
//                    .where(UserDao.Properties.CodeLines.gt(0))
//                    .orderDesc(UserDao.Properties.CodeLines)
//                    .build()
//                    .list();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return userList;
//    }

    public List<User> getUserListByName(String query){

        List<User> userList = new ArrayList<>();

        try{
            //выходит что like(%%) работает как будто его и нет
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"), UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.Rating)
                    .build()
                    .list();

        }catch (Exception e){
            e.printStackTrace();
        }

        return userList;

    }

    //endregion

}
