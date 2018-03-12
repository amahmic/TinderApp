package com.example.azramahmic.tinderapp.di.module;

import com.example.azramahmic.tinderapp.presentation.feed.FeedContract;
import com.example.azramahmic.tinderapp.presentation.feed.FeedPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class FeedModule {
    @Binds
    public abstract FeedContract.Presenter provideFeedPresenter(FeedPresenter feedPresenter);
}
