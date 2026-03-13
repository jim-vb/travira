package com.harshitcreations.tourguard;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimulationApiClient {

    private static final String BASE_URL = "http://10.0.82.200:3000/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}