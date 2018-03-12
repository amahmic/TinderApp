package com.example.azramahmic.tinderapp.presentation.feed;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.azramahmic.tinderapp.data.api.base_model.ErrorData;
import com.example.azramahmic.tinderapp.data.api.tinder.TinderService;
import com.example.azramahmic.tinderapp.data.api.tinder.model.FeedResponse;
import com.example.azramahmic.tinderapp.data.local.FeedLocalStore;
import com.example.azramahmic.tinderapp.data.local.model.feed.FeedData;
import com.example.azramahmic.tinderapp.data.local.model.feed.User;
import com.example.azramahmic.tinderapp.data.local.model.feed.UserPhoto;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class FeedPresenter implements FeedContract.Presenter, RealmChangeListener<RealmResults<User>> {

    private TinderService tinderService;
    private FeedLocalStore feedLocalStore;
    private CompositeDisposable compositeDisposable;
    private FeedContract.View view;
    Realm realm;
    RealmResults<User> feedData;
    private int nextPage;
    private boolean loading;
    private boolean lastPage;
    private int fetchedCount;

    @Inject
    FeedPresenter(CompositeDisposable compositeDisposable, TinderService tinderService, FeedLocalStore feedLocalStore) {
        this.tinderService = tinderService;
        this.feedLocalStore = feedLocalStore;
        this.compositeDisposable = compositeDisposable;
        this.nextPage = 1;
        this.loading = false;
        this.fetchedCount = 0;
        this.lastPage = false;
    }

    @Override
    public void attach(FeedContract.View view) {
        this.view = view;
        this.realm = Realm.getDefaultInstance();
        feedData = realm.where(User.class).findAll();
        view.showData(feedData);
        feedData.addChangeListener(this);
        fetchFeed(nextPage);
    }

    @Override
    public void onChange(@NonNull RealmResults<User> users) {
        Log.d("FeedPresenter", "onChange");
        Log.d("FeedPresenter", "onChange count: " + users.size());
        view.refreshData(users);
        Log.d("FeedPresenter", "Current count: " + users.size());
        //view.showData(users);
    }

    @Override
    public void viewRemoved(int count) {
        Log.d("INFINITE", "View removed, current count: " + count);
        if (count < 3) {
            if (!loading && !lastPage) {
                Log.d("INFINITE", "Fetch page: " + nextPage);
                fetchFeed(nextPage);
            } else if (count == 0) {
                Log.d("INFINITE", "Show no more data");
                view.showNoMoreData();
            }
        }
    }

    private void fetchFeed(int page) {
        loading = true;
        tinderService.fetchFeed(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FeedResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(com.example.azramahmic.tinderapp.data.api.tinder.model.FeedResponse feedResponse) {
                        loading = false;
                        ErrorData feedError = feedResponse.getErrorData();
                        if (feedError == null && !feedResponse.getFeedData().isEmpty()) {
                            Log.i("FeedPresenter", "error == null");
                            Log.d("FeedPresenter: ", "Page: " + feedResponse.getPage());
                            feedLocalStore.saveUsers(feedResponse.getFeedData());
                            nextPage++;
                            fetchedCount += feedResponse.getFeedData().size();
                            lastPage = fetchedCount >= feedResponse.getTotalCount();
                            Log.d("INFINITE", "Next page: " + nextPage);
                            Log.d("INFINITE", "Fetched count: " + fetchedCount);
                            Log.d("INFINITE", "Last page: " + lastPage);
                        } else {
                            Log.i("FeedPresenter", "error != null");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                        Log.d("FeedPresenter", "on throwable from network");
                    }
                });
    }

    private void saveUsersToLocalStore(List<FeedData> feedDataList) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                for (FeedData feedData : feedDataList) {
                    User user = realm.copyToRealmOrUpdate(feedData.getUser());
                    RealmList<UserPhoto> _userPhotos = new RealmList<>();
                    List<UserPhoto> userPhotosManaged = realm.copyToRealmOrUpdate(feedData.getUserPhotos());
                    _userPhotos.addAll(userPhotosManaged);
                    user.setUserPhotos(_userPhotos);
                }
            }
        });
    }

    @Override
    public void detach() {
        this.view = null;
        compositeDisposable.dispose();
        if (feedData != null) {
            feedData.removeAllChangeListeners();
        }
        if (realm != null) {
            realm.close();
        }
    }
}
