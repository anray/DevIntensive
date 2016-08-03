package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.storage.models.Likes;
import com.softdesign.devintensive.data.storage.models.LikesDao;
import com.softdesign.devintensive.data.storage.models.Repository;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anray on 20.07.2016.
 */
public class SaveUsersToDbChronos extends ChronosOperation<String> {


    private List<UserListRes.UserData> mResponse;

    static final String TAG = ConstantManager.TAG_PREFIX + "SaveUsersToDbChrono";

    public SaveUsersToDbChronos(List<UserListRes.UserData> response) {
        mResponse = response;
    }


    @Nullable
    @Override
    public String run() {
        DataManager mDataManager = DataManager.getInstance();
        RepositoryDao mRepositoryDao;
        UserDao mUserDao;
        LikesDao mLikesDao;

        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();
        mLikesDao = mDataManager.getDaoSession().getLikesDao();


        List<Likes> allLikes = new ArrayList<>();
        List<Repository> allRepositories = new ArrayList<Repository>();
        List<User> allUsers = new ArrayList<User>();

        for (UserListRes.UserData userRes : mResponse) {

            allLikes.addAll(getLikesListFromUserRes(userRes));
            allRepositories.addAll(getRepoListFromUserRes(userRes));
            allUsers.add(new User(userRes));

            //allRepositories.addAll(userRes.getRepositories().getRepo());


        }


        mRepositoryDao.insertOrReplaceInTx(allRepositories);
        mUserDao.insertOrReplaceInTx(allUsers);
        mLikesDao.deleteAll();
        mLikesDao.insertOrReplaceInTx(allLikes);

        mResponse = null;

        return null;

    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<String>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<String> {
    }


    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Repository> repositories = new ArrayList<>();
        for (UserModelResponse.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repository(repositoryRes, userId));
        }

        return repositories;
    }

    private List<Likes> getLikesListFromUserRes(UserListRes.UserData userData) {
        final String userId = userData.getId();

        List<Likes> likes = new ArrayList<>();

        for (String likeRes : userData.getProfileValues().getLikesBy()) {
            likes.add(new Likes(likeRes, userId));
        }

        return likes;
    }

}
