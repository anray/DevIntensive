package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.TextWatcherValidator;
import com.softdesign.devintensive.utils.CircleTransform;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private int mCurrentEditMode = 0;

    private DataManager mDataManager;

    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.user_info_user_photo_main_iv)
    ImageView mProfileImage;

    @BindView(R.id.send_email_iv)
    ImageView mProfileEmail;

    @BindView(R.id.open_VK_profile_iv)
    ImageView mProfileVk;

    @BindView(R.id.open_repository1_iv)
    ImageView mProfileGithub1;

    @BindView(R.id.open_repository2_iv)
    ImageView mProfileGithub2;

    @BindView(R.id.open_repository3_iv)
    ImageView mProfileGithub3;

    @BindView(R.id.call)
    ImageView mProfileTel;

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.user_info_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.phone_et)
    EditText mUserPhone;

    @BindView(R.id.email_et)
    EditText mUserMail;

    @BindView(R.id.vk_profile_et)
    EditText mUserVk;

    @BindView(R.id.git_repository1_et)
    EditText mUserGit1;

    @BindView(R.id.git_repository2_et)
    EditText mUserGit2;

    @BindView(R.id.git_repository3_et)
    EditText mUserGit3;

    @BindView(R.id.user_info_aboutMyself_et)
    EditText mUserBio;

    @BindView(R.id.user_info_rating_tv)
    TextView mUserValueRating;

    @BindView(R.id.user_info_projects_tv)
    TextView mUserValueProjects;

    @BindView(R.id.user_info_numOfCodeLines_tv)
    TextView mUserValueCodeLines;

    private List<TextView> mUserValueViews;


    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    private List<EditText> mUserInfoViews;


    private TextWatcherValidator mPhoneValidator = null;
    private TextWatcherValidator mUserMailValidator = null;
    private TextWatcherValidator mUserVkValidator = null;
    private TextWatcherValidator mUserGit1Validator = null;
    private TextWatcherValidator mUserGit2Validator = null;
    private TextWatcherValidator mUserGit3Validator = null;


    private ImageView mAvatar;
    private TextView mNavTxtNameView, mNavTxtEmailView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //инициализация синглтона
        mDataManager = DataManager.getInstance();

        //инициализация аватарки
        mAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.avatar);

        //инициализация ФИО в drawere
        mNavTxtNameView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_txt);

        //инициализация email в drawere
        mNavTxtEmailView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email_txt);

        //привязка листенара для FAB
        mFab.setOnClickListener(this);

        //привязка листенара для заставки на апп баре
        mProfilePlaceholder.setOnClickListener(this);

        //region ========== Привязка листенара для кнопок справа от полей

        mProfileTel.setOnClickListener(this);
        mProfileEmail.setOnClickListener(this);
        mProfileVk.setOnClickListener(this);
        mProfileGithub1.setOnClickListener(this);
        mProfileGithub2.setOnClickListener(this);
        mProfileGithub3.setOnClickListener(this);

        //endregion

        //инициализация ArrayList данных профиля Главного пользователя для сохранения в SharedPrefernces
        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit1);
        mUserInfoViews.add(mUserGit2);
        mUserInfoViews.add(mUserGit3);
        mUserInfoViews.add(mUserBio);


        //инициализация ArrayList для загрузки в плашку профиля Главного пользователя из SharePrefernces
        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodeLines);
        mUserValueViews.add(mUserValueProjects);

        //установка гамбургер меню
        setupToolbar();


        //region ============== загрузка из Shared Preferences содержимого
        initUserInfoValue(); //в плашку
        initUserFields(); //остальных данных профиля и drawer-а

        //добавляем валидаторы для initUserFields чтобы валидация сработала на загруженные поля
        //и тут же удаляем чтобы память не кушала
        addValidators();
        removeValidators();

        //фото профиля
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                //.resize(768, 512)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.user_bg)
                .into(mProfileImage);

        //загрузка аватара происхоидт в setupDrawer()


        //endregion


        if (savedInstanceState == null) {

            //активити ни разу не запускалось
            //saveUserFields();
            //showSnackbar("активити ни разу не запускалось");
            //showToast("активити ни разу не запускалось");

        } else {
            //активити запускалось когда-то
            //showSnackbar("активити запускалось когда-то");
            //showToast("активити запускалось когда-то");

            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }


        Log.d(TAG, "onCreate: ");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        //необходимо здесь чтобы при нажатии Back выделенная опция меню соответствовала текущей активити
        setupDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserFields();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
        initUserFields();
    }

    /**
     * Реализует onClickListener для всех кнопок
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                //showSnackbar("Click");
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call:
                dial();
                break;
            case R.id.send_email_iv:
                sendEmail();
                break;
            case R.id.open_VK_profile_iv:
                viewInBrowser(v);
                break;
            case R.id.open_repository1_iv:
                viewInBrowser(v);
                break;
            case R.id.open_repository2_iv:
                viewInBrowser(v);
                break;
            case R.id.open_repository3_iv:
                viewInBrowser(v);
                break;


        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

    }

    /**
     * Нужен чтобы выполнять действия с задержкой
     */
    public void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Выполнить с задержкой
                hideProgress();
            }
        }, 5000);

    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();

    }


    /**
     * Получение результата от другой Activity: фото из камеры или галерии
     *
     * @param requestCode код идентифицирующий источник фото
     * @param resultCode  код операции
     * @param data        возвращенный интент
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();

                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }


    /**
     * переключает режим редактирования
     *
     * @param mode если 1 режим редактирования, если 0 режим просмотра
     */
    private void changeEditMode(int mode) {

        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_check_black_24dp);

            //Добавляем валидаторы
            addValidators();


            for (EditText userValue : mUserInfoViews) {


                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
                mUserPhone.requestFocus();

                //region это нужно чтобы показывалась клавиатура
//                mUserPhone.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
//                mUserPhone.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                //endregion
            }

        } else {

            //Удаляем валидаторы с полей
            removeValidators();

            //если пользователь не менял фото, то не грузить на сервер
            if (mSelectedImage != null && !mSelectedImage.toString().isEmpty()) {
                uploadPhotoFile(mSelectedImage);
            }


            mFab.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            for (EditText userValue : mUserInfoViews) {

                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

                hideProfilePlaceholder();
                unLockToolbar();

                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

                saveUserFields();
            }
        }

    }

    /**
     * загружает содержимое полей из SharedPrefernces и устанавливает их полям
     */
    private void initUserFields() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
//        for (int i = 0; i < userData.size()-1; i++) { //нужно уменьшить на 1 потому что в сохраненном листе больше полей, чем в листе EditText-ов
//            mUserInfoViews.get(i).setText(userData.get(i));
//        }

        for (int i = 0; i < mUserInfoViews.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }


        mNavTxtEmailView.setText(userData.get(1)); //Установка email в drawere
        mNavTxtNameView.setText(userData.get(userData.size() - 1)); //Установка ФИО в drawere
    }

    /**
     * сохраняет содержимое полей в SharedPrefernces
     */

    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        userData.add(mNavTxtNameView.getText().toString());
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {
        List<String> userInfoValues = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userInfoValues.size(); i++) {
            mUserValueViews.get(i).setText(userInfoValues.get(i));
            Log.d(ConstantManager.TAG_PREFIX + "MainActivity", userInfoValues.get(i));
        }

    }

    /**
     * Добавляет гамбургер меню на тулбар
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Выбирает пункты меню и закрывает меню после выбора
     */
    private void setupDrawer() {

        //устанавливает выбранным пункт меню, где мы находимся
        mNavigationView.getMenu().findItem(R.id.user_profile_id).setChecked(true);


        Picasso.with(mNavigationDrawer.getContext())
                .load(mDataManager.getPreferencesManager().loadUserAvatar())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                //.resize(120, 120)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.user_bg)
                .transform(new CircleTransform())
                .into(mAvatar);


        mNavigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //showSnackbar(item.getTitle().toString());

                switch (item.getItemId()) {
                    case R.id.user_profile_id:

                        break;
                    case R.id.team_menu:
                        item.setChecked(true);
                        Intent openTeamList = new Intent(MainActivity.this, UserListActivity.class);
                        startActivity(openTeamList);
                        break;
                    case R.id.logout:
                        DevintensiveApplication.getSharedPreferences().edit().clear().apply();
                        //finishAffinity(); //закрывает приложение со всеми activity
                        Intent openAuth = new Intent(MainActivity.this, AuthActivity.class);
                        startActivity(openAuth);
                        //finish();
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
     * @param item габургер меню
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
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
     * Закрывает боковую менюшку по нажатию Back (вместо выхода из приложения)
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Инициирует интент для выбора фотографии из галереи
     */
    private void loadPhotoFromGallery() {

        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);

    }

    /**
     * Инициирует интент для вызова камеры. Запрашивает права при необходимости
     */

    private void loadPhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //если есть права
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();

                Log.e(ConstantManager.TAG_CAMERA, "Load from camera was not successful");
                showToast(getString(R.string.error_create_file));

            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);

            }
        } else { //запрашиваем разрешения
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, R.string.snackbar_give_permissions, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_give_permissions_button, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 07.07.2016 тут обрабатываем разрешение, если разрешение получено вывести сообщение или реализовать какую-то другую логику
                showToast(getString(R.string.need_camera_permissions));
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 07.07.2016 тут обрабатываем разрешение, если разрешение получено вывести сообщение или реализовать какую-то другую логику
                showToast(getString(R.string.need_write_to_external_storage_permissions));
            }
        }
    }

    /**
     * Прячет слой с интерфейсом для выбора фотографии на апп бар
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);

    }

    /**
     * Показывает слой с интерфейсом для выбора фотографии на апп бар
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Раскрывает тулбар полностью (нужно в режиме редактирования)
     */
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);

    }

    /**
     * Убирает полное раскрытие тулбара - делает его доступным для скролла
     */
    private void unLockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        mCollapsingToolbar.setLayoutParams(mAppBarParams);

    }

    /**
     * Создание диалогов
     *
     * @param ConstantManager.LOAD_PROFILE_PHOTO - для обновления фотографии профиля
     * @return
     */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {

                            case 0:
                                loadPhotoFromGallery();
                                //showSnackbar("загрузить из галереи");
                                break;
                            case 1:
                                //загрузить из камеры
                                loadPhotoFromCamera();
                                //showSnackbar("загрузить из камеры");
                                break;
                            case 3:
                                //отмена
                                dialog.cancel();
                                showSnackbar("отмена");
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }


    }

    /**
     * Создание файла, куда будет сохранятся фотография с камеры
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;

    }

    /**
     * Установка фотографии/фото из галерии на апп бар
     *
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {


        Picasso.with(this)
                .load(selectedImage)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                //.resize(768, 512)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.user_bg)
                .into(mProfileImage);


        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    /**
     * Открывает настройки текущего приложения (для выставления разрешений)
     */

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Звонит по номеру из профиля
     */
    private void dial() {
        String phoneNo = ((EditText) findViewById(R.id.phone_et)).getText().toString();

        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));
        startActivity(Intent.createChooser(dialIntent, getString(R.string.chooser_title_dial)));
    }

    /**
     * Пишет на имейл из профиля
     */

    private void sendEmail() {
        String email = ((EditText) findViewById(R.id.email_et)).getText().toString();

        Intent dialIntent = new Intent(Intent.ACTION_SENDTO);
        //, Uri.parse("mailto:" + email)); выдает очень много вариантов
        //dialIntent.setType("message/rfc822"); выдает меньше вариантов, но среди них тоже есть не почтовые клиенты
        dialIntent.setData(Uri.parse("mailto:"));
        dialIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(Intent.createChooser(dialIntent, getString(R.string.chooser_title_sendEmail)));

    }

    /**
     * Открывает ссылку из профиля в браузере
     *
     * @param v
     */
    private void viewInBrowser(View v) {

        String link = ((EditText) ((TextInputLayout) ((LinearLayout) ((LinearLayout) v.getParent()).getChildAt(1)).getChildAt(0)).getChildAt(0)).getText().toString();

        Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + link));
        startActivity(Intent.createChooser(openBrowser, getString(R.string.chooser_title_openBrowser)));
    }

    private void addValidators() {

        TextWatcherValidator mPhoneValidator = new TextWatcherValidator(mUserPhone, getString(R.string.validate_user_phone_error));
        TextWatcherValidator mUserMailValidator = new TextWatcherValidator(mUserMail, getString(R.string.validate_user_email_error));
        TextWatcherValidator mUserVkValidator = new TextWatcherValidator(mUserVk, getString(R.string.validate_user_vk_error));
        TextWatcherValidator mUserGit1Validator = new TextWatcherValidator(mUserGit1, getString(R.string.validate_user_github_error));
        TextWatcherValidator mUserGit2Validator = new TextWatcherValidator(mUserGit2, getString(R.string.validate_user_github_error));
        TextWatcherValidator mUserGit3Validator = new TextWatcherValidator(mUserGit3, getString(R.string.validate_user_github_error));


        mUserPhone.addTextChangedListener(mPhoneValidator);
        mUserMail.addTextChangedListener(mUserMailValidator);
        mUserVk.addTextChangedListener(mUserVkValidator);
        mUserGit1.addTextChangedListener(mUserGit1Validator);
        mUserGit2.addTextChangedListener(mUserGit2Validator);
        mUserGit3.addTextChangedListener(mUserGit3Validator);

        //нужно чтобы валидация сработала сразу после добавления Watcher-а
        mUserPhone.setText(mUserPhone.getText());
        mUserMail.setText(mUserMail.getText());
        mUserVk.setText(mUserVk.getText());
        mUserGit1.setText(mUserGit1.getText());
        mUserGit2.setText(mUserGit2.getText());
        mUserGit3.setText(mUserGit3.getText());
    }

    private void removeValidators() {
        mUserPhone.removeTextChangedListener(mPhoneValidator);
        mUserMail.removeTextChangedListener(mUserMailValidator);
        mUserVk.removeTextChangedListener(mUserVkValidator);
        mUserGit1.removeTextChangedListener(mUserGit1Validator);
        mUserGit2.removeTextChangedListener(mUserGit2Validator);
        mUserGit3.removeTextChangedListener(mUserGit3Validator);
    }

    /**
     * загрузка фото в профиль на сервер
     *
     * @param fileUri файла для загрузки в профиль на сервер
     */
    public void uploadPhotoFile(Uri fileUri) {
        File file;

        if (fileUri.getScheme() == "file") {
            //из камеры приходит путь вида (схема file) file:///storage/emulated/0/Pictures/JPEG-234234-3454332.jpg
            file = new File(fileUri.getPath());
        } else {
            //из галереи приходит путь вида (схема content) content://media/external/images/media/24324
            file = new File(getRealPathFromURI(this, fileUri));
        }

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);


        // finally, execute the request
        Call<ResponseBody> call = mDataManager.uploadPhoto(mDataManager.getPreferencesManager().getUserId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.d(TAG, "Upload success");

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Upload failed" + t.getMessage().toString());
            }
        });
    }

    /**
     * Метод нужен чтоб получить абсолютный путь к файлу, так как из галереи приходит путь типа content://media/external/images/media/24324
     *
     * @param context
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

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
//        Log.d(TAG, "onContextItemSelected: ");
//        return super.onContextItemSelected(item);
//    }


}
