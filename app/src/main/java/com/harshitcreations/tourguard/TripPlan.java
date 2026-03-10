package com.harshitcreations.tourguard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import android.net.Uri;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripPlan extends AppCompatActivity {

    private EditText startLocation, destinationLocation;
    private EditText startTime, endTime;
    private EditText intermediateStops;
    private EditText contactName, contactNumber;
    private EditText hotelName, transportType;

    private Button startTripBtn;
    Button previewRouteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plan);

        startLocation = findViewById(R.id.startLocation);
        destinationLocation = findViewById(R.id.destinationLocation);
        intermediateStops = findViewById(R.id.intermediateStops);
        previewRouteBtn = findViewById(R.id.previewRouteBtn);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        contactName = findViewById(R.id.contactName);
        contactNumber = findViewById(R.id.contactNumber);

        hotelName = findViewById(R.id.hotelName);
        transportType = findViewById(R.id.transportType);

        startTripBtn = findViewById(R.id.startTripBtn);

        // Disable typing for date fields
        startTime.setFocusable(false);
        endTime.setFocusable(false);

        startTime.setOnClickListener(v -> showDateTimePicker(startTime));
        endTime.setOnClickListener(v -> showDateTimePicker(endTime));
        previewRouteBtn.setOnClickListener(v -> openRoutePreview());

        startTripBtn.setOnClickListener(v -> validateForm());
    }

    private void validateForm() {

        if (TextUtils.isEmpty(startLocation.getText().toString().trim())) {
            startLocation.setError("Enter start location");
            return;
        }

        if (TextUtils.isEmpty(destinationLocation.getText().toString().trim())) {
            destinationLocation.setError("Enter destination");
            return;
        }

        if (TextUtils.isEmpty(startTime.getText().toString().trim())) {
            startTime.setError("Select start time");
            return;
        }

        if (TextUtils.isEmpty(endTime.getText().toString().trim())) {
            endTime.setError("Select end time");
            return;
        }

        if (TextUtils.isEmpty(contactName.getText().toString().trim())) {
            contactName.setError("Enter emergency contact name");
            return;
        }

        String number = contactNumber.getText().toString().trim();

        if (TextUtils.isEmpty(number)) {
            contactNumber.setError("Enter contact number");
            return;
        }

        if (number.length() != 10) {
            contactNumber.setError("Enter valid 10 digit number");
            return;
        }

        String tripName = "Tour Trip";

        String startingLocation = startLocation.getText().toString();
        String endingLocation = destinationLocation.getText().toString();

        String startingDandT = startTime.getText().toString();
        String endingDandT = endTime.getText().toString();

        String stops = intermediateStops.getText().toString();

        String transportation = transportType.getText().toString();
        String hotel = hotelName.getText().toString();

        String emergencyName = contactName.getText().toString();
        String emergencyContact = contactNumber.getText().toString();

        Trip trip = new Trip(
                tripName,
                startingLocation,
                endingLocation,
                startingDandT,
                endingDandT,
                stops,
                transportation,
                hotel,
                emergencyName,
                emergencyContact
        );

        ApiService apiService = ApiClient.getClient(TripPlan.this).create(ApiService.class);

        Call<Trip> call = apiService.saveTrip(trip);

        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(TripPlan.this, "Trip Created Successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(TripPlan.this, Profile.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(TripPlan.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {

                Toast.makeText(TripPlan.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateTimePicker(EditText targetField) {

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TripPlan.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            TripPlan.this,
                            (timeView, selectedHour, selectedMinute) -> {

                                String dateTime = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear
                                        + " " + selectedHour + ":" + selectedMinute;

                                targetField.setText(dateTime);

                            },
                            hour,
                            minute,
                            true
                    );

                    timePickerDialog.show();

                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void openRoutePreview() {

        String start = startLocation.getText().toString().trim();
        String destination = destinationLocation.getText().toString().trim();
        String stops = intermediateStops.getText().toString().trim();

        if (start.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Enter start and destination first", Toast.LENGTH_SHORT).show();
            return;
        }

        String uri;

        if (!stops.isEmpty()) {
            uri = "https://www.google.com/maps/dir/" + start + "/" + stops + "/" + destination;
        } else {
            uri = "https://www.google.com/maps/dir/" + start + "/" + destination;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(TripPlan.this, Profile.class);
        startActivity(intent);
        super.onBackPressed();
    }
}