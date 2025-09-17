package com.harshitcreations.tourguard;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
}
