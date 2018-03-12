package com.example.azramahmic.tinderapp.data.local.model.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;

public class FeedData {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("issuingUser")
    @Expose
    private User user;
    @SerializedName("attachments")
    @Expose
    public List<UserPhoto> userPhotos;

    public FeedData() {
    }

    public FeedData(User user, List<UserPhoto> userPhotos) {
        this.user = user;
        this.userPhotos = userPhotos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserPhoto> getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(RealmList<UserPhoto> userPhotos) {
        this.userPhotos = userPhotos;
    }

    @Override
    public String toString() {
        return "FeedData{" +
                "id=" + id +
                ", user=" + user +
                ", userPhotos=" + userPhotos +
                '}';
    }
}