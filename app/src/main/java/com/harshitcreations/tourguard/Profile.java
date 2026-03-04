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

    Button goToDashboardBtn, planTripButton;
    TextView displayToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goToDashboardBtn = findViewById(R.id.goToDashboardBtn);
        planTripButton = findViewById(R.id.planNextBtn);
        displayToken = findViewById(R.id.displayToken);

        planTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, TripPlan.class);
                startActivity(intent);
                finish();
            }
        });

//        String token = getIntent().getStringExtra("token");
//
//        displayToken.setText(token);

    }
}