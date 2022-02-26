package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.model.apiModel.ResponseApi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiAdressService {
    String API_KEY = BuildConfig.API_KEY;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://open.mapquestapi.com/geocoding/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // For LatLng search
    @GET("address?key=" + API_KEY)
    Call<ResponseApi> getLatLng(@Query("location") String location);
}
