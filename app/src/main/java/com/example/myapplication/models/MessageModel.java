package com.example.myapplication.models;

public class MessageModel {
    private String phoneNumber;
    private String message;

    public MessageModel(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }
}
