package com.example.projectapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.projectapi.api.entity.WeatherLocation;
import com.example.projectapi.api.service.WeatherMapService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardFragment extends Fragment {

    Button btn_signOut;
//    NavController navController;
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/";


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_signOut = view.findViewById(R.id.btn_signOut);


//        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);



        btn_signOut.setOnClickListener(view1 -> {

            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
            startActivity(new Intent(requireActivity(), com.example.projectapi.DefaultActivity.class));

        });

        EditText searchQuery = view.findViewById(R.id.main_searchCity);
        Button searchButton = view.findViewById(R.id.main_searchButton);

        TextView cityName = view.findViewById(R.id.main_cityName);
        TextView cityID = view.findViewById(R.id.main_cityID);
        TextView cityLongitude = view.findViewById(R.id.main_cityLongitude);
        TextView cityLatitude = view.findViewById(R.id.main_cityLatitude);
        TextView cityWeatherDescription = view.findViewById(R.id.main_weatherDescription);
        ImageView cityWeatherIcon = view.findViewById(R.id.main_weatherIcon);



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