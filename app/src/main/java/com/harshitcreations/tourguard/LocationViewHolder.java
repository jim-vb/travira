package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class LocationViewHolder extends RecyclerView.ViewHolder {
    TextView locationTextView;
    TextView locationLastUpdateView;
    ImageButton refreshButton;

    public LocationViewHolder(View itemView) {
        super(itemView);
        locationTextView = itemView.findViewById(R.id.locationTextView);
        refreshButton = itemView.findViewById(R.id.refreshButton);
        locationLastUpdateView = itemView.findViewById(R.id.locationLastUpdateView);
    }
}
