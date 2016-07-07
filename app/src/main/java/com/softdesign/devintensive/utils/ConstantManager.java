package com.softdesign.devintensive.utils;

/**
 * Created by anray on 22.06.2016.
 */
public interface ConstantManager {
    boolean DEBUG = true;
    String TAG_PREFIX = "DEV ";
    String COLOR_MODE_KEY = "COLOR_MODE_KEY";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT1_KEY = "USER_4_KEY";
    String USER_GIT2_KEY = "USER_5_KEY";
    String USER_GIT3_KEY = "USER_6_KEY";
    String USER_BIO_KEY = "USER_7_KEY";
    String USER_PHOTO_KEY = "USER_8_KEY";


    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 88;

    int PERMISSION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
}
