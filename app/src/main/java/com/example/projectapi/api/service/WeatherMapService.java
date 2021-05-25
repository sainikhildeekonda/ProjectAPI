package com.example.projectapi.api.service;


//Kinda like our Dao

import com.example.projectapi.api.entity.WeatherLocation;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherMapService {

    @GET("weather?appid=b33f168a4e526bcffb5878682d4e1d96")
    Call<WeatherLocation> getWeather(@Query("q") String city);
}
