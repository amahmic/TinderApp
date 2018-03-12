package com.example.azramahmic.tinderapp.di.module;

import com.example.azramahmic.tinderapp.presentation.register.RegisterContract;
import com.example.azramahmic.tinderapp.presentation.register.RegisterPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RegisterModule {
    @Binds
    public abstract RegisterContract.Presenter provideRegisterPresenter(RegisterPresenter registerPresenter);
}
