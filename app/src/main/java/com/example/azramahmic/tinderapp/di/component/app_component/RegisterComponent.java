package com.example.azramahmic.tinderapp.di.component.app_component;

import com.example.azramahmic.tinderapp.di.module.RegisterModule;
import com.example.azramahmic.tinderapp.di.scope.ActivityScope;
import com.example.azramahmic.tinderapp.presentation.register.RegisterActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {RegisterModule.class})
@ActivityScope
public interface RegisterComponent {

    void inject(RegisterActivity registerActivity);

    @Subcomponent.Builder
    interface Builder {
        RegisterComponent build();
    }
}
