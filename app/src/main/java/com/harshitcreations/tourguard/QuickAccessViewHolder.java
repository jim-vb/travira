package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class QuickAccessViewHolder extends RecyclerView.ViewHolder {
    LinearLayout viewIdButton, planButton, alertButton, sosButton;

    public QuickAccessViewHolder(View itemView) {
        super(itemView);
        viewIdButton = itemView.findViewById(R.id.viewIdButton);
        planButton = itemView.findViewById(R.id.planButton);
        alertButton = itemView.findViewById(R.id.alertButton);
        sosButton = itemView.findViewById(R.id.sosButton);
    }
}
