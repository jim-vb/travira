package com.harshitcreations.tourguard;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripItineraryActivity extends AppCompatActivity {

    EditText tripName, startLocation, endLocation;
    EditText startDate, endDate;
    EditText stops, transport, hotel;
    EditText contactName, emergencyContact;

    Button updateTripBtn;

    String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_itinerary);

        initViews();

        tripId = getIntent().getStringExtra("tripId");

        if (tripId == null || tripId.trim().isEmpty()) {
            Toast.makeText(this, "Trip ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadTripDetails();

        updateTripBtn.setOnClickListener(v -> updateTrip());
    }

    private void initViews() {

        tripName = findViewById(R.id.tripName);
        startLocation = findViewById(R.id.startLocation);
        endLocation = findViewById(R.id.endLocation);

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        stops = findViewById(R.id.stops);
        transport = findViewById(R.id.transport);
        hotel = findViewById(R.id.hotel);

        contactName = findViewById(R.id.contactName);
        emergencyContact = findViewById(R.id.emergencyContact);

        updateTripBtn = findViewById(R.id.updateTripBtn);
    }

    private void loadTripDetails() {

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Call<TripResponse> call = apiService.getTripById("Bearer " + token, tripId);

        call.enqueue(new Callback<TripResponse>() {

            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Trip trip = response.body().getTrip();

                    if (trip == null) {
                        Toast.makeText(TripItineraryActivity.this,
                                "Trip data not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tripName.setText(safeText(trip.getTripName()));
                    startLocation.setText(safeText(trip.getStartingLocation()));
                    endLocation.setText(safeText(trip.getEndingLocation()));

                    startDate.setText(safeText(trip.getStartingDandT()));
                    endDate.setText(safeText(trip.getEndingDandT()));

                    stops.setText(safeText(trip.getIntermediateStops()));
                    transport.setText(safeText(trip.getTransportation()));

                    hotel.setText(safeText(trip.getHotelName()));

                    emergencyContact.setText(safeText(trip.getContactName()));
                    contactName.setText(safeText(trip.getEmergencyContact()));
                }
                else {
                    Toast.makeText(TripItineraryActivity.this,
                            "Failed to load trip", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {

                Toast.makeText(TripItineraryActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTrip() {

        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getToken();

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Trim all inputs
        String name = tripName.getText().toString().trim();
        String startLoc = startLocation.getText().toString().trim();
        String endLoc = endLocation.getText().toString().trim();
        String start = startDate.getText().toString().trim();
        String end = endDate.getText().toString().trim();
        String stop = stops.getText().toString().trim();
        String trans = transport.getText().toString().trim();
        String hotelName = hotel.getText().toString().trim();
        String contact = contactName.getText().toString().trim();
        String emergency = emergencyContact.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            tripName.setError("Trip name required");
            tripName.requestFocus();
            return;
        }

        if (startLoc.isEmpty()) {
            startLocation.setError("Starting location required");
            startLocation.requestFocus();
            return;
        }

        if (endLoc.isEmpty()) {
            endLocation.setError("Ending location required");
            endLocation.requestFocus();
            return;
        }

        if (start.isEmpty()) {
            startDate.setError("Start date required");
            startDate.requestFocus();
            return;
        }

        if (end.isEmpty()) {
            endDate.setError("End date required");
            endDate.requestFocus();
            return;
        }

        if (!contact.isEmpty() && contact.length() < 10) {
            contactName.setError("Invalid phone number");
            contactName.requestFocus();
            return;
        }

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Trip trip = new Trip(
                name,
                startLoc,
                endLoc,
                start,
                end,
                stop,
                trans,
                hotelName,
                emergency,
                contact
        );

        Call<Void> call = apiService.updateTrip("Bearer " + token, tripId, trip);

        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(TripItineraryActivity.this,
                            "Trip Updated Successfully", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(TripItineraryActivity.this,
                            "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Toast.makeText(TripItineraryActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Prevent null values in UI
    private String safeText(String value) {
        return value == null ? "" : value.trim();
    }
}