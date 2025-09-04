package com.harshitcreations.tourguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.appwrite.Client;
import io.appwrite.coroutines.CoroutineCallback;
import io.appwrite.exceptions.AppwriteException;
import io.appwrite.services.Account;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Client client = AppwriteConfig.getClient(MainActivity.this);
                client.setProject("68b6ad4500279297a573");
                Account account = new Account(client);

                try {
                    account.get(new CoroutineCallback<>((result, error) -> {
                        runOnUiThread(() -> {
                            if (error != null) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                Toast.makeText(MainActivity.this, "Login to continue!", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }


                            //                            Check in database if tripId is available or not

                            if (false){ // mtlb ki tripId available hai to direct dashboard khol de
                                Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(intent);
                                finish();

                            } else{
                                Toast.makeText(MainActivity.this, "Plan your trip!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, TripPlan.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }));
                } catch (AppwriteException e) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(MainActivity.this, "Login to continue!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        },500);

    }
}