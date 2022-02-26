package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsItem {

    @SerializedName("locations")
    private List<LocationsItem> locations;

    @SerializedName("providedLocation")
    private ProvidedLocation providedLocation;

    public List<LocationsItem> getLocations() {
        return locations;
    }

    public ProvidedLocation getProvidedLocation() {
        return providedLocation;
    }
}