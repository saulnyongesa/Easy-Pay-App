package com.example.qrscannerapp;
import com.google.gson.annotations.SerializedName;

public class ResponsePay {
    @SerializedName("till_number")
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
