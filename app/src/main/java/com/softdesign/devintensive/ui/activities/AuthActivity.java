package com.softdesign.devintensive.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        //mSignIn = (Button) findViewById(R.id.login_button_btn);
        mSignIn.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button_btn:
                //openMainActivity();

                loginSuccess();
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

    private void loginSuccess() {
        showSnackbar("Вход");
    }

}
