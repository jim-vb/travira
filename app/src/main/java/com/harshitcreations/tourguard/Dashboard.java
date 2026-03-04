package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private Button logoutButton;
    private DashboardAdapter adapter;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private List<DashboardItem> itemList;
    String phone_number;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        checkKreShadni();

//        Client client = AppwriteConfig.getClient(Dashboard.this);
//        client.setProject("68b6ad4500279297a573");
//        Account account = new Account(client);


////        Check in database if tripId is available or not
//        if (false){ // mtlb ki tripId available hai to direct dashboard khol de
//            Toast.makeText(Dashboard.this, "Logged In!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Dashboard.this, TripPlan.class);
//            startActivity(intent);
//            finish();
//
//        }




//        try {
//            account.get(new io.appwrite.coroutines.CoroutineCallback<>((user, error) -> {
//                runOnUiThread(() -> {
//                    if (error != null) {
////                        Toast.makeText(Dashboard.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        // yaha phone number milega
//                        phone_number = user.getPhone();
////                        Toast.makeText(Dashboard.this, "Phone: " + phoneNumber, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }));
//        } catch (AppwriteException e) {
//            throw new RuntimeException(e);
//        }

        phone_number = getSavedPhoneNumberFromPrefs();


//        logoutButton = findViewById(R.id.logoutButton);

//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                account.deleteSession("current", new CoroutineCallback<>((result, error) -> {
//                    runOnUiThread(() -> {
//                        if (error != null) {
//                            Toast.makeText(Dashboard.this, "Logout failed!"+error.getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            // Logout successful
//                            Toast.makeText(Dashboard.this, "Logged out!", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Dashboard.this, LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                }));
//
//            }
//        });

        String name = getIntent().getStringExtra("name");


        RecyclerView recyclerView = findViewById(R.id.dashboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();
        String greeting = getGreetingBasedOnTime();
        String fullMessage = greeting + name;
        itemList.add(new DashboardItem(DashboardItemType.GREETING, fullMessage));
        itemList.add(new DashboardItem(DashboardItemType.SAFETY_SCORE, null));
        itemList.add(new DashboardItem(DashboardItemType.QUICK_ACCESS, null));
        itemList.add(new DashboardItem(DashboardItemType.LOCATION, "Fetching location..."));
        itemList.add(new DashboardItem(DashboardItemType.SAFETY_TIP, null));


        adapter = new DashboardAdapter(this, itemList, new DashboardAdapter.OnLocationRefreshListener() {
            @Override
            public void onRefreshRequested() {
                refreshLocation();
            }
        }, new DashboardAdapter.OnAlertClicked() {
            @Override
            public void onAlertReceived() {
                showHighRiskAlert();
            }
        });


//        Current Location

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Step 1: Check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted → request karo
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already hai → location fetch karo
            refreshLocation();

        }

//        ApiService apiService = ApiClient.getClient(Dashboard.this).create(ApiService.class);
//
//        Call<Void> call = apiService.getSafety();
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//                if (!response.isSuccessful() || response.body() == null) {
//                    Toast.makeText(Dashboard.this, "Server error", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                String trip_id = response.message().toString();
//
//                if (trip_id != null && !trip_id.isEmpty()) {
//
//                    TokenManager tokenManager = new TokenManager(Dashboard.this);
//                    tokenManager.saveToken(trip_id);
//
//                    Toast.makeText(Dashboard.this, "Login Success!", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(Dashboard.this, Profile.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    Toast.makeText(Dashboard.this, "Invalid login", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(Dashboard.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });




        recyclerView.setAdapter(adapter);
    }

    public String getGreetingBasedOnTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour < 12) {
            return "Good Morning, ";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon, ";
        } else {
            return "Good Evening, ";
        }
    }




    // Step 2: Handle permission request ka result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission mil gayi → location fetch karo
                getCurrentLocation(locationResult -> {
                    // Result mil gaya
                    String city = locationResult.getCity();
                    String state = locationResult.getState();

                    // itemList ke LOCATION type wale item ko update karo
                    itemList.set(3, new DashboardItem(DashboardItemType.LOCATION,
                            city + ", " + state));
                    adapter.notifyItemChanged(3);

                });

            } else {
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show();
                itemList.set(3, new DashboardItem(DashboardItemType.LOCATION,
                        "Unable to fetch right now"));
                adapter.notifyItemChanged(3);
            }
        }
    }

    // Step 3: Get current coordinates
    private void getCurrentLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

//                        Toast.makeText(this, "Long & Lat:"+latitude+longitude, Toast.LENGTH_SHORT).show();

                        getCityAndState(latitude, longitude, callback);
                    } else {
                        Toast.makeText(Dashboard.this, "Location not available", Toast.LENGTH_SHORT).show();
                        itemList.set(3, new DashboardItem(DashboardItemType.LOCATION,
                                "Unable to fetch right now"));
                        adapter.notifyItemChanged(3);
                    }
                });
    }

    private void getCityAndState(double latitude, double longitude, LocationCallback callback) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();

                LocationResult result = new LocationResult(latitude, longitude, city, state);
                callback.onLocationReceived(result);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public interface LocationCallback {
        void onLocationReceived(LocationResult locationResult);
    }

    public void refreshLocation() {
        getCurrentLocation(locationResult -> {
            String city = locationResult.getCity();
            String state = locationResult.getState();

            int locationIndex = 3; // Location item ka index
            itemList.set(locationIndex, new DashboardItem(
                    DashboardItemType.LOCATION,
                    city + ", " + state
            ));
            adapter.notifyItemChanged(locationIndex);
            Toast.makeText(this, "Location refreshed", Toast.LENGTH_SHORT).show();




//            save location on api

            // ✅ API call to save/update location
//            ApiService api = ApiClient.getClient().create(ApiService.class);
//            LocationRequest request = new LocationRequest(
//                    phone_number, // <- yaha user ka actual phoneNumber pass karna
//                    locationResult.getLatitude(),
//                    locationResult.getLongitude()
//            );
//
//            api.saveLocation(request).enqueue(new retrofit2.Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(Dashboard.this, "Location updated on server", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(Dashboard.this, "Failed to update (Code: " + response.code() + ")", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Toast.makeText(Dashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
        });
    }

    private void showHighRiskAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.high_risk_alert, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Init views
        TextView txtLocation = dialogView.findViewById(R.id.txtLocation);
        Button btnDismiss = dialogView.findViewById(R.id.btnDismiss);
        Button btnSOS = dialogView.findViewById(R.id.btnSOS);
        TextView btnClose = dialogView.findViewById(R.id.btnClose);

        // Set location dynamically
        txtLocation.setText("ACEM, Faridabad\nRisk Level: High • Reported incidents: 3 this week");

        btnDismiss.setOnClickListener(v -> dialog.dismiss());

        btnSOS.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, SosActivity.class));
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void checkKreShadni() {
        ApiService apiService = ApiClient.getClient(Dashboard.this).create(ApiService.class);
        apiService.getConnection().enqueue(new retrofit2.Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
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
                .getString("saved_phone", null); // Returns null if no phone is saved
    }

}