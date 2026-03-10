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

    private EditText emailEditTextLogin, passwordTextLogin;
    private Button loginButton;
    ProgressDialog progressDialog;
    private TextView loginSwitch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        emailEditTextLogin = findViewById(R.id.emailEditTextLogin);
        passwordTextLogin = findViewById(R.id.passwordTextLogin);
        loginSwitch = findViewById(R.id.loginSwitch);
        loginButton = findViewById(R.id.loginButtonLogin);

        loginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailEditTextLogin.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter valid email!", Toast.LENGTH_SHORT).show();
                } else {
                    if (passwordTextLogin.getText().toString().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Enter valid password!", Toast.LENGTH_SHORT).show();
                    } else {

                        String email = emailEditTextLogin.getText().toString();
                        String password = passwordTextLogin.getText().toString();

                        ApiService apiService = ApiClient.getClient(LoginActivity.this).create(ApiService.class);

                        CheckUser checkUser = new CheckUser(email, password);

                        Call<LoginResponse> call = apiService.getUser(checkUser);

                        call.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                                if (!response.isSuccessful() || response.body() == null) {
                                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String token = response.body().getToken(); // <-- Yaha se token mil gaya!

                                if (token != null && !token.isEmpty()) {

                                    TokenManager tokenManager = new TokenManager(LoginActivity.this);
                                    tokenManager.saveToken(token);

                                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, Profile.class);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid login", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            }
        });
    }

}
