package com.example.azramahmic.tinderapp.di.component;

import android.app.Application;

import com.example.azramahmic.tinderapp.di.component.app_component.LoginComponent;
import com.example.azramahmic.tinderapp.di.component.app_component.RegisterComponent;
import com.example.azramahmic.tinderapp.di.component.app_component.SplashComponent;
import com.example.azramahmic.tinderapp.di.component.app_component.UserComponent;
import com.example.azramahmic.tinderapp.di.module.NetworkModule;
import com.example.azramahmic.tinderapp.di.module.RxModule;
import com.example.azramahmic.tinderapp.di.qualifier.TinderAuthBaseUrl;
import com.example.azramahmic.tinderapp.di.qualifier.TinderBaseUrl;
import com.example.azramahmic.tinderapp.presentation.TinderApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {NetworkModule.class, RxModule.class})
@Singleton
public interface AppComponent {

    SplashComponent.Builder plusSplashComponent();
    LoginComponent.Builder plusLoginComponent();
    RegisterComponent.Builder plusRegisterComponent();
    UserComponent.Builder plusUserComponent();

    @Component.Builder
    interface Builder {
        AppComponent build();
        @BindsInstance
        Builder aplication(Application application);
        @BindsInstance
        Builder baseAuthURL(@TinderAuthBaseUrl String baseUrl);
        @BindsInstance
        Builder baseURL(@TinderBaseUrl String baseUrl);
        @BindsInstance
        Builder tinderApp(TinderApp tinderApp);
    }
}