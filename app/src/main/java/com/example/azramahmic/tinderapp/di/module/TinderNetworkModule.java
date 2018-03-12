package com.example.azramahmic.tinderapp.di.module;

import com.example.azramahmic.tinderapp.data.api.tinder.TinderInterceptor;
import com.example.azramahmic.tinderapp.data.api.tinder.TinderService;
import com.example.azramahmic.tinderapp.di.qualifier.BaseOkHttpClient;
import com.example.azramahmic.tinderapp.di.qualifier.TinderBaseUrl;
import com.example.azramahmic.tinderapp.di.qualifier.TinderOkHttpClient;
import com.example.azramahmic.tinderapp.di.qualifier.TinderRetrofit;
import com.example.azramahmic.tinderapp.di.scope.UserScope;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class TinderNetworkModule {

    @Provides
    @UserScope
    @TinderRetrofit
    public static Retrofit provideTinderRetrofit(@TinderOkHttpClient OkHttpClient okHttpClient,
                                                 @TinderBaseUrl String baseUrl,
                                                 Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @UserScope
    public static TinderService provideTinderService(@TinderRetrofit Retrofit retrofit) {
        return retrofit.create(TinderService.class);
    }

    @Provides
    @UserScope
    @TinderOkHttpClient
    public static OkHttpClient provideTinderOkHttpClient(@BaseOkHttpClient OkHttpClient okHttpClient,
                                                         TinderInterceptor tinderInterceptor,
                                                         HttpLoggingInterceptor loggingInterceptor) {
        return okHttpClient.newBuilder()
                .addInterceptor(tinderInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
