package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SafetyScoreViewHolder extends RecyclerView.ViewHolder {

    ProgressBar safetyProgress;
    TextView safetyScoreText;
    TextView safetyScoreDescription;

    public SafetyScoreViewHolder(View itemView) {
        super(itemView);

        safetyScoreText = itemView.findViewById(R.id.safetyScoreText);
        safetyScoreDescription = itemView.findViewById(R.id.safetyScoreDescription);
    }

    public void updateScore(int score, String level){


        safetyScoreText.setText(score + "%");

        safetyScoreDescription.setText(level);
    }
}
