package com.example.projectapi.api.entity;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lon")  //The key name from response
    private float longitude;

    @SerializedName("lat")
    private float latitude;

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


}
