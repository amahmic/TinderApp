package com.example.azramahmic.tinderapp.di.module;

import android.app.Application;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.TinderAuthInterceptor;
import com.example.azramahmic.tinderapp.data.api.tinder_auth.TinderAuthService;
import com.example.azramahmic.tinderapp.di.qualifier.BaseOkHttpClient;
import com.example.azramahmic.tinderapp.di.qualifier.TinderAuthBaseUrl;
import com.example.azramahmic.tinderapp.di.qualifier.TinderAuthOkHttpClient;
import com.example.azramahmic.tinderapp.di.qualifier.TinderAuthRetrofit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class NetworkModule {

    @Provides
    @Singleton
    public static Gson provideBaseGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton
    public static HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    public static Cache provideCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    @BaseOkHttpClient
    public static OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    @TinderAuthOkHttpClient
    public static OkHttpClient provideTinderAuthOkHttpClient(@BaseOkHttpClient OkHttpClient okHttpClient,
                                               TinderAuthInterceptor tinderAuthInterceptor,
                                               HttpLoggingInterceptor loggingInterceptor) {
        return okHttpClient.newBuilder()
                .addInterceptor(tinderAuthInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @TinderAuthRetrofit
    public static Retrofit provideTinderAuthRetrofit(@TinderAuthOkHttpClient OkHttpClient okHttpClient,
                                                     @TinderAuthBaseUrl String baseUrl,
                                                     Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public static TinderAuthService provideTinderAuthService(@TinderAuthRetrofit Retrofit retrofit) {
        return retrofit.create(TinderAuthService.class);
    }
}
