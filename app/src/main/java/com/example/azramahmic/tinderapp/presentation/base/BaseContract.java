package com.example.azramahmic.tinderapp.presentation.base;

public interface BaseContract {

    interface BaseView {
    }

    interface BasePresenter<V extends BaseView> {
        void attach(V view);
        void detach();
    }
}
