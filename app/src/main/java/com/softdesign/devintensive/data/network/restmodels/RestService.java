package com.softdesign.devintensive.data.network.restmodels;

import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserListRes;
import com.softdesign.devintensive.data.network.response.UserModelResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponseByToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by anray on 11.07.2016.
 */
public interface RestService {


    @POST("login")
    Call<UserModelResponse> loginUser (@Body UserLoginRequest req);

    @GET("user/{userId}")
    Call<UserModelResponseByToken> loginUserByToken (@Path("userId") String userId);

    @GET("user/list?orderBy=rating")
    Call<UserListRes> getUserList();

}
