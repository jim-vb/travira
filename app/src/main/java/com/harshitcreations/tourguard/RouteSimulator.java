package com.harshitcreations.tourguard;

import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteSimulator {

    private List<List<Double>> route;
    private int index = 0;

    private Handler handler = new Handler();
    private Random random = new Random();

    private SimulationApiService apiService;

    private String userId;
    private int tripId;

    /* -----------------------------
       SAFETY SCORE LISTENER
    ------------------------------ */

    public interface SafetyListener{
        void onSafetyUpdate(int score,String level);
    }

    private SafetyListener safetyListener;

    public void setSafetyListener(SafetyListener listener){
        this.safetyListener = listener;
    }

    /* -----------------------------
       MAP LOCATION LISTENER
    ------------------------------ */

    public interface LocationListener{
        void onLocationUpdate(
                double userLat,
                double userLng,
                double escortLat,
                double escortLng
        );
    }

    private LocationListener locationListener;

    public void setLocationListener(LocationListener listener){
        this.locationListener = listener;
    }

    public RouteSimulator(SimulationApiService apiService,
                          List<List<Double>> route,
                          String userId,
                          int tripId){

        this.apiService = apiService;
        this.route = route;
        this.userId = userId;
        this.tripId = tripId;
    }

    public void start(){
        handler.post(simulationLoop);
    }

    private Runnable simulationLoop = new Runnable() {
        @Override
        public void run() {

            if(route == null || route.isEmpty()) return;

            if(index >= route.size()){
                index = 0;
            }

            List<Double> point = route.get(index);

            double lng = point.get(0);
            double lat = point.get(1);
            double speed = 10;

            /* ------------------------
               SIMULATION CONDITIONS
            ------------------------- */

            // Route deviation
            if(index > 40 && index < 50){
                lat += random.nextDouble() * 0.01;
                lng += random.nextDouble() * 0.01;
                Log.d("SIM","Route deviation simulated");
            }

            // Inactivity
            if(index > 50 && index < 60){
                speed = 0;
                Log.d("SIM","User stopped");
            }

            // Speed anomaly
            if(index > 60 && index < 70){
                speed = 120;
                Log.d("SIM","High speed simulated");
            }

            /* ------------------------
               ESCORT SIMULATION
            ------------------------- */

            double escortLat = lat + (random.nextDouble() * 0.001);
            double escortLng = lng + (random.nextDouble() * 0.001);

            /* send location to map */

            if(locationListener != null){
                locationListener.onLocationUpdate(
                        lat,
                        lng,
                        escortLat,
                        escortLng
                );
            }

            sendLocation(lat,lng,speed);

            index++;

            handler.postDelayed(this,2000);
        }
    };

    private void sendLocation(double lat,double lng,double speed){

        String timestamp = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
        ).format(new Date());

        LocationUpdateRequest request =
                new LocationUpdateRequest(
                        userId,
                        tripId,
                        lat,
                        lng,
                        speed,
                        timestamp
                );

        apiService.sendLocation(request).enqueue(new Callback<LocationResponse>() {

            @Override
            public void onResponse(Call<LocationResponse> call,
                                   Response<LocationResponse> response) {

                if(response.isSuccessful() && response.body()!=null){

                    LocationResponse res = response.body();

                    Log.d("SIM","Location sent -> "+lat+","+lng);
                    Log.d("SIM","Safety Score -> "+res.safety_score);



                    if(safetyListener != null){
                        safetyListener.onSafetyUpdate(
                                res.safety_score,
                                res.safety_level
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {

                Log.e("SIM","Error "+t.getMessage());
            }
        });
    }
}