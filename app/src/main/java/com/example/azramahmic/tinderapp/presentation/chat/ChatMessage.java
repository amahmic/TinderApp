package com.example.azramahmic.tinderapp.presentation.chat;

import com.google.firebase.database.IgnoreExtraProperties;

//Properties that don't map to class fields are ignored when serializing to a class annotated with this annotation.
@IgnoreExtraProperties
public class ChatMessage {

    private String textMessage;
    private String senderName;
    private String senderId;
    private String receiverId;

    public ChatMessage() {
        // Default constructor required for calls to DataSnapshot.getValue(ChatMessage.class)
    }

    public ChatMessage(String textMessage, String senderName, String senderId, String receiverId) {
        this.textMessage = textMessage;
        this.senderName = senderName;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "textMessage='" + textMessage + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                '}';
    }
}
