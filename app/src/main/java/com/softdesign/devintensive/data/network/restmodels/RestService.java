package com.softdesign.devintensive.data.network.restmodels;

import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by anray on 11.07.2016.
 */
public interface RestService {

    @POST("login")
    Call<UserModelResponse> loginUser (@Body UserLoginRequest req);

}
