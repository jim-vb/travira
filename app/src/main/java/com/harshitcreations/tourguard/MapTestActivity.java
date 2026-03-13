package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;

public class MapTestActivity extends AppCompatActivity {

    private WebView mapView;
    private boolean mapLoaded = false;
    private List<List<Double>> cachedRoute;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        mapView = findViewById(R.id.mapView);

        WebSettings webSettings = mapView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        mapView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

                mapLoaded = true;
                Log.d("MAP_TEST","Map Loaded");

                if(cachedRoute != null){
                    sendRouteToMap();
                }
            }
        });

        mapView.loadUrl("file:///android_asset/maps.html");

        fetchRouteFromAPI();
    }

    private void fetchRouteFromAPI(){

        ApiService apiService =
                ApiClient.getClient(MapTestActivity.this).create(ApiService.class);

        MonitoringRequest request =
                new MonitoringRequest(
                        "4",
                        "69b3acca948e60c949583a6c"
                );

        apiService.getTripMonitoring(request).enqueue(new retrofit2.Callback<MonitoringResponse>() {

            @Override
            public void onResponse(Call<MonitoringResponse> call,
                                   retrofit2.Response<MonitoringResponse> response) {

                if(response.isSuccessful() && response.body() != null){

                    cachedRoute = response.body().expected_route;

                    Log.d("MAP_TEST","Route size: " + cachedRoute.size());

                    if(mapLoaded){
                        sendRouteToMap();
                    }
                }
            }

            @Override
            public void onFailure(Call<MonitoringResponse> call, Throwable t) {

                Log.e("MAP_TEST","API Error: " + t.getMessage());
            }
        });
    }

    private void sendRouteToMap(){

        try {

            JSONArray array = new JSONArray();

            for(List<Double> point : cachedRoute){

                JSONArray coord = new JSONArray();
                coord.put(point.get(0)); // lng
                coord.put(point.get(1)); // lat

                array.put(coord);
            }

            String js = "receiveRouteFromAndroid(" + array.toString() + ")";

            Log.d("MAP_TEST","JS -> " + js);

            mapView.evaluateJavascript(js, null);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}