package com.example.azramahmic.tinderapp.data.api.tinder_auth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuthDetails {
    @SerializedName("email")
    @Expose
    public final String email;
    @SerializedName("password")
    @Expose
    public final String password;

    public UserAuthDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAuthDetails{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
