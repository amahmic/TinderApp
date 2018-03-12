package com.example.azramahmic.tinderapp.data.api.base_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorData {

    @SerializedName("httpCode")
    @Expose
    private int httpCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errors")
    @Expose
    private List<Error> errors;

    public int getHttpCode() {
        return httpCode;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ErrorData{" +
                "httpCode=" + httpCode +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}
