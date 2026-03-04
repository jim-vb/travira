package com.harshitcreations.tourguard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripPlan extends AppCompatActivity {

    private EditText inputStart, inputDestination, inputVehicle, inputHotel,
            inputAadhaar, inputEmergencyName, inputEmergencyNumber, inputName, inputEmail;
    private Button btnGenerateTrip, btnAddStop;
    private Spinner spinnerRelation;
    private LinearLayout stopContainer;
    private int stopCount = 0; // stop numbering ke liye

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plan);

        spinnerRelation = findViewById(R.id.spinner_relation);

// Spinner options
        String[] relations = {
                "Select Relation",
                "Parent",
                "Spouse",
                "Sibling",
                "Friend",
                "Relative",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                relations
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRelation.setAdapter(adapter);


        // 🔹 Initialize fields
        inputStart = findViewById(R.id.input_start);
        inputDestination = findViewById(R.id.input_destination);
        inputVehicle = findViewById(R.id.input_vehicle);
        inputHotel = findViewById(R.id.input_hotel);
        inputEmergencyName = findViewById(R.id.input_emergency_name);
        inputEmergencyNumber = findViewById(R.id.input_emergency_number);
        spinnerRelation = findViewById(R.id.spinner_relation);
        btnGenerateTrip = findViewById(R.id.btn_generate_trip);
        stopContainer = findViewById(R.id.stopContainer);
        btnAddStop = findViewById(R.id.btn_add_stop);


        btnGenerateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        btnAddStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewStopField();
            }
        });

    }

    private void validateForm() {

        // 🔹 Starting Point
        if (TextUtils.isEmpty(inputStart.getText().toString().trim())) {
            inputStart.setError("Enter starting location");
            inputStart.requestFocus();
            return;
        }

        // 🔹 Destination
        if (TextUtils.isEmpty(inputDestination.getText().toString().trim())) {
            inputDestination.setError("Enter destination");
            inputDestination.requestFocus();
            return;
        }

        // 🔹 Vehicle / Transport
        if (TextUtils.isEmpty(inputVehicle.getText().toString().trim())) {
            inputVehicle.setError("Enter vehicle / transport info");
            inputVehicle.requestFocus();
            return;
        }

        // 🔹 Hotel
        if (TextUtils.isEmpty(inputHotel.getText().toString().trim())) {
            inputHotel.setError("Enter hotel name");
            inputHotel.requestFocus();
            return;
        }

        // 🔹 Emergency Contact Name
        if (TextUtils.isEmpty(inputEmergencyName.getText().toString().trim())) {
            inputEmergencyName.setError("Enter emergency contact name");
            inputEmergencyName.requestFocus();
            return;
        }

        // 🔹 Emergency Contact Number (10 digits)
        String number = inputEmergencyNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            inputEmergencyNumber.setError("Enter emergency contact number");
            inputEmergencyNumber.requestFocus();
            return;
        }
        if (number.length() != 10) {
            inputEmergencyNumber.setError("Enter valid 10-digit number");
            inputEmergencyNumber.requestFocus();
            return;
        }

        // 🔹 Relation Spinner
        if (spinnerRelation.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select relation", Toast.LENGTH_SHORT).show();
            spinnerRelation.requestFocus();
            return;
        }

        // 🔹 Stops Validation (jitne bhi add kiye gaye unme se koi empty na ho)
        // 🔹 Single string jisme sab stops comma-separated store honge
        StringBuilder stopBuilder = new StringBuilder();

        for (int i = 0; i < stopContainer.getChildCount(); i++) {
            View child = stopContainer.getChildAt(i);

            if (child instanceof LinearLayout) {
                LinearLayout stopLayout = (LinearLayout) child;

                for (int j = 0; j < stopLayout.getChildCount(); j++) {
                    View innerChild = stopLayout.getChildAt(j);

                    if (innerChild instanceof EditText) {
                        EditText stopField = (EditText) innerChild;
                        String stopText = stopField.getText().toString().trim();

                        if (TextUtils.isEmpty(stopText)) {
                            stopField.setError("Enter stop location");
                            stopField.requestFocus();
                            return;
                        }

                        // 👉 Add to single string (comma separated)
                        if (stopBuilder.length() > 0) {
                            stopBuilder.append(",");
                        }
                        stopBuilder.append(stopText);
                    }
                }
            }
        }

// 🔹 Final comma-separated string
        String stopsCommaSeparated = stopBuilder.toString();



        // ✅ Agar sab valid hai

        String tripName = "null";
        String startingLocation = inputStart.getText().toString();
        String endingLocation = inputDestination.getText().toString();
        String startingDandT = "null";
        String endingDandT = "null";
        String intermediateStops = stopsCommaSeparated;
        String transportation = inputVehicle.getText().toString();
        String hotelName = inputHotel.getText().toString();
        String contactName = inputEmergencyName.getText().toString();
        String emergencyContact = inputEmergencyNumber.getText().toString();

        Trip trip = new Trip(tripName, startingLocation, endingLocation, startingDandT, endingDandT, intermediateStops, transportation, hotelName, contactName, emergencyContact);

        ApiService apiService = ApiClient.getClient(TripPlan.this).create(ApiService.class);

        Call<Trip> call = apiService.saveTrip(trip);

        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(TripPlan.this, "Trip Created!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(TripPlan.this, DigitalIdActivity.class);
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


//        save all the trip details at the database
//
//        String tripId = "TRIP-" + System.currentTimeMillis() + UUID.randomUUID().toString();
//
//        Intent intent = new Intent(TripPlan.this, DigitalIdActivity.class);
//        startActivity(intent);
//        finish();
//
//
//        Toast.makeText(this, "Trip ID Generated Successfully ✅", Toast.LENGTH_LONG).show();

    }



    private void addNewStopField() {
        stopCount++;

        // Parent layout banate hain (EditText + Button ko hold karega)
        LinearLayout stopLayout = new LinearLayout(this);
        stopLayout.setOrientation(LinearLayout.HORIZONTAL);
        stopLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        stopLayout.setPadding(0, 8, 0, 8);

        // Stop ka EditText
        EditText newStop = new EditText(this);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1  // Weight = 1 (poora space lega)
        );
        newStop.setLayoutParams(etParams);
        newStop.setHint("Stop " + stopCount);
        newStop.setBackgroundResource(android.R.drawable.edit_text);
        newStop.setPadding(10, 10, 10, 10);
        newStop.setTextColor(Color.BLACK);

        // Remove button
        Button removeBtn = new Button(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        removeBtn.setLayoutParams(btnParams);
        removeBtn.setText("❌");
        removeBtn.setBackgroundColor(Color.TRANSPARENT);
        removeBtn.setTextSize(18);

        // Click karte hi pura stopLayout delete hoga
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopContainer.removeView(stopLayout);
            }
        });

        // Add EditText & Button in parent layout
        stopLayout.addView(newStop);
        stopLayout.addView(removeBtn);

        // Finally add parent layout in stopContainer
        stopContainer.addView(stopLayout);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(TripPlan.this, Dashboard.class);
        startActivity(intent);

        super.onBackPressed();
    }

}