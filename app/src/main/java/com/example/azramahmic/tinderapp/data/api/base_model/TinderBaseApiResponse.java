package com.example.azramahmic.tinderapp.data.api.base_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TinderBaseApiResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private ErrorData errorData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorData error) {
        this.errorData = error;
    }
}
