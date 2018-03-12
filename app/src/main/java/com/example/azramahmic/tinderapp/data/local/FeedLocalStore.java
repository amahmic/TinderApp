package com.example.azramahmic.tinderapp.data.local;

import com.example.azramahmic.tinderapp.data.local.model.feed.FeedData;
import com.example.azramahmic.tinderapp.data.local.model.feed.User;
import com.example.azramahmic.tinderapp.data.local.model.feed.UserPhoto;
import com.example.azramahmic.tinderapp.di.scope.UserScope;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;

@UserScope
public class FeedLocalStore {

    @Inject
    public FeedLocalStore() {

    }

    public void saveUsers(List<FeedData> feedDataList) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
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
    }

    //First delete all from database and then insert
    public void updateUsers(List<FeedData> feedData) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                    insertUsers(realm, feedData);
                }
            });
        }
    }

    private void insertUsers(Realm realm, List<FeedData> feedDataList) {
        for (FeedData feedData : feedDataList) {
            User user = realm.copyToRealmOrUpdate(feedData.getUser());
            RealmList<UserPhoto> _userPhotos = new RealmList<>();
            List<UserPhoto> userPhotosManaged = realm.copyToRealmOrUpdate(feedData.getUserPhotos());
            _userPhotos.addAll(userPhotosManaged);
            user.setUserPhotos(_userPhotos);
        }
    }

}
