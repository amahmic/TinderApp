package com.example.azramahmic.tinderapp.data.local.model.auth_user;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TinderAppUser extends RealmObject {
    @PrimaryKey
    private String email;
    private String uid;

    public TinderAppUser() {
    }

    public TinderAppUser(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "TinderAppUser{" +
                "email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}