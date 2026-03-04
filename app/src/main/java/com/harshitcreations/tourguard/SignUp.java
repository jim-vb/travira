package com.harshitcreations.tourguard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText nameText, emailText, phoneNumberText, aadhaarNumberText, passwordText;
    private Button button;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameText = findViewById(R.id.nameEditTextSignUp);
        emailText = findViewById(R.id.emailEditTextSignUp);
        phoneNumberText = findViewById(R.id.phoneNumberEditTextSignUp);
        aadhaarNumberText = findViewById(R.id.adhaarTextSignUp);
        passwordText = findViewById(R.id.passwordTextSignUp);
        button = findViewById(R.id.signUpButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                String phoneNumber = phoneNumberText.getText().toString();
                if (phoneNumber.length() != 10) {
                    Toast.makeText(SignUp.this, "Please enter valid phone number!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    phoneNumber = "+91" + phoneNumber;

                    if (nameText.getText().toString().isEmpty()) {
                        Toast.makeText(SignUp.this, "Please enter valid name!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        String name = nameText.getText().toString();

                        if (emailText.getText().toString().isEmpty()) {
                            Toast.makeText(SignUp.this, "Please enter valid email id!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            String email = emailText.getText().toString();

                            if (aadhaarNumberText.getText().toString().isEmpty() || aadhaarNumberText.getText().length() != 12) {
                                Toast.makeText(SignUp.this, "Please enter valid aadhaar number!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                String aadhaar = aadhaarNumberText.getText().toString();

                                if (passwordText.getText().toString().isEmpty()) {
                                    Toast.makeText(SignUp.this, "Please enter strong password!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    String password = passwordText.getText().toString();

                                    ApiService apiService = ApiClient.getClient(SignUp.this).create(ApiService.class);

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
                                                Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(SignUp.this, "You can Login now!", Toast.LENGTH_SHORT).show();
                                                finish();

                                            } else {
                                                Toast.makeText(SignUp.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(SignUp.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }


                            }
                        }
                    }

                }

            }
        });

    }
}