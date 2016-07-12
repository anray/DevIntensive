package com.softdesign.devintensive.data.network.interceptors;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anray on 12.07.2016.
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferencesManager pm = DataManager.getInstance().getPreferencesManager();

        Request original = chain.request(); //оригинальный запрос, который шлем мы сейчас
        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token", pm.getAuthToken())
                .header("Request-User-Id", pm.getUserId())
                .header("User-Agent", "DevIntensiveApp");

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}
