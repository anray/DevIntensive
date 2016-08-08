package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.DevintensiveApplication;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.List;



/**
 * Created by anray on 20.07.2016.
 */
public class LoadUsersFromDbChronos extends ChronosOperation<List<User>> {


    @Nullable
    @Override
    public List<User> run() {

        // here you should write what you do to get the BusinessObject
       // try {

            return   DevintensiveApplication.getDaoSession().queryBuilder(User.class)
                    //.where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.Rating)
                    .build()
                    .list();

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass(){
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<User>> {
    }
}
