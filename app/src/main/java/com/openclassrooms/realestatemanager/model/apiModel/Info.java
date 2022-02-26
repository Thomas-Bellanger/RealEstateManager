package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Info {

    @SerializedName("statuscode")
    private int statuscode;

    @SerializedName("copyright")
    private Copyright copyright;

    @SerializedName("messages")
    private List<Object> messages;

    public int getStatuscode() {
        return statuscode;
    }

    public Copyright getCopyright() {
        return copyright;
    }

    public List<Object> getMessages() {
        return messages;
    }
}