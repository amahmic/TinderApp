package com.example.azramahmic.tinderapp.data.api.tinder_auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UserCredentials extends RealmObject {
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("password")
    @Expose
    String password;

    String tinder_token;

    public UserCredentials() {
    }

    public UserCredentials(String email, String password, String tinder_token) {
        this.email = email;
        this.password = password;
        this.tinder_token = tinder_token;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", tinder_token='" + tinder_token + '\'' +
                '}';
    }
}
