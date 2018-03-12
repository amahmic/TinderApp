package com.example.azramahmic.tinderapp.data.api.tinder_auth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthData {
    @SerializedName("token")
    @Expose
    private String authToken;
    @SerializedName("firebaseToken")
    @Expose
    private String firebaseToken;

    public String getToken() {
        return authToken;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", firebaseToken='" + firebaseToken + '\'' +
                '}';
    }
}
