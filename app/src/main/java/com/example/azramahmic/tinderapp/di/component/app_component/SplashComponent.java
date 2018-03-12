package com.example.azramahmic.tinderapp.di.component.app_component;

import com.example.azramahmic.tinderapp.di.module.SplashModule;
import com.example.azramahmic.tinderapp.di.scope.ActivityScope;
import com.example.azramahmic.tinderapp.presentation.splash.SplashActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {SplashModule.class})
@ActivityScope
public interface SplashComponent {

    void inject(SplashActivity splashActivity);

    @Subcomponent.Builder
    interface Builder {
        SplashComponent build();
    }
}
