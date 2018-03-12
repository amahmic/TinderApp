package com.example.azramahmic.tinderapp.data.local.model.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("profileImage")
    @Expose
    private String profileImageUrl;
    @SerializedName("thumbnailImage")
    @Expose
    private String profileThumbnailUrl;

    //declare one to many relationship
    private RealmList<UserPhoto> userPhotos;

    public User() {
    }

    public User(int id, String firstName, String profileImageUrl, String profileThumbnailUrl) {
        this.id = id;
        this.firstName = firstName;
        this.profileImageUrl = profileImageUrl;
        this.profileThumbnailUrl = profileThumbnailUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String photoUrl) {
        this.profileImageUrl = photoUrl;
    }

    public String getProfileThumbnailUrl() {
        return profileThumbnailUrl;
    }

    public void setProfileThumbnailUrl(String profileThumbnailUrl) {
        this.profileThumbnailUrl = profileThumbnailUrl;
    }

    public RealmList<UserPhoto> getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(RealmList<UserPhoto> userPhotos) {
        this.userPhotos = userPhotos;
    }
}