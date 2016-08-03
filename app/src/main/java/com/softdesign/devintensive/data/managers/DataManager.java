package com.softdesign.devintensive.data.managers;

import android.content.Context;
import android.util.Log;

import com.softdesign.devintensive.data.network.PicassoCache;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserLikeResponse;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponseByToken;
import com.softdesign.devintensive.data.network.restmodels.RestService;
import com.softdesign.devintensive.data.network.restmodels.ServiceGenerator;
import com.softdesign.devintensive.data.storage.models.DaoSession;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by anray on 29.06.2016.
 */
public class DataManager {

    private static final String TAG = ConstantManager.TAG_PREFIX + "DataManager";
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

    public Call<ResponseBody> uploadPhoto(String userId, MultipartBody.Part file) {
        return mRestService.uploadPhoto(userId, file);
    }

    public Call<UserLikeResponse> likeUser(String userId) {
        return mRestService.likeUser(userId);
    }
    public Call<UserLikeResponse> unLikeUser(String userId) {
        return mRestService.unLikeUser(userId);
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

    public List<User> getUserListByName(String query) {

        List<User> userList = new ArrayList<>();


        //создаем билдер для построения динамических условий
        QueryBuilder qb = mDaoSession.queryBuilder(User.class);

        //инициализация where условия одним из статичных условий
        WhereCondition whereLike =  UserDao.Properties.CodeLines.gt(0);

        //этот запрос нужен для инициализации where условия, % работает как будто like и нету
        //WhereCondition whereLike = UserDao.Properties.SearchName.like("%");

        // делит строку на слова. \W - не слово
        String[] queryWords = query.split("\\W+");
        for (String word : queryWords) {
            //Log.d(TAG, " getUserListByName " + word);
            // для каждого слова добавляет like запрос
            whereLike = qb.and(whereLike, UserDao.Properties.SearchName.like("%" + word.toUpperCase() + "%"));
        }

        try {
            //запуск запроса
            userList = qb
                    .where(whereLike)
                    .orderDesc(UserDao.Properties.Rating)
                    .build()
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;

    }

    //endregion

}
