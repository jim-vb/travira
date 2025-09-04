package com.harshitcreations.tourguard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("signup")
    Call<Void> saveUser(@Body String user);

    @GET("login")
    Call<String> getUser(@Path("phone") String phone);

    @POST("trip")
    Call<Void> saveTrip(@Body String trip);

    @GET("trip/{phone}/{tripId}")
    Call<String> getTrip(@Path("phone") String phone, @Path("tripId") String tripId);

    @POST("location")
    Call<Void> saveLocation(@Body LocationRequest locationRequest);

    @GET("safety/{phone}")
    Call<String> getSafety(@Path("phone") String phone);

    @GET("check-zone")
    Call<List<String>> getAlerts();
}

