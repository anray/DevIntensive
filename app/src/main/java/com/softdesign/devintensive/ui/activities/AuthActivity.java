package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponseByToken;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.SaveUsersToDbChronos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.login_email_et)
    EditText mLogin;

    @BindView(R.id.login_password_et)
    EditText mPassword;

    @BindView(R.id.login_button_btn)
    Button mSignIn;

    @BindView(R.id.forgot_password_tv)
    TextView mForgotPassword;

    @BindView(R.id.auth_main_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.authorization_box)
    CardView mCardView;

    private DataManager mDataManager;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;
    private ChronosConnector mConnector;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        setContentView(R.layout.activity_auth);


        ButterKnife.bind(this);


        mDataManager = DataManager.getInstance();

        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();


        mSignIn.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);


        signInByToken();




    }


    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        mConnector.onPause();
        super.onPause();
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button_btn:
                //openMainActivity();
                signIn();


                break;
            case R.id.forgot_password_tv:
                forgotPassword();
                break;
        }

    }

    public void openMainActivity() {
        Intent openMainActivity = new Intent(this, MainActivity.class);
        startActivity(openMainActivity);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();

    }

    private void forgotPassword() {
        Intent rememberPasswordIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberPasswordIntent);
    }

    private void loginSuccessByToken(UserModelResponseByToken userModelByToken) {

        //region=============Splash Screen
        showProgress();
        mCardView.setVisibility(View.GONE);
        //endregion

        //showSnackbar(userModel.getData().getToken());
        //mDataManager.getPreferencesManager().saveAuthToken(userModelByToken.ge);
        //mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getUserId());

        saveUserValuesByToken(userModelByToken);
        saveUserProfileDetailsByToken(userModelByToken);
        saveUserProfileImageByToken(userModelByToken);
        saveUserAvatarImageByToken(userModelByToken);
        saveUsersInDb();


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
//                startActivity(loginIntent);
//            }
//        }, AppConfig.START_DELAY);


    }


    private void loginSuccess(UserModelResponse userModel) {

        //region=============Splash Screen
        showProgress();
        mCardView.setVisibility(View.GONE);
        //endregion

        //showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getUserId());

        saveUserValues(userModel);
        saveUserProfileDetails(userModel);
        saveUserProfileImage(userModel);
        saveUserAvatarImage(userModel);
        saveUsersInDb();


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
//                startActivity(loginIntent);
//            }
//        }, AppConfig.START_DELAY);


    }

    public void onOperationFinished(final SaveUsersToDbChronos.Result result) {
        hideProgress();

        //  20.07.2016 Переход в UserListActivity
        if (result.isSuccessful()) {

            Intent loginIntent = new Intent(AuthActivity.this, UserListActivity.class);
            startActivity(loginIntent);
        } else {
            Log.d(TAG, result.getErrorMessage().toString());
        }

        finish();

    }

    private void signInByToken() {

        if (NetworkStatusChecker.isNetworkAvailable(this)) {

            String tempid = mDataManager.getPreferencesManager().getUserId();

            Call<UserModelResponseByToken> call = mDataManager.loginUserByToken(tempid);
            call.enqueue(new Callback<UserModelResponseByToken>() {
                @Override
                public void onResponse(Call<UserModelResponseByToken> call, Response<UserModelResponseByToken> response) {
                    if (response.code() == 200) {

                        loginSuccessByToken(response.body());


                    } else {

                        mCardView.setVisibility(View.VISIBLE);
                        return;

                    }
                }

                @Override
                public void onFailure(Call<UserModelResponseByToken> call, Throwable t) {

                    Log.d(TAG, "in onFailure" + t.toString());
                    //showSnackbar("Произошла ошибка чтения с сервера, попробуйте еще раз");
                    mCardView.setVisibility(View.VISIBLE);
                    return;

                }
            });

        } else {
            mCardView.setVisibility(View.VISIBLE);
            return;
            //showSnackbar("Сеть недоступна, попробуйте позже");
        }
    }


    private void signIn() {

        if (NetworkStatusChecker.isNetworkAvailable(this)) {

            Call<UserModelResponse> call = mDataManager.loginUser(new UserLoginRequest(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelResponse>() {
                @Override
                public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());


                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль");
                    } else {
                        showSnackbar("Другая ошибка");

                        //region Logging
                        if (ConstantManager.DEBUG == true) {
                            Log.d(ConstantManager.TAG_PREFIX, String.valueOf(response.code()));
                        }
                        //endregion
                    }
                }

                @Override
                public void onFailure(Call<UserModelResponse> call, Throwable t) {

                    Log.d(TAG, "in onFailure" + t.toString());
                    showSnackbar("Произошла ошибка чтения с сервера, попробуйте еще раз");

                }
            });

        } else {
            showSnackbar("Сеть недоступна, попробуйте позже");
        }
    }

    private void saveUserValues(UserModelResponse userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRaiting(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);


    }

    private void saveUserValuesByToken(UserModelResponseByToken userModel) {
        int[] userValues = {
                userModel.getData().getProfileValues().getRaiting(),
                userModel.getData().getProfileValues().getLinesCode(),
                userModel.getData().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);


    }

    private void saveUserProfileDetails(UserModelResponse userModel) {
        List<String> userValues = new ArrayList<>();
        userValues.add(userModel.getData().getUser().getContacts().getPhone());
        userValues.add(userModel.getData().getUser().getContacts().getEmail());
        userValues.add(userModel.getData().getUser().getContacts().getVk());


        List<UserModelResponse.Repo> repo = (userModel.getData().getUser().getRepositories().getRepo());
        for (int i = 0; i < 3; i++) {
            String gitTitle = "";
            try {
                gitTitle = repo.get(i).getGit();
            } catch (Exception ex) {
                gitTitle = "No_" + (i + 1) + "_git_repo";
            }

            userValues.add(gitTitle);

        }

//        userValues.add((userModel.getData().getUser().getRepositories().getRepo()).get(0).getGit());
//        userValues.add((userModel.getData().getUser().getRepositories().getRepo()).get(1).getGit());
//        userValues.add((userModel.getData().getUser().getRepositories().getRepo()).get(2).getGit());
        userValues.add(userModel.getData().getUser().getPublicInfo().getBio());
        userValues.add(userModel.getData().getUser().getFirstName() + " " + userModel.getData().getUser().getSecondName());


        mDataManager.getPreferencesManager().saveUserProfileData(userValues);


    }

    private void saveUserProfileDetailsByToken (UserModelResponseByToken userModel) {
        List<String> userValues = new ArrayList<>();
        userValues.add(userModel.getData().getContacts().getPhone());
        userValues.add(userModel.getData().getContacts().getEmail());
        userValues.add(userModel.getData().getContacts().getVk());


        List<UserModelResponse.Repo> repo = (userModel.getData().getRepositories().getRepo());
        for (int i = 0; i < 3; i++) {
            String gitTitle = "";
            try {
                gitTitle = repo.get(i).getGit();
            } catch (Exception ex) {
                gitTitle = "No_" + (i + 1) + "_git_repo";
            }

            userValues.add(gitTitle);

        }

//        userValues.add((userModel.getData().getUser().getRepositories().getRepo()).get(0).getGit());
//        userValues.add((userModel.getData().getUser().getRepositories().getRepo()).get(1).getGit());
//        userValues.add((userModel.getData().getUser().getRepositories().getRepo()).get(2).getGit());
        userValues.add(userModel.getData().getPublicInfo().getBio());
        userValues.add(userModel.getData().getFirstName() + " " + userModel.getData().getSecondName());


        mDataManager.getPreferencesManager().saveUserProfileData(userValues);


    }

    private void saveUserProfileImage(UserModelResponse userModel) {

        Uri photoUrl = Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto());
        mDataManager.getPreferencesManager().saveUserPhoto(photoUrl);


    }

    private void saveUserProfileImageByToken(UserModelResponseByToken userModel) {

        Uri photoUrl = Uri.parse(userModel.getData().getPublicInfo().getPhoto());
        mDataManager.getPreferencesManager().saveUserPhoto(photoUrl);


    }

    private void saveUserAvatarImage(UserModelResponse userModel) {

        Uri photoUrl = Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar());
        mDataManager.getPreferencesManager().saveUserAvatar(photoUrl);


    }

    private void saveUserAvatarImageByToken(UserModelResponseByToken userModel) {

        Uri photoUrl = Uri.parse(userModel.getData().getPublicInfo().getAvatar());
        mDataManager.getPreferencesManager().saveUserAvatar(photoUrl);


    }

    private void saveUsersInDb() {


        Call<UserListRes> call = mDataManager.getUserListFromNetwork();
        call.enqueue(new Callback<UserListRes>() {
                         @Override
                         public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {

                             try {

                                 if (response.code() == 200) {


                                     mConnector.runOperation(new SaveUsersToDbChronos(response.body().getData()), false);


                                 } else {
                                     showSnackbar("Список пользователей не может быть получен");
                                     Log.e(TAG, "onResponse: " + String.valueOf(response.errorBody().source()));
                                 }


                             } catch (NullPointerException e) {
                                 e.printStackTrace();
                                 Log.d(TAG, e.toString());
                                 showSnackbar("Ответ 200, но данные не пришли почему-то");
                             }


                         }

                         @Override
                         public void onFailure(Call<UserListRes> call, Throwable t) {
                             // 14.07.2016 обработка ошибок ретрофита

                             Log.d(TAG, t.toString());

                         }
                     }

        );
    }

//    private List<Repository> getRepoListFromUserRes(UserListRes.UserData userData) {
//        final String userId = userData.getId();
//
//        List<Repository> repositories = new ArrayList<>();
//        for (UserModelResponse.Repo repositoryRes : userData.getRepositories().getRepo()) {
//            repositories.add(new Repository(repositoryRes, userId));
//        }
//
//        return repositories;
//    }


}
