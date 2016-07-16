package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.CircleTransform;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity {


    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.user_info_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUsers;

    private final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        setupToolbar();
        setupDrawer();

        loadUsers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadUsers() {

        showProgress();

        Call<UserListRes> call = mDataManager.getUserList();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                if (response.code() == 200) {


                    try {
                        mUsers = response.body().getData();
                        mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                            @Override
                            public void onUserItemClickListener(int position) {
                                //showSnackbar("Пользователь с индексом " + position);

                                UserDTO userDTO = new UserDTO(mUsers.get(position));

                                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
                                startActivity(profileIntent);


                            }
                        });
                        mRecyclerView.setAdapter(mUsersAdapter);
                    } catch (NullPointerException e) {
                        Log.d(TAG, e.toString());
                        showSnackbar("Ответ 200, но данные не пришли почему-то");
                    }

                } else if (response.code() == 404) {
                    showSnackbar("Неверный логин или пароль");
                } else {
                    showSnackbar("Другая ошибка");

                    //region Logging
                    if (ConstantManager.DEBUG == true) {
                        Log.d(TAG, String.valueOf(response.code()));
                    }
                    //endregion
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // 14.07.2016 обработка ошибок ретрофита
                hideProgress();
                Log.d(TAG, t.toString());

            }
        });
    }

    private void setupDrawer() {


        NavigationView mNavigationView = (NavigationView) findViewById(R.id.user_list_navigation_view);

        ImageView mAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.avatar);

        //устанавливает выбранным пункт меню, где мы находимся
        mNavigationView.getMenu().findItem(R.id.team_menu).setChecked(true);

        Picasso.with(mNavigationDrawer.getContext())
                .load(mDataManager.getPreferencesManager().loadUserAvatar())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(120, 120)
                .centerCrop()
                .placeholder(R.drawable.user_bg)
                .transform(new CircleTransform())
                .into(mAvatar);

        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        //инициализация ФИО в drawere
        TextView mNavTxtNameView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_txt);
        //инициализация email в drawere
        TextView mNavTxtEmailView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email_txt);

        mNavTxtEmailView.setText(userData.get(1)); //Установка email в drawere из SharedPreferences
        mNavTxtNameView.setText(userData.get(userData.size() - 1)); //Установка ФИО в drawere из SharedPreferences




        mNavigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //showSnackbar(item.getTitle().toString());

                switch (item.getItemId()) {
                    case R.id.user_profile_id:
                        item.setChecked(true);
                        Intent openProfile = new Intent(UserListActivity.this, MainActivity.class);
                        startActivity(openProfile);
                        break;
                    case R.id.team_menu:
                        item.setChecked(true);
                        break;
                    case R.id.logout:
                        //mDataManager.setPreferencesManager(null);
                        Intent logout = new Intent(UserListActivity.this, AuthActivity.class);
                        startActivity(logout);
                        break;
                }


                mNavigationDrawer.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
