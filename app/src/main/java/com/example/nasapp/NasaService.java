package com.example.nasapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaService {

    @GET("/planetary/apod")
    Call<NasaResponse> searchByDate(@Query("api_key") String api_key,
                                          @Query("date") String date);

    @GET("/planetary/apod")
    Call<NasaResponse> searchToday(@Query("api_key") String api_key);


}