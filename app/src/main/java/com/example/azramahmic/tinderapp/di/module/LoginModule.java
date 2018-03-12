package com.example.azramahmic.tinderapp.di.module;

import com.example.azramahmic.tinderapp.presentation.login.LoginContract;
import com.example.azramahmic.tinderapp.presentation.login.LoginPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginModule {
    @Binds
    public abstract LoginContract.Presenter provideLoginPresenter(LoginPresenter loginPresenter);
}
