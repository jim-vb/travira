package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GreetingViewHolder extends RecyclerView.ViewHolder {
    TextView greetingText;
    ImageView settingsBtn;

    public GreetingViewHolder(View itemView) {
        super(itemView);
        greetingText = itemView.findViewById(R.id.greetingText);
        settingsBtn = itemView.findViewById(R.id.settingsBtn);
    }

    public void bind(String greeting) {
        greetingText.setText(greeting);
    }

}

