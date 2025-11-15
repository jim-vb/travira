package com.harshitcreations.tourguard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // ---------------- Users ----------------
    @POST("api/users/signup")
    Call<Void> saveUser(@Body UserRequest userRequest);

    @GET("api/users/login")
    Call<String> getUser(@Path("phone") String email, String password);

    // ---------------- Digital IDs ----------------
    @POST("api/digital-ids")
    Call<Void> saveDigitalId(@Body String digitalId);

    @GET("api/digital-ids/{id}")
    Call<String> getDigitalId(@Path("id") String id);

    // ---------------- Locations ----------------
    @POST("api/locations")
    Call<Void> saveLocation(@Body LocationRequest locationRequest);

    @GET("api/locations/{phone}")
    Call<String> getLocation(@Path("phone") String phone);

    // ---------------- Emergencies / SOS ----------------
    @POST("api/emergencies")
    Call<Void> setEmergency(@Body EmergencyRequest emergencyRequest);

    @GET("api/emergencies/{phone}")
    Call<String> getEmergency(@Path("phone") String phone);

    // ---------------- Safety ----------------
    @GET("api/safety/{phone}")
    Call<String> getSafety(@Path("phone") String phone);

    // ---------------- Alerts ----------------
    @GET("api/alerts")
    Call<List<String>> getAlerts();

    // ---------------- Check Connection ----------------
    @GET("check")
    Call<StatusResponse> getConnection();
}
