package com.example.finalprojectapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectapi.api.entity.WeatherLocation;
import com.example.finalprojectapi.api.service.WeatherMapService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchQuery = findViewById(R.id.main_searchCity);
        Button searchButton = findViewById(R.id.main_searchButton);

        TextView cityName = findViewById(R.id.main_cityName);
        TextView cityID = findViewById(R.id.main_cityID);
        TextView cityLongitude = findViewById(R.id.main_cityLongitude);
        TextView cityLatitude = findViewById(R.id.main_cityLatitude);
        TextView cityWeatherDescription = findViewById(R.id.main_weatherDescription);
        ImageView cityWeatherIcon = findViewById(R.id.main_weatherIcon);



        //Creating retrofit for specified API
        Retrofit retrofitWeather = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create Services
        WeatherMapService weatherMapService = retrofitWeather.create(WeatherMapService.class);

        searchButton.setOnClickListener(v -> {
            String city = searchQuery.getText().toString();

            //Create Calls
            Call<WeatherLocation> callCity = weatherMapService.getWeather(city);
            callCity.enqueue(new Callback<WeatherLocation>() {
                @Override
                public void onResponse(Call<WeatherLocation> call, Response<WeatherLocation> response) {
                    WeatherLocation cityLocation = response.body();
                    cityName.setText(cityLocation.getName());
                    cityID.setText("" + cityLocation.getId());
                    cityLatitude.setText("" + cityLocation.getCoordinates().getLatitude());
                    cityLongitude.setText("" + cityLocation.getCoordinates().getLongitude());
                    cityWeatherDescription.setText(cityLocation.getWeather().get(0).getDescription());

                    String iconName = cityLocation.getWeather().get(0).getIcon(); //Combine with http://openweathermap.org/img/wn/10d@2x.png
                    String iconFullPath = "http://openweathermap.org/img/wn/" + iconName + "@2x.png";
                    Picasso.get().load(iconFullPath).into(cityWeatherIcon);
                }

                @Override
                public void onFailure(Call<WeatherLocation> call, Throwable t) {
                    Log.e("WEATHER ERROR", "Some info...");
                    Log.e("Info", call.toString());
                }
            });
        });


    }
}