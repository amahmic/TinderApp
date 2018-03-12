package com.example.azramahmic.tinderapp.di.component.app_component;

import com.example.azramahmic.tinderapp.di.module.LoginModule;
import com.example.azramahmic.tinderapp.di.scope.ActivityScope;
import com.example.azramahmic.tinderapp.presentation.login.LoginActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {LoginModule.class})
@ActivityScope
public interface LoginComponent {

    void inject(LoginActivity loginActivity);

    @Subcomponent.Builder
    interface Builder {
        LoginComponent build();
    }
}
