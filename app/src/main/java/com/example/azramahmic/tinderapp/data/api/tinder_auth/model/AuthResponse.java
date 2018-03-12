package com.example.azramahmic.tinderapp.data.api.tinder_auth.model;

import com.example.azramahmic.tinderapp.data.api.base_model.TinderBaseApiResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse extends TinderBaseApiResponse {
    @SerializedName("data")
    @Expose
    private AuthData authData;

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }
}
