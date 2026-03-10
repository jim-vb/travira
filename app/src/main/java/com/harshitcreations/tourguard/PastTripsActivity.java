package com.harshitcreations.tourguard;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastTripsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PastTripAdapter adapter;
    List<Trip> pastTrips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trips);

        recyclerView = findViewById(R.id.pastTripsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PastTripAdapter(pastTrips);
        recyclerView.setAdapter(adapter);

        loadPastTrips();
    }

    private void loadPastTrips() {

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<GetTrip> call = apiService.getTrips("Bearer " + token);

        call.enqueue(new Callback<GetTrip>() {

            @Override
            public void onResponse(Call<GetTrip> call, Response<GetTrip> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Trip> trips = response.body().getTrips();

                    if (trips == null) return;

                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy H:mm", Locale.getDefault());
                        Date now = new Date();

                        for (Trip trip : trips) {

                            Date end = sdf.parse(trip.getEndingDandT());

                            if (now.after(end)) {
                                pastTrips.add(trip);
                            }
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<GetTrip> call, Throwable t) {

                Toast.makeText(PastTripsActivity.this,
                        "Failed to load trips",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}