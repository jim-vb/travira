package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class SafetyTipViewHolder extends RecyclerView.ViewHolder {
    LinearLayout safetyTipLayout;

    public SafetyTipViewHolder(View itemView) {
        super(itemView);
        safetyTipLayout = itemView.findViewById(R.id.safetyTipLayout);
    }
}
