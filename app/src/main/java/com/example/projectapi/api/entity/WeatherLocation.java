package com.example.projectapi.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherLocation {
    private int id;
    private String name;
    @SerializedName("coord")
    private Coordinates coordinates;
    private ArrayList<Weather> weather;

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
