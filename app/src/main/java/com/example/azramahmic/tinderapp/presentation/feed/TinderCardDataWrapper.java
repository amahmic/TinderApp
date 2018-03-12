package com.example.azramahmic.tinderapp.presentation.feed;

import com.example.azramahmic.tinderapp.data.local.model.feed.User;

public class TinderCardDataWrapper {

    public static final int PROFILE_IMAGE_INDEX = -1;

    private User user;
    private int index;

    public TinderCardDataWrapper(User user) {
        this.user = user;
        this.index = PROFILE_IMAGE_INDEX;
    }

    public User getUser() {
        return user;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
