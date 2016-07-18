package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

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

    private DataManager mDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        //mSignIn = (Button) findViewById(R.id.login_button_btn);
        mSignIn.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);


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

    private void loginSuccess(UserModelResponse userModel) {

        //showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getUserId());
        saveUserValues(userModel);
        saveUserProfileDetails(userModel);
        saveUserProfileImage(userModel);
        saveUserAvatarImage(userModel);

        Intent loginIntent = new Intent(this, UserListActivity.class);
        startActivity(loginIntent);
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

                    Log.d(TAG, t.toString());

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

    private void saveUserProfileDetails(UserModelResponse userModel) {
        List<String> userValues = new ArrayList<>();
        userValues.add(userModel.getData().getUser().getContacts().getPhone());
        userValues.add(userModel.getData().getUser().getContacts().getEmail());
        userValues.add(userModel.getData().getUser().getContacts().getVk());


        List<UserModelResponse.Repo> repo = (userModel.getData().getUser().getRepositories().getRepo());
        for (int i = 0; i<3;i++){
            String gitTitle = "";
            try {
                gitTitle = repo.get(i).getGit();
            } catch (Exception ex) {
                gitTitle = "No_" + (i+1) + "_git_repo";
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

    private void saveUserProfileImage(UserModelResponse userModel) {

        Uri photoUrl = Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto());
        mDataManager.getPreferencesManager().saveUserPhoto(photoUrl);


    }

    private void saveUserAvatarImage(UserModelResponse userModel) {

        Uri photoUrl = Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar());
        mDataManager.getPreferencesManager().saveUserAvatar(photoUrl);


    }

}
