package com.example.azramahmic.tinderapp.data.api.tinder_auth;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.AuthResponse;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.model.UserAuthDetails;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TinderAuthService {
    @POST("register")
    Single<AuthResponse> register(@Body UserAuthDetails user);

    @POST("login")
    Single<AuthResponse> login(@Body UserAuthDetails user);

    @POST("login")
    Call<AuthResponse> loginPom(@Body UserCredentials user);
}
