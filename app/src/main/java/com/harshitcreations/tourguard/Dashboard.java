package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import android.location.Location;
import com.google.android.gms.location.LocationResult;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;


public class Dashboard extends AppCompatActivity {

    private DashboardAdapter adapter;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;

    private List<DashboardItem> itemList;

    String phone_number;

    private int greetingIndex = 0;
    private int safetyScoreIndex = 1;
    private int quickAccessIndex = 2;
    private int locationIndex = 3;
    private int safetyTipIndex = 4;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        checkKreShadni();

        phone_number = getSavedPhoneNumberFromPrefs();


        RecyclerView recyclerView = findViewById(R.id.dashboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();
        String name = getIntent().getStringExtra("name");
        String tripId = getIntent().getStringExtra("tripId");

        if (name == null || name.isEmpty()) {
            name = "Traveller";
        }

        itemList.add(new DashboardItem(DashboardItemType.GREETING, name));
        itemList.add(new DashboardItem(DashboardItemType.SAFETY_SCORE, null));
        itemList.add(new DashboardItem(DashboardItemType.QUICK_ACCESS, null));
        itemList.add(new DashboardItem(DashboardItemType.LOCATION, "Fetching location..."));
        itemList.add(new DashboardItem(DashboardItemType.SAFETY_TIP, null));

        adapter = new DashboardAdapter(
                this,
                itemList,
                this::refreshLocation,
                this::showHighRiskAlert,
                () -> startActivity(new Intent(Dashboard.this, SettingsActivity.class)),
                tripId
        );

        recyclerView.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = getIntent().getStringExtra("name");
        if (name == null || name.isEmpty()) {
            name = "Traveller";
        }

        itemList.set(greetingIndex,
                new DashboardItem(DashboardItemType.GREETING, name));

        adapter.notifyItemChanged(greetingIndex);

        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );

        } else {
            refreshLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                refreshLocation();

            } else {

                Toast.makeText(this,
                        "Location permission denied!",
                        Toast.LENGTH_SHORT).show();

                itemList.set(locationIndex,
                        new DashboardItem(DashboardItemType.LOCATION,
                                "Unable to fetch right now"));

                adapter.notifyItemChanged(locationIndex);
            }
        }
    }

    private void getCurrentLocation(LocationResultCallback callback) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        getCityAndState(latitude, longitude, callback);

                    } else {

                        itemList.set(locationIndex,
                                new DashboardItem(DashboardItemType.LOCATION,
                                        "Location not available"));

                        adapter.notifyItemChanged(locationIndex);
                    }
                });
    }

    private void getCityAndState(double latitude, double longitude, LocationResultCallback callback) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {

            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();

                UserLocation result =
                        new UserLocation(latitude, longitude, city, state);

                callback.onLocationReceived(result);
            }

        } catch (IOException e) {

            Toast.makeText(this,
                    "Location error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public interface LocationResultCallback {
        void onLocationReceived(UserLocation userLocation);
    }

    public void refreshLocation() {

        getCurrentLocation(userLocation -> {

            String city = userLocation.getCity();
            String state = userLocation.getState();

            String locationText = city + ", " + state;

            String timeUpdated = "Updated " +
                    new java.text.SimpleDateFormat("hh:mm a",
                            java.util.Locale.getDefault())
                            .format(new java.util.Date());

            itemList.set(locationIndex,
                    new DashboardItem(
                            DashboardItemType.LOCATION,
                            locationText + "|" + timeUpdated
                    ));

            adapter.notifyItemChanged(locationIndex);

            uploadCurrentLocation(userLocation);
        });
    }

    private void showHighRiskAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.high_risk_alert, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        TextView txtLocation = dialogView.findViewById(R.id.txtLocation);
        Button btnDismiss = dialogView.findViewById(R.id.btnDismiss);
        Button btnSOS = dialogView.findViewById(R.id.btnSOS);
        TextView btnClose = dialogView.findViewById(R.id.btnClose);

        txtLocation.setText(
                "ACEM, Faridabad\nRisk Level: High • Reported incidents: 3 this week");

        btnDismiss.setOnClickListener(v -> dialog.dismiss());

        btnSOS.setOnClickListener(v -> {

            dialog.dismiss();
            startActivity(new Intent(this, SosActivity.class));
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void checkKreShadni() {

        ApiService apiService =
                ApiClient.getClient(Dashboard.this).create(ApiService.class);

        apiService.getConnection().enqueue(new retrofit2.Callback<StatusResponse>() {

            @Override
            public void onResponse(Call<StatusResponse> call,
                                   retrofit2.Response<StatusResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(Dashboard.this,
                            "API Connected: " + response.body().getStatus(),
                            Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Dashboard.this,
                            "API Error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Toast.makeText(Dashboard.this,
                        "Connection Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

                Log.e("API_CHECK", "Connection Failed", t);
            }
        });
    }


    private String getSavedPhoneNumberFromPrefs() {

        return getSharedPreferences("tourguard_prefs", MODE_PRIVATE)
                .getString("saved_phone", null);
    }

    private void uploadCurrentLocation(UserLocation userLocation) {

        ApiService apiService =
                ApiClient.getClient(Dashboard.this).create(ApiService.class);

        CurrentLocationRequest request = new CurrentLocationRequest(
                userLocation.getLatitude(),
                userLocation.getLongitude(),
                userLocation.getCity(),
                userLocation.getState(),
                false
        );

        apiService.saveCurrentLocation(request).enqueue(new retrofit2.Callback<StatusResponse>() {

            @Override
            public void onResponse(Call<StatusResponse> call,
                                   retrofit2.Response<StatusResponse> response) {

                if (response.isSuccessful()) {

                    Log.d("LOCATION_UPLOAD", "Location saved successfully");

                } else {

                    Log.e("LOCATION_UPLOAD", "Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Log.e("LOCATION_UPLOAD", "Error: " + t.getMessage());
            }
        });
    }

    private void startLocationUpdates() {

        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                30 * 1000   // 30 seconds
        )
                .setMinUpdateIntervalMillis(15 * 1000) // fastest allowed update
                .build();

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) return;

                Location location = locationResult.getLastLocation();

                if (location != null) {

                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    getCityAndState(lat, lng, result -> {

                        String locationText = result.getCity() + ", " + result.getState();

                        String timeUpdated = "Updated " +
                                new java.text.SimpleDateFormat("hh:mm a",
                                        java.util.Locale.getDefault())
                                        .format(new java.util.Date());

                        itemList.set(locationIndex,
                                new DashboardItem(
                                        DashboardItemType.LOCATION,
                                        locationText + "|" + timeUpdated
                                ));

                        adapter.notifyItemChanged(locationIndex);

                        uploadCurrentLocation(result);
                    });
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    getMainLooper()
            );
        }
    }

    private void stopLocationUpdates() {

        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}