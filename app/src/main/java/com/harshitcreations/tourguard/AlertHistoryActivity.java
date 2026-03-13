package com.harshitcreations.tourguard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AlertAdapter adapter;

    List<AlertItem> alerts = new ArrayList<>();

    String userId = "69b3acca948e60c949583a6c";
    String tripId = "6";

    SimulationApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_history);

        recyclerView = findViewById(R.id.recyclerAlerts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AlertAdapter(alerts, this::showAlertPopup);
        recyclerView.setAdapter(adapter);

        apiService = SimulationApiClient.getClient().create(SimulationApiService.class);

        fetchAlerts();
    }

    private void fetchAlerts(){

        apiService.getAlerts(userId,tripId).enqueue(new Callback<AlertHistoryResponse>() {

            @Override
            public void onResponse(Call<AlertHistoryResponse> call, Response<AlertHistoryResponse> response) {

                if(response.isSuccessful() && response.body()!=null){

                    alerts.clear();

                    for(AlertRaw raw : response.body().alerts){

                        AlertItem item = new AlertItem();

                        item.id = raw.id;
                        item.title = raw.title;
                        item.message = raw.message;
                        item.severity = raw.severity;
                        item.type = raw.type;
                        item.created_at = raw.created_at;

                        if(raw.metadata!=null){

                            if(raw.metadata.lat!=null)
                                item.lat = raw.metadata.lat;

                            if(raw.metadata.lng!=null)
                                item.lng = raw.metadata.lng;
                        }

                        alerts.add(item);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AlertHistoryResponse> call, Throwable t) {

                t.printStackTrace();

            }
        });
    }

    private void showAlertPopup(AlertItem alert){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.high_risk_alert, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        TextView txtLocation = dialogView.findViewById(R.id.txtLocation);
        Button btnDismiss = dialogView.findViewById(R.id.btnDismiss);
        Button btnSOS = dialogView.findViewById(R.id.btnSOS);
        TextView btnClose = dialogView.findViewById(R.id.btnClose);

        txtLocation.setText(
                alert.title + "\n" +
                        "Severity: " + alert.severity + "\n" +
                        alert.message
        );

        btnDismiss.setOnClickListener(v -> dialog.dismiss());

        btnSOS.setOnClickListener(v -> {

            dialog.dismiss();
            startActivity(new Intent(this, SosActivity.class));
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
}