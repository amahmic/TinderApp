package com.example.azramahmic.tinderapp.presentation.feed;

import com.example.azramahmic.tinderapp.data.local.model.feed.User;
import com.example.azramahmic.tinderapp.presentation.base.BaseContract;

import io.realm.RealmResults;

public interface FeedContract extends BaseContract {

    interface View extends BaseContract.BaseView {
        void showProgress(boolean inProgress);
        void showData(RealmResults<User> users);
        void refreshData(RealmResults<User> users);
        void showError(String message);
        void showNoMoreData();
    }

    interface Presenter extends BaseContract.BasePresenter<FeedContract.View> {
        void viewRemoved(int count);
    }

}
