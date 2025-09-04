package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.services.Account;

public class SettingsActivity extends AppCompatActivity {

    private ImageView back_button_settings;
    private Button logoutBtn;
    private Account account;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Client client = AppwriteConfig.getClient(this);
        client.setProject("68b6ad4500279297a573");
        account = new Account(client);

        back_button_settings = findViewById(R.id.back_button_settings);
        logoutBtn = findViewById(R.id.logoutBtn);

        back_button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });

    }

    public void handleLogout() {
        account.deleteSession("current", new CoroutineCallback<>((result, error) -> {
            runOnUiThread(() -> {
                if (error != null) {
                    Toast.makeText(SettingsActivity.this, "Logout failed! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }));
    }


}