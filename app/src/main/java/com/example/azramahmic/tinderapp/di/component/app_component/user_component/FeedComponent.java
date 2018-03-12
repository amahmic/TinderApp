package com.example.azramahmic.tinderapp.di.component.app_component.user_component;

import com.example.azramahmic.tinderapp.di.module.FeedModule;
import com.example.azramahmic.tinderapp.presentation.feed.FeedActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {FeedModule.class})
public interface FeedComponent {

    void inject(FeedActivity feedActivity);

    @Subcomponent.Builder
    interface Builder {
        FeedComponent build();
    }
}
