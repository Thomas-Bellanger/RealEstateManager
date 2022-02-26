package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

public class ProvidedLocation {

    @SerializedName("location")
    private String location;

    public String getLocation() {
        return location;
    }
}