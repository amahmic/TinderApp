package com.example.azramahmic.tinderapp.di.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public abstract class RxModule {

    @Provides
    public static CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
