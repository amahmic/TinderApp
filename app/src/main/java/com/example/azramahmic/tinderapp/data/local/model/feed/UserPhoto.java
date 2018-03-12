package com.example.azramahmic.tinderapp.data.local.model.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserPhoto extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original")
    @Expose
    private String photoUrl;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnailUrl;

    public UserPhoto() {
    }

    public UserPhoto(int id, String photoUrl, String thumbnailUrl) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        return "UserPhoto{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
