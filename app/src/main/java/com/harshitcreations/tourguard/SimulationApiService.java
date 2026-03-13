package com.harshitcreations.tourguard;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SimulationApiService {

    @POST("location")
    Call<LocationResponse> sendLocation(@Body LocationUpdateRequest request);

    @GET("/alerts")
    Call<AlertHistoryResponse> getAlerts(
            @Query("user_id") String userId,
            @Query("trip_id") String tripId
    );
}