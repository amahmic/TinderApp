package com.example.azramahmic.tinderapp.presentation.chat_users;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatUser {

    private String email;
    private String uid;

    public ChatUser() {

    }

    public ChatUser(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
