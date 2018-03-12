package com.example.azramahmic.tinderapp.data.local.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ForegroundChatUser extends RealmObject {
    @PrimaryKey
    @SerializedName("receiverId")
    @Expose
    private String receiverId;

    public ForegroundChatUser() {
    }

    public ForegroundChatUser(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
