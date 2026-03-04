package com.harshitcreations.tourguard;

import androidx.annotation.StringRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ---------------- Users ----------------
    @POST("api/users/signup")
    Call<Void> saveUser(@Body UserRequest userRequest);

    @POST("api/users/login")
    Call<LoginResponse> getUser(@Body CheckUser checkUser);

    // ---------------- Digital IDs ----------------
    @POST("api/trips")
    Call<Trip> saveTrip(@Body Trip trip);

    @POST("api/emergency-notifications/sos")
    Call<EmergencyRequest> setSos(@Body EmergencyRequest emergencyRequest);

    // ---------------- Locations ----------------
    @GET("api/safety-score")
    Call<String> getSafety();

    @GET("api/locations/{phone}")
    Call<String> getLocation(@Path("phone") String phone);

    // ---------------- Emergencies / SOS ----------------
    @POST("api/emergencies")
    Call<Void> setEmergency(@Body EmergencyRequest emergencyRequest);

    @GET("api/emergencies/{phone}")
    Call<String> getEmergency(@Path("phone") String phone);

    // ---------------- Safety ----------------
//    @GET("api/safety/{phone}")
//    Call<String> getSafety(@Path("phone") String phone);

    // ---------------- Alerts ----------------
    @GET("api/alerts")
    Call<List<String>> getAlerts();

    // ---------------- Check Connection ----------------
    @GET("check")
    Call<StatusResponse> getConnection();
}
