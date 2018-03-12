package com.example.azramahmic.tinderapp.data.local;

import com.example.azramahmic.tinderapp.data.api.tinder_auth.UserCredentials;
import com.example.azramahmic.tinderapp.data.local.model.auth_user.TinderAppUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;

@Singleton
public class TinderAppUserLocalStore {

    @Inject
    public TinderAppUserLocalStore() {
    }

    public void insertTinderAppUser(TinderAppUser tinderAppUser) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<TinderAppUser> user = realm.where(TinderAppUser.class).findAll();
                    if (user != null) {
                        user.deleteAllFromRealm();
                    }
                    realm.insertOrUpdate(tinderAppUser);
                }
            });
        }
    }

    public TinderAppUser getTinderAppUser() {
        try (Realm realm = Realm.getDefaultInstance()) {
            TinderAppUser tinderAppUserFromRealm = realm.where(TinderAppUser.class).findFirst();
            if (tinderAppUserFromRealm != null) {
                return realm.copyFromRealm(tinderAppUserFromRealm);
            } else {
                return null;
            }
        }
    }

    public UserCredentials getUserCredentials() {
        try (Realm realm = Realm.getDefaultInstance()) {
            UserCredentials userCredentials = realm.where(UserCredentials.class).findFirst();
            if (userCredentials != null) {
                return realm.copyFromRealm(userCredentials);
            } else {
                return null;
            }
        }
    }
}
