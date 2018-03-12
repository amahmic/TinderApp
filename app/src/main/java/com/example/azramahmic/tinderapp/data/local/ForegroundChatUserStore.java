package com.example.azramahmic.tinderapp.data.local;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.azramahmic.tinderapp.data.local.model.ForegroundChatUser;
import com.example.azramahmic.tinderapp.di.scope.UserScope;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

@UserScope
public class ForegroundChatUserStore {

    @Inject
    public ForegroundChatUserStore() {
    }

    public void insertCurrentChat(ForegroundChatUser foregroundChatUser) {
        Log.d("TAG", "Inserting current chat user: " + foregroundChatUser);
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    RealmResults<ForegroundChatUser> user = realm.where(ForegroundChatUser.class).findAll();
                    if (user != null) {
                        user.deleteAllFromRealm();
                        realm.insertOrUpdate(foregroundChatUser);
                    }
                }
            });
        }
    }

    public void clear() {
        Log.d("TAG", "Clearing ForegroundChatUser.class");
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    RealmResults<ForegroundChatUser> user = realm.where(ForegroundChatUser.class).findAll();
                    if (user != null) {
                        user.deleteAllFromRealm();
                    }
                }
            });
        }
    }
}
