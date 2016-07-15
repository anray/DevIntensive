package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {


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
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // TODO: 14.07.2016 обработать ошибки ретрофита
                Log.d(TAG, t.toString());

            }
        });
    }

    private void setupDrawer() {
        // TODO: 14.07.2016 Реализовать переход в другую активити при клике по элементу меню в Navigation Drawer
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
