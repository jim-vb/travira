package com.harshitcreations.tourguard;

import androidx.annotation.StringRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ---------------- Users ----------------
    @POST("api/users/signup")
    Call<Void> saveUser(@Body UserRequest userRequest);

    @POST("api/users/login")
    Call<LoginResponse> getUser(@Body CheckUser checkUser);

    // ---------------- TRIPS ----------------
    @POST("api/trips")
    Call<Trip> saveTrip(@Body Trip trip);
//---------------------SOS FEATURE----------
    @POST("api/emergency-notifications/sos")
    Call<EmergencyRequest> setSos(@Body EmergencyRequest emergencyRequest);

//-------------------CONNECTION--------------
    @GET("check")
    Call<StatusResponse> getConnection();
    //------------------ME-------------------------------
    @GET("api/users/me")
    Call<User> getProfile(@Header("Authorization") String token);
    //----------------user-trip-infos--------------------
    @GET("/api/trips")
    Call<GetTrip> getTrips(@Header("Authorization") String token);
    //------------------ Current-location ---------------------------
    @POST("api/current-location")
    Call<StatusResponse> saveCurrentLocation(@Body CurrentLocationRequest request);

    @PUT("api/current-location")
    Call<StatusResponse> updateCurrentLocation(@Body CurrentLocationRequest request);

    //-------------------TRIP-Itinerary---------------------------------------

    @GET("api/trips/{tripId}")
    Call<TripResponse> getTripById(
            @Header("Authorization") String token,
            @Path("tripId") String tripId
    );

    @PUT("api/trips/{tripId}")
    Call<Void> updateTrip(
            @Header("Authorization") String token,
            @Path("tripId") String tripId,
            @Body Trip trip
    );
}
