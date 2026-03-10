package com.harshitcreations.tourguard;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    Button goToDashboardBtn, planTripButton;
    private Button logoutBtn;

    TextView nameText, emailText, phoneText;
    String activeTripId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);
        logoutBtn = findViewById(R.id.logoutBtn);

        goToDashboardBtn = findViewById(R.id.goToDashboardBtn);
        planTripButton = findViewById(R.id.planNextBtn);

        loadUserProfile();
        goToDashboardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, PastTripsActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkTrips();
    }

    private void loadUserProfile() {

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<User> call = apiService.getProfile("Bearer " + token);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    nameText.setText(user.getFullName());
                    emailText.setText(user.getEmail());
                    phoneText.setText(user.getPhoneNumber());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkTrips() {

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<GetTrip> call = apiService.getTrips("Bearer " + token);

        call.enqueue(new Callback<GetTrip>() {

            @Override
            public void onResponse(Call<GetTrip> call, Response<GetTrip> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Trip> trips = response.body().getTrips();

                    if (trips == null || trips.isEmpty()) {

                        planTripButton.setText("Plan Your Next Trip");
                        planTripButton.setOnClickListener(v ->
                                startActivity(new Intent(Profile.this, TripPlan.class))
                        );
                        return;
                    }

                    boolean activeTripFound = false;

                    try {


                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy H:mm", Locale.getDefault());
                        Date now = new Date();

                        for (Trip trip : trips) {

                            try {

                                Date start = sdf.parse(trip.getStartingDandT());
                                Date end = sdf.parse(trip.getEndingDandT());

                                if (now.before(end)) {
                                    activeTripFound = true;
                                    activeTripId = trip.getId();   // store id
                                    break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (activeTripFound) {

                        planTripButton.setText("View On-going Trip");

                        String finalTripId = activeTripId;
                        String name = nameText.getText().toString();

                        planTripButton.setOnClickListener(v -> {
                            Intent intent = new Intent(Profile.this, Dashboard.class);

                            intent.putExtra("tripId", finalTripId);
                            intent.putExtra("name", name);

                            startActivity(intent);
                        });
                    } else {

                        planTripButton.setText("Plan Your Next Trip");

                        planTripButton.setOnClickListener(v ->
                                startActivity(new Intent(Profile.this, TripPlan.class))
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTrip> call, Throwable t) {

                Toast.makeText(Profile.this, "Failed to load trips", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleLogout() {

        TokenManager tokenManager = new TokenManager(this);
        tokenManager.clearToken();

        clearSavedPhoneNumberFromPrefs();

        Toast.makeText(Profile.this, "Logged out!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Profile.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }

    private void clearSavedPhoneNumberFromPrefs() {
        getSharedPreferences("tourguard_prefs", MODE_PRIVATE)
                .edit()
                .remove("saved_phone")
                .apply();
    }
}