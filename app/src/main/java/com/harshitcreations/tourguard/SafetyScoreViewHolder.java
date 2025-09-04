package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SafetyScoreViewHolder extends RecyclerView.ViewHolder {
    ProgressBar safetyProgress;
    TextView safetyScoreText;

    public SafetyScoreViewHolder(View itemView) {
        super(itemView);
        safetyProgress = itemView.findViewById(R.id.safetyProgress);
        safetyScoreText = itemView.findViewById(R.id.safetyScoreText);
    }
}
