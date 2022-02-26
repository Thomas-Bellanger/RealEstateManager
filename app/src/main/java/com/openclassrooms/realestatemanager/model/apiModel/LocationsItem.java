package com.openclassrooms.realestatemanager.model.apiModel;

import com.google.gson.annotations.SerializedName;

public class LocationsItem {

    @SerializedName("dragPoint")
    private boolean dragPoint;

    @SerializedName("displayLatLng")
    private DisplayLatLng displayLatLng;

    @SerializedName("adminArea4")
    private String adminArea4;

    @SerializedName("unknownInput")
    private String unknownInput;

    @SerializedName("adminArea5")
    private String adminArea5;

    @SerializedName("adminArea6")
    private String adminArea6;

    @SerializedName("postalCode")
    private String postalCode;

    @SerializedName("adminArea1")
    private String adminArea1;

    @SerializedName("adminArea3")
    private String adminArea3;

    @SerializedName("sideOfStreet")
    private String sideOfStreet;

    @SerializedName("type")
    private String type;

    @SerializedName("adminArea6Type")
    private String adminArea6Type;

    @SerializedName("geocodeQualityCode")
    private String geocodeQualityCode;

    @SerializedName("adminArea4Type")
    private String adminArea4Type;

    @SerializedName("linkId")
    private String linkId;

    @SerializedName("street")
    private String street;

    @SerializedName("adminArea5Type")
    private String adminArea5Type;

    @SerializedName("mapUrl")
    private String mapUrl;

    @SerializedName("geocodeQuality")
    private String geocodeQuality;

    @SerializedName("adminArea1Type")
    private String adminArea1Type;

    @SerializedName("adminArea3Type")
    private String adminArea3Type;

    @SerializedName("latLng")
    private LatLng latLng;

    public boolean isDragPoint() {
        return dragPoint;
    }

    public DisplayLatLng getDisplayLatLng() {
        return displayLatLng;
    }

    public String getAdminArea4() {
        return adminArea4;
    }

    public String getUnknownInput() {
        return unknownInput;
    }

    public String getAdminArea5() {
        return adminArea5;
    }

    public String getAdminArea6() {
        return adminArea6;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAdminArea1() {
        return adminArea1;
    }

    public String getAdminArea3() {
        return adminArea3;
    }

    public String getSideOfStreet() {
        return sideOfStreet;
    }

    public String getType() {
        return type;
    }

    public String getAdminArea6Type() {
        return adminArea6Type;
    }

    public String getGeocodeQualityCode() {
        return geocodeQualityCode;
    }

    public String getAdminArea4Type() {
        return adminArea4Type;
    }

    public String getLinkId() {
        return linkId;
    }

    public String getStreet() {
        return street;
    }

    public String getAdminArea5Type() {
        return adminArea5Type;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public String getGeocodeQuality() {
        return geocodeQuality;
    }

    public String getAdminArea1Type() {
        return adminArea1Type;
    }

    public String getAdminArea3Type() {
        return adminArea3Type;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}