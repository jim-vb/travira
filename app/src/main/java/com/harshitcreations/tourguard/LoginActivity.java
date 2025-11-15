package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.services.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberEditText, nameEditTextLogin, emailEditTextLogin, emailEditTextLoginLogin, adhaarTextLogin, passwordTextLogin, passwordTextLoginLogin;
    private Button sendOtpButton, loginButton;
    private String phoneNumber, otp;
    ProgressDialog progressDialog;
    private LinearLayout otpLayout, loginLayout;
    private String userId;
    private TextView loginSwitch, subtitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//        Client client = AppwriteConfig.getClient(this);
//        client.setProject("68b6ad4500279297a573");
//        Account account = new Account(client);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        nameEditTextLogin = findViewById(R.id.nameEditTextLogin);
        emailEditTextLogin = findViewById(R.id.emailEditTextLogin);
        emailEditTextLoginLogin = findViewById(R.id.emailEditTextLoginLogin);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        adhaarTextLogin = findViewById(R.id.adhaarTextLogin);
        passwordTextLogin = findViewById(R.id.passwordTextLogin);
        passwordTextLoginLogin = findViewById(R.id.passwordTextLoginLogin);
        sendOtpButton = findViewById(R.id.sendOtpButton);
        otpLayout = findViewById(R.id.otpLayout);
        loginLayout = findViewById(R.id.loginLayout);
        loginSwitch = findViewById(R.id.loginSwitch);
        subtitle = findViewById(R.id.subtitle);

        loginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                subtitle.setText("Login to Continue");
            }
        });

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                phoneNumber = phoneNumberEditText.getText().toString();
                if (phoneNumber.length() != 10) {
                    Toast.makeText(LoginActivity.this, "Please enter valid phone number!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    phoneNumber = "+91" + phoneNumber;

                    if (nameEditTextLogin.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter valid name!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        String name = nameEditTextLogin.getText().toString();

                        if (emailEditTextLogin.getText().toString().isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please enter valid email id!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            String email = emailEditTextLogin.getText().toString();

                            if (adhaarTextLogin.getText().toString().isEmpty() || adhaarTextLogin.getText().length() == 12) {
                                Toast.makeText(LoginActivity.this, "Please enter valid aadhaar number!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                String aadhaar = adhaarTextLogin.getText().toString();

                                if (passwordTextLogin.getText().toString().isEmpty()) {
                                    Toast.makeText(LoginActivity.this, "Please enter strong password!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    String password = passwordTextLogin.getText().toString();

                                    ApiService apiService = ApiClient.getClient().create(ApiService.class);

                                    UserRequest userRequest = new UserRequest(
                                            name,
                                            email,
                                            phoneNumber,
                                            password,
                                            aadhaar
                                    );
                                    // API call
                                    Call<Void> call = apiService.saveUser(userRequest);

                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {

                                            if (response.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "User Saved Successfully", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                                Intent intent = new Intent(LoginActivity.this, Profile.class);
                                                startActivity(intent);
                                                finish();

                                            } else {
                                                Toast.makeText(LoginActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(LoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }


                            }
                        }
                    }

//                    SharedPreferences prefs = getSharedPreferences("MyData", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putString("phone", phoneNumber);
//                    editor.apply();
//
//
//                    UUID randomId = UUID.randomUUID();
//                    account.createPhoneToken(
//                            randomId.toString(),
//                            phoneNumber,
//                            new CoroutineCallback<>((result, error) -> {
//                                runOnUiThread(() -> {



//                    Send at backend...

//                    if (error != null) {
//                        Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        return;
//                    }

//                    Toast.makeText(LoginActivity.this, "OTP sent!", Toast.LENGTH_SHORT).show();

//                    userId = result.getUserId();
//
//                    loginLayout.setVisibility(View.VISIBLE);
//                    otpLayout.setVisibility(View.GONE);
//                                });
//                            })
//                    );
                }
            }
        });

//        loginButton.setOnClickListener(v -> {
//
//
//            progressDialog.show();
//
//
////            account.updatePhoneSession(
////                    userId,
////                    otp,
////                    new CoroutineCallback<>((session, error) -> {
////                        runOnUiThread(() -> {
//                            progressDialog.dismiss();
////                            if (error != null) {
////                                Toast.makeText(LoginActivity.this, "Verification failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
////                                return;
////                            }
//
//            ApiService apiService = ApiClient.getClient().create(ApiService.class);
//            apiService.getConnection().enqueue(new retrofit2.Callback<StatusResponse>() {
//                @Override
//                public void onResponse(Call<StatusResponse> call, retrofit2.Response<StatusResponse> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        Toast.makeText(LoginActivity.this,
//                                "API Connected: " + response.body().getStatus(),
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this,
//                                "API Error: " + response.code(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<StatusResponse> call, Throwable t) {
//                    Toast.makeText(LoginActivity.this,
//                            "Connection Failed: " + t.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    Log.e("API_CHECK", "Connection Failed", t);
//                }
//            });
//
//
//
//                            // Save phone number to SharedPreferences for auto-login next time
////                            savePhoneNumberToPrefs(phoneNumber);
//
//                            // Check if tripId is available or not
////                            if (false) { // Assume tripId available means direct dashboard

//                            }

//                        });
//                    })
//            );
//        });

    }

    // Save phone number to SharedPreferences
    private void savePhoneNumberToPrefs(String phone) {
        getSharedPreferences("tourguard_prefs", MODE_PRIVATE)
                .edit()
                .putString("saved_phone", phone)
                .apply();
    }

}
