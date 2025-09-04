package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.services.Account;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberEditText, otpEditText;
    private Button sendOtpButton, loginButton;
    private String phoneNumber, otp;
    ProgressDialog progressDialog;
    private LinearLayout otpLayout, loginLayout;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Client client = AppwriteConfig.getClient(this);
        client.setProject("68b6ad4500279297a573");
        Account account = new Account(client);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        otpEditText = findViewById(R.id.otpEditText);
        sendOtpButton = findViewById(R.id.sendOtpButton);
        loginButton = findViewById(R.id.loginButton);
        otpLayout = findViewById(R.id.otpLayout);
        loginLayout = findViewById(R.id.loginLayout);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                phoneNumber = phoneNumberEditText.getText().toString();
                if (phoneNumber.length() != 10){
                    Toast.makeText(LoginActivity.this, "Please enter valid phone number!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    phoneNumber = "+91"+phoneNumber;
                    UUID randomId = UUID.randomUUID();
                    account.createPhoneToken(
                            randomId.toString(),
                            phoneNumber,
                            new CoroutineCallback<>((result, error) -> {
                                runOnUiThread(() -> {
                                    progressDialog.dismiss();
                                    if (error != null) {
                                        Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Toast.makeText(LoginActivity.this, "OTP sent!", Toast.LENGTH_SHORT).show();

                                    userId = result.getUserId();
                                    loginLayout.setVisibility(View.VISIBLE);
                                    otpLayout.setVisibility(View.GONE);
                                });
                            })
                    );
                }
            }
        });

        loginButton.setOnClickListener(v -> {
            otp = otpEditText.getText().toString();
            if (otp.length() != 6) {
                Toast.makeText(LoginActivity.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            account.updatePhoneSession(
                    userId,
                    otp,
                    new CoroutineCallback<>((session, error) -> {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            if (error != null) {
                                Toast.makeText(LoginActivity.this, "Verification failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

//                            Check in database if tripId is available or not

                            if (false){ // mtlb ki tripId available hai to direct dashboard khol de
                                Toast.makeText(LoginActivity.this, "Phone Verified! Session Created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                startActivity(intent);
                                finish();

                            } else{
                                Toast.makeText(LoginActivity.this, "Plan your trip!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, TripPlan.class);
                                startActivity(intent);
                                finish();
                            }

                            Toast.makeText(LoginActivity.this, "Phone Verified! Session Created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        });
                    })
            );
        });

    }
}