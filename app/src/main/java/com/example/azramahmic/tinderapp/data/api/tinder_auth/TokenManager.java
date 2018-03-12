package com.example.azramahmic.tinderapp.data.api.tinder_auth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.azramahmic.tinderapp.di.scope.UserScope;
import com.example.azramahmic.tinderapp.presentation.TinderApp;

import javax.inject.Inject;

import io.realm.Realm;

@UserScope
public class TokenManager {

    private TinderApp tinderApp;

    @Inject
    public TokenManager(UserCredentials userCredentials, TinderApp tinderApp) {
        this.tinderApp = tinderApp;
        Log.d("TOKEN_MANAGER", "CREATE");
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    UserCredentials user = realm.where(UserCredentials.class).findFirst();
                    if (user != null) {
                        Log.d("TOKEN_MANAGER", "User is not null, update");
                        user.tinder_token = userCredentials.tinder_token;
                        Log.d("TOKEN_MANAGER", "User: " + user.toString());
                    } else {
                        Log.d("TOKEN_MANAGER", "User: is null");
                        Log.d("TOKEN_MANAGER", "Insert user: " + userCredentials.toString());
                        realm.insertOrUpdate(userCredentials);
                    }
                }
            });
        }
    }

    public void updateToken(String token) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    UserCredentials user = realm.where(UserCredentials.class).findFirst();
                    if (user != null) {
                        user.tinder_token = token;
                    }
                }
            });
        }
    }

    public String getToken() {
        try (Realm realm = Realm.getDefaultInstance()) {
            UserCredentials userCredentials = realm.where(UserCredentials.class).findFirst();
            if (userCredentials != null) {
                return realm.copyFromRealm(userCredentials).tinder_token;
            } else {
                return "";
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

    public void logout() {
        tinderApp.logout();
    }
}
