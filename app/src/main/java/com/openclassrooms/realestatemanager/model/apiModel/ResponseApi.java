package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseApi {

    @SerializedName("options")
    private Options options;

    @SerializedName("results")
    private List<ResultsItem> results;

    @SerializedName("info")
    private Info info;

    public Options getOptions() {
        return options;
    }

    public List<ResultsItem> getResults() {
        return results;
    }

    public Info getInfo() {
        return info;
    }
}