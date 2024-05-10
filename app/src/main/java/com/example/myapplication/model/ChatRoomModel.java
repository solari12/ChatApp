package com.example.myapplication.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {
    String chatRoomId;
    List<String> userId;
    Timestamp lasMessageTimestamp;
    String lastMessageSenderId;

    public ChatRoomModel() {
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public ChatRoomModel(String chatRoomId, List<String> userId, Timestamp lasMessageTimestamp, String lastMessageSenderId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.lasMessageTimestamp = lasMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public Timestamp getLasMessageTimestamp() {
        return lasMessageTimestamp;
    }

    public void setLasMessageTimestamp(Timestamp lasMessageTimestamp) {
        this.lasMessageTimestamp = lasMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }
}
