package com.harshitcreations.tourguard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class GreetingViewHolder extends RecyclerView.ViewHolder {

    TextView greetingText;
    ImageView settingsBtn;

    public interface OnSettingsClicked {
        void onSettingsClick();
    }

    public GreetingViewHolder(View itemView, OnSettingsClicked listener) {
        super(itemView);

        greetingText = itemView.findViewById(R.id.greetingText);
        settingsBtn = itemView.findViewById(R.id.settingsBtn);

        settingsBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSettingsClick();
            }
        });
    }

    public void bind(String userName) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning";
        } else if (hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }

        String firstName = userName.split(" ")[0];
        greetingText.setText(greeting + ", " + firstName + "!");
    }
}