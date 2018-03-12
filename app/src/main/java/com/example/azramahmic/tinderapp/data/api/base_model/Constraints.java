package com.example.azramahmic.tinderapp.data.api.base_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Constraints {

    @SerializedName("minLength")
    @Expose
    private String minLength;

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }
}
