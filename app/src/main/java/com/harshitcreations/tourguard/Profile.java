package com.harshitcreations.tourguard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    Button save, planTripButton;
    EditText name, email, adhaar;
    CardView profilecard1, profilecard;
    TextView toolbarSet, toolbarProfile, nameShow, phoneShow, emailShow, adhaarShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        save = findViewById(R.id.saveButton);
        planTripButton = findViewById(R.id.planTripButton);
        profilecard = findViewById(R.id.profileCard);
        profilecard1 = findViewById(R.id.profilecard1);
        name = findViewById(R.id.input_name_profile);
        email = findViewById(R.id.input_email_profile);
        adhaar = findViewById(R.id.input_adhaar_profile);
        toolbarSet = findViewById(R.id.toolbarSet);
        toolbarProfile = findViewById(R.id.toolbarProfile);
        nameShow = findViewById(R.id.nameShow);
        emailShow = findViewById(R.id.emailShow);
        phoneShow = findViewById(R.id.phoneShow);
        adhaarShow = findViewById(R.id.adhaarShow);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty()){

                    if (!email.getText().toString().isEmpty()) {

                        if (adhaar.length() == 12) {

//                                Save to backend

                            String name1 = name.getText().toString();
                            String email1 = email.getText().toString();
                            String adhaar1 = adhaar.getText().toString();

                            profilecard.setVisibility(View.GONE);
                            save.setVisibility(View.GONE);
                            toolbarSet.setVisibility(View.GONE);

                            nameShow.setText("Name: " + name1);
                            emailShow.setText("Email: " + email1);
                            adhaarShow.setText("Adhaar: " + adhaar1);

                            profilecard1.setVisibility(View.VISIBLE);
                            planTripButton.setVisibility(View.VISIBLE);
                            toolbarProfile.setVisibility(View.VISIBLE);


                        } else {
                            Toast.makeText(Profile.this, "Enter valid Adhaar", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Profile.this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Profile.this, "Enter valid Name", Toast.LENGTH_SHORT).show();
                }
            }

        });

        planTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, TripPlan.class);
                startActivity(intent);
                finish();
            }
        });

    }
}