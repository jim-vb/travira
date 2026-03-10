package com.harshitcreations.tourguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText nameText, emailText, phoneText, aadhaarText, passwordText;
    private Button signUpButton;
    private TextView signupSwitch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameText = findViewById(R.id.nameEditTextSignUp);
        emailText = findViewById(R.id.emailEditTextSignUp);
        phoneText = findViewById(R.id.phoneNumberEditTextSignUp);
        aadhaarText = findViewById(R.id.adhaarTextSignUp);
        passwordText = findViewById(R.id.passwordTextSignUp);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        signupSwitch = findViewById(R.id.signupSwitch);

        signUpButton.setOnClickListener(v -> registerUser());
        signupSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }

    private void registerUser() {

        String name = nameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String aadhaar = aadhaarText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            nameText.setError("Enter name");
            return;
        }

        if (email.isEmpty()) {
            emailText.setError("Enter email");
            return;
        }

        if (phone.length() != 10) {
            phoneText.setError("Enter valid phone number");
            return;
        }

        if (aadhaar.length() != 12) {
            aadhaarText.setError("Enter valid Aadhaar number");
            return;
        }

        if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters");
            return;
        }

        phone = "+91" + phone;

        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        UserRequest request = new UserRequest(
                name,
                email,
                phone,
                password,
                aadhaar
        );

        Call<Void> call = apiService.saveUser(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUp.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(SignUp.this,
                            "Signup failed: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

                Toast.makeText(SignUp.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}