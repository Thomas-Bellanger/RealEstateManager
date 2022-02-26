package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

public class Copyright {

    @SerializedName("imageAltText")
    private String imageAltText;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("text")
    private String text;

    public String getImageAltText() {
        return imageAltText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }
}