package com.softdesign.devintensive.data.managers;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.softdesign.devintensive.R;

import java.util.regex.Pattern;

/**
 * Created by anray on 09.07.2016.
 */
public class TextWatcherValidator implements TextWatcher {


    private EditText mEditText;
    private String mErrorMessage;
    private final Pattern mPhoneRegex = Pattern.compile("^(\\d{11,20}|\\+[\\d]\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2,6})$");
    private final Pattern mEmailRegex = Pattern.compile("^[\\w]{3,}@[\\w]{2,}\\.[\\w]{2,3}$");
    private final Pattern mVkRegex = Pattern.compile("^vk\\.com[\\\\\\/]\\w{3,}$");
    private final Pattern mGithubRegex = Pattern.compile("^(github\\.com[\\\\\\/]\\w{3,}[\\\\\\/]\\w{3,}|No_[\\d]_git_repo)$");

    private int mCursorPosition;


    private boolean isValid(CharSequence s, Pattern regEx) {
        return regEx.matcher(s).matches();
    }

    /**
     * @param et    получаем объект едит текста, в которм будем сетить текст
     * @param error получаем текст ошибки для поля
     */

    public TextWatcherValidator(EditText et, String error) {
        mEditText = et;
        mErrorMessage = error;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mCursorPosition = start + after;
        Log.d("XXX-before-start", String.valueOf(start));
        Log.d("XXX-before-count", String.valueOf(count));
        Log.d("XXX-before-after", String.valueOf(after));

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Log.d("XXX-on-start", String.valueOf(start));
        Log.d("XXX-on-before", String.valueOf(before));
        Log.d("XXX-on-count", String.valueOf(count));

    }

    @Override
    public void afterTextChanged(Editable s) {

        //удаляем пробелы
        String result = s.toString().replaceAll(" ", "");


        switch (mEditText.getId()) {
            case R.id.phone_et:
                //выводим ссобщение об ошибке если не совпадает с Regex
                if (!isValid(result, mPhoneRegex)) {
                    mEditText.setError(mErrorMessage);


                }
                break;
            case R.id.email_et:
                //выводим ссобщение об ошибке если не совпадает с Regex
                if (!isValid(result, mEmailRegex)) {
                    mEditText.setError(mErrorMessage);
                }
                break;

            case R.id.vk_profile_et:
                //удаляем все до vk.com

                try { //try нужен для того чтобы можно было удалить vk.com

                    result = result.substring(result.indexOf("vk.com"), result.length());
                    

                } catch (Exception e) {

                }

                mEditText.setSelection(result.length()); //ставит курсор в конец строки
                //применяем все фильтры к строке
                //mEditText.setText(result);

                //выводим ссобщение об ошибке если не совпадает с Regex
                if (!isValid(result, mVkRegex)) {
                    mEditText.setError(mErrorMessage);
                }
                break;

            case R.id.git_repository1_et:
            case R.id.git_repository2_et:
            case R.id.git_repository3_et:
                //удаляем все до github.com
                try { //try нужен для того чтобы можно было удалить github.com
                    result = result.substring(result.indexOf("github.com"), result.length());

                } catch (Exception e) {

                }

                mEditText.setSelection(result.length()); //ставит курсор в конец строки
                //применяем все фильтры к строке
                //mEditText.setText(result);

                //выводим ссобщение об ошибке если не совпадает с Regex
                if (!isValid(result, mGithubRegex)) {
                    mEditText.setError(mErrorMessage);
                }
                break;
        }

        if (!s.toString().equals(result)) {
            mEditText.setText(result);
        }

        mEditText.setSelection(mCursorPosition); //ставит курс в текущую позицию

    }
}
