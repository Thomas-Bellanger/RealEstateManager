package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

public class Options {

    @SerializedName("thumbMaps")
    private boolean thumbMaps;

    @SerializedName("maxResults")
    private int maxResults;

    @SerializedName("ignoreLatLngInput")
    private boolean ignoreLatLngInput;

    public boolean isThumbMaps() {
        return thumbMaps;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public boolean isIgnoreLatLngInput() {
        return ignoreLatLngInput;
    }
}