package com.harshitcreations.tourguard;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DigitalIdActivity extends AppCompatActivity {

    private ImageView back_button_id;
    private Button goToDashboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_id);

        back_button_id = findViewById(R.id.back_button_id);
        goToDashboardBtn = findViewById(R.id.goToDashboardBtn);

        back_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DigitalIdActivity.this, Dashboard.class));
                finish();
            }
        });

        goToDashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DigitalIdActivity.this, Dashboard.class));
                finish();
            }
        });

    }

}