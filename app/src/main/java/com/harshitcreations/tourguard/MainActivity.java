package com.harshitcreations.tourguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Account;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Appwrite client setup
//                Client client = AppwriteConfig.getClient(MainActivity.this);
//                client.setProject("68b6ad4500279297a573");
//                Account account = new Account(client);

//                try {
//                    // Try checking login status via Appwrite
//                    account.get(new CoroutineCallback<>((result, error) -> {
//                        runOnUiThread(() -> {
//                            if (error != null) {
//                                // Network issue, fallback to local storage
//                                handleLoginWithLocalStorage();
//                                return;
//                            }
//
//                            // If the user is logged in
//                            String userId = result.getId();
//                            String savedPhone = getSavedPhoneNumberFromPrefs();
//
//                            // Compare Appwrite result with saved phone number to ensure consistency
//                            if (savedPhone != null && savedPhone.equals(result.getPhone())) {
//                                // Now check tripId availability and proceed
//                                if (false) {  // If tripId available, go to Dashboard
//                                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {  // Else go to TripPlan
//                                    Intent intent = new Intent(MainActivity.this, TripPlan.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            } else {
//                                // Inconsistent, redirect to login
//                                handleLoginWithLocalStorage();
//                            }
//                        });
//                    }));
//                } catch (AppwriteException e) {
//                    // If exception occurs, fallback to local storage check
//                    handleLoginWithLocalStorage();
//                }
                handleLoginWithLocalStorage();
            }
        }, 500);
    }

    // Handle login using local storage
    private void handleLoginWithLocalStorage() {
        String savedPhone = getSavedPhoneNumberFromPrefs();

        if (savedPhone != null) {
            // If phone number is saved locally, verify tripId logic
//            if (false) {  // Assume tripId is available
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
//            } else {
//                Intent intent = new Intent(MainActivity.this, TripPlan.class);
//                startActivity(intent);
//                finish();
//            }
        } else {
            // If phone number is not saved locally, show login screen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(MainActivity.this, "Login to continue!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Retrieve saved phone number from SharedPreferences
    private String getSavedPhoneNumberFromPrefs() {
        return getSharedPreferences("tourguard_prefs", MODE_PRIVATE)
                .getString("saved_phone", null); // Returns null if no phone is saved
    }
}
