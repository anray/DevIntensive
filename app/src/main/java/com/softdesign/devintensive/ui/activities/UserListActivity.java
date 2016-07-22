package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.storage.models.RepositoryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.CircleTransform;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.softdesign.devintensive.utils.LoadUsersFromDbChronos;
import com.softdesign.devintensive.utils.SimpleItemTouchHelperCallback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity {


    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.user_list_navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.user_info_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private MenuItem mSearchItem;

    private List<UserListRes.UserData> mFilteredResponse = new ArrayList<UserListRes.UserData>();

    private final String TAG = ConstantManager.TAG_PREFIX + "UserListActivity";
    private String mQuery;
    private Handler mHandler;
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;
    private ChronosConnector mConnector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mUserDao = mDataManager.getDaoSession().getUserDao();
        mRepositoryDao = mDataManager.getDaoSession().getRepositoryDao();


        mHandler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        setupToolbar();


        //start Chronus operation
        mConnector.runOperation(new LoadUsersFromDbChronos(), false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();

        //необходимо здесь чтобы при нажатии Back выделенная опция меню соответствовала текущей активити
        setupDrawer();
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

    /**
     * запускается по окончании Chronus потока
     *
     * @param result Список пользователей из БД
     */
    public void onOperationFinished(final LoadUsersFromDbChronos.Result result) {
        if (result.isSuccessful()) {
            showUsers(result.getOutput());
        } else {
            Log.d(TAG, result.getErrorMessage().toString());
        }
    }


    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    private void setupDrawer() {


        //инициализация аватарки
        ImageView mAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.avatar);

        //устанавливает выбранным пункт меню, где мы находимся
        mNavigationView.getMenu().findItem(R.id.team_menu).setChecked(true);

        Picasso.with(mNavigationDrawer.getContext())
                .load(mDataManager.getPreferencesManager().loadUserAvatar())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                //.resize(120, 120)
                .fit()
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
                        DevintensiveApplication.getSharedPreferences().edit().clear().apply();
                        finishAffinity();
                        break;
                }


                mNavigationDrawer.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

    /**
     * По нажатию на гамбургер меню открывает дравер
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        Log.d(TAG, "onOptionsItemSelected: ");

        return super.onOptionsItemSelected(item);
    }

    /**
     * Открывает drawer по нажатию хардовой кнопки меню
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        mNavigationDrawer.openDrawer(GravityCompat.START);
        Log.d(TAG, "onMenuOpened: ");

        //return super.onMenuOpened(featureId, menu);
        return false;
    }

    /**
     * Задает параметры панели поиска, после того как нажата лупа
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setQueryHint(getString(R.string.query_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // 19.07.2016 Вызвает поиск тут
                showUsersByQuery(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Закрывает боковую менюшку по нажатию Back (вместо выхода из приложения)
     */
    @Override
    public void onBackPressed() {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);

        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            //нужно чтобы по второму нажатию Back сворачивалось поле поиска (по первому прячется клавиатура)
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }


    }


    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Показывает профили людей, а также добавляет листенеры на каждую карточку
     * @param users
     */
    private void showUsers(List<User> users) {
        mUsers = users;

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
        mRecyclerView.swapAdapter(mUsersAdapter, false);

        //region ====== присоединил чтобы был свайп
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mUsersAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        //endregion
    }


    private void showUsersByQuery(String query) {
        mQuery = query;

        if (mQuery.isEmpty()) {
            //вернет список всех людей, отрабатывает очень быстро, можно даже без отдельного потока. (выходит что like(%%) работает как будто его и нет)
            //showUsers(mDataManager.getUserListByName(mQuery));

            //start Chronus operation
            mConnector.runOperation(new LoadUsersFromDbChronos(), false);
        } else {

            Runnable searchUsers = new Runnable() {
                @Override
                public void run() {
                    showUsers(mDataManager.getUserListByName(mQuery));

                }
            };

            mHandler.removeCallbacks(searchUsers);
            mHandler.postDelayed(searchUsers, ConstantManager.SEARCH_DELAY);

        }
    }


//    private void loadUsersFromDb() {
//
//
//        if (mDataManager.getUserListFromDb().size() == 0) {
//            showSnackbar("Список пользователей пуст, не может быть загружен");
//        } else {
//
//            showUsers(mDataManager.getUserListFromDb());
//        }

//        showProgress();
//
//        Call<UserListRes> call = mDataManager.getUserListFromNetwork();
//        call.enqueue(new Callback<UserListRes>() {
//            @Override
//            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
//                if (response.code() == 200) {
//
//                    try {
//
//                        mUsers = response.body().getData();
//
//                        mFilteredResponse.clear();
//
//                        if (query != null && !query.isEmpty()) {
//
//
//                            for (int i = 0; i < mUsers.size(); i++) {
//                                if (mUsers.get(i).getFullName().toLowerCase().trim().contains(query.toLowerCase().trim())) {
//                                    mFilteredResponse.add(mUsers.get(i));
//                                }
//
//                            }
//                        } else {
//                            mFilteredResponse = mUsers;
//                        }
//
//
//                        mUsersAdapter = new UsersAdapter(mFilteredResponse, new UsersAdapter.UserViewHolder.CustomClickListener() {
//                            @Override
//                            public void onUserItemClickListener(int position) {
//                                //showSnackbar("Пользователь с индексом " + position);
//
//                                UserDTO userDTO = new UserDTO(mFilteredResponse.get(position));
//
//                                Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
//                                profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);
//                                startActivity(profileIntent);
//
//
//                            }
//                        });
//                        mRecyclerView.setAdapter(mUsersAdapter);
//                    } catch (NullPointerException e) {
//                        Log.d(TAG, e.toString());
//                        showSnackbar("Ответ 200, но данные не пришли почему-то");
//                    }
//
//                } else if (response.code() == 404) {
//                    showSnackbar("Неверный логин или пароль");
//                } else {
//                    showSnackbar("Другая ошибка");
//
//                    //region Logging
//                    if (ConstantManager.DEBUG == true) {
//                        Log.d(TAG, String.valueOf(response.code()));
//                    }
//                    //endregion
//                }
//                hideProgress();
//            }
//
//            @Override
//            public void onFailure(Call<UserListRes> call, Throwable t) {
//                // 14.07.2016 обработка ошибок ретрофита
//                hideProgress();
//                Log.d(TAG, t.toString());
//
//            }
//        });
//    }

    //    /**
//     * По нажатию на гамбургер меню открывает дравер
//     * @param item
//     * @return
//     */
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            mNavigationDrawer.openDrawer(GravityCompat.START);
//        }
//
//        Log.d(TAG, "onContextItemSelected: ");
//
//        return super.onContextItemSelected(item);
//    }
}
