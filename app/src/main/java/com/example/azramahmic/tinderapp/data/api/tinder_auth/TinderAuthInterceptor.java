package com.example.azramahmic.tinderapp.data.api.tinder_auth;

import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;
public class TinderAuthInterceptor implements Interceptor {

    @Inject
    public TinderAuthInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
