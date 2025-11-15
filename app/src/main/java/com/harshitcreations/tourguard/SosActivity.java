package com.harshitcreations.tourguard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;

public class SosActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private FrameLayout btnSOS;
    private LinearLayout layoutNormal, layoutCountdown, layoutActivated;
    private Button btnCancelCountdown, btnCancelActivated;
    private TextView tvCountdown;

    private CountDownTimer countDownTimer;
    private static final int COUNTDOWN_TIME = 10; // seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        // 🔹 Bind Views
        btnBack = findViewById(R.id.btnBack);
        btnSOS = findViewById(R.id.btnSOS);

        layoutNormal = findViewById(R.id.layoutNormal);
        layoutCountdown = findViewById(R.id.layoutCountdown);
        layoutActivated = findViewById(R.id.layoutActivated);

        btnCancelCountdown = findViewById(R.id.btnCancelCountdown);
        btnCancelActivated = findViewById(R.id.btnCancelActivated);

        tvCountdown = findViewById(R.id.tvCountdown);

        // 🔹 Back button
        btnBack.setOnClickListener(v -> finish());

        // 🔹 SOS button → start countdown
        btnSOS.setOnClickListener(v -> showCountdownLayout());

        // 🔹 Cancel Countdown → back to normal
        btnCancelCountdown.setOnClickListener(v -> {
            cancelTimer();
            showNormalLayout();
        });

        // 🔹 Cancel Activated → back to normal
        btnCancelActivated.setOnClickListener(v -> {
            showNormalLayout();
        });
    }

    // ================= Layout Handlers =================

    private void showNormalLayout() {
        layoutNormal.setVisibility(View.VISIBLE);
        layoutCountdown.setVisibility(View.GONE);
        layoutActivated.setVisibility(View.GONE);
    }

    private void showCountdownLayout() {
        layoutNormal.setVisibility(View.GONE);
        layoutCountdown.setVisibility(View.VISIBLE);
        layoutActivated.setVisibility(View.GONE);

        startCountdown();
    }

    private void showActivatedLayout() {
        layoutNormal.setVisibility(View.GONE);
        layoutCountdown.setVisibility(View.GONE);
        layoutActivated.setVisibility(View.VISIBLE);
    }

    // ================= Countdown Timer =================

    private void startCountdown() {
        cancelTimer(); // safety

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME * 1000L, 1000) {
            int timeLeft = COUNTDOWN_TIME;

            @Override
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText(String.valueOf(timeLeft));
                timeLeft--;
            }

            @Override
            public void onFinish() {
                showActivatedLayout();
                // 🔹 Start SOS process
                startSosProcess();
            }
        }.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void startSosProcess() {
        if (isNetworkAvailable()) {
            // Network available → push directly to your custom API
//            pushSosToServer();
        } else {
            // No network → start nearby device discovery & relay P2P
            startNearbyDiscovery();
        }
    }

    // ------------------- Network check -------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    // ------------------- Push to server -------------------
//    private void pushSosToServer() {
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//        // Example: use device phone number & current location
////        EmergencyRequest emergency = new EmergencyRequest(
////                getSavedPhoneNumberFromPrefs(),        // phone
//////                currentLat,          // latitude
//////                currentLng,          // longitude
////                "SOS! Please help"   // message
////        );
//
//        Call<Void> call = apiService.setEmergency(emergency);
//        call.enqueue(new retrofit2.Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                if (response.isSuccessful()) {
//                    runOnUiThread(() -> {
//                        Toast.makeText(SosActivity.this, "SOS sent successfully!", Toast.LENGTH_SHORT).show();
//                    });
//                } else {
//                    runOnUiThread(() -> {
//                        Toast.makeText(SosActivity.this, "Failed to send SOS!", Toast.LENGTH_SHORT).show();
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                t.printStackTrace();
//                runOnUiThread(() -> {
//                    Toast.makeText(SosActivity.this, "Error sending SOS: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//            }
//        });
//    }


    // ------------------- Nearby discovery -------------------
    private void startNearbyDiscovery() {
        // Here you would initialize your ConnectionsClient
        // Start advertising + discovery using Nearby Connections API
        // Implement the relay logic for sending the SOS to other devices
    }

    private String getSavedPhoneNumberFromPrefs() {
        return getSharedPreferences("tourguard_prefs", MODE_PRIVATE)
                .getString("saved_phone", null); // null means not saved
    }

}
