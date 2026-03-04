package com.harshitcreations.tourguard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DashboardItem> items;
    private Context context;
    private OnLocationRefreshListener refreshListener;
    private OnAlertClicked alertClicked;

    public DashboardAdapter(Context context, List<DashboardItem> items, OnLocationRefreshListener refreshListener, OnAlertClicked alertClicked) {
        this.context = context;
        this.items = items;
        this.refreshListener = refreshListener;
        this.alertClicked = alertClicked;
//        this.logoutListener = logoutListener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case DashboardItemType.GREETING:
                view = LayoutInflater.from(context).inflate(R.layout.greeting_item, parent, false);
                return new GreetingViewHolder(view);

            case DashboardItemType.SAFETY_SCORE:
                view = LayoutInflater.from(context).inflate(R.layout.safety_score_item, parent, false);
                return new SafetyScoreViewHolder(view);

            case DashboardItemType.QUICK_ACCESS:
                view = LayoutInflater.from(context).inflate(R.layout.quick_access_item, parent, false);
                return new QuickAccessViewHolder(view);

            case DashboardItemType.LOCATION:
                view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
                return new LocationViewHolder(view);

            case DashboardItemType.SAFETY_TIP:
                view = LayoutInflater.from(context).inflate(R.layout.safety_tip_item, parent, false);
                return new SafetyTipViewHolder(view);

            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DashboardItem item = items.get(position);

        switch (item.getType()) {
            case DashboardItemType.GREETING:
                ((GreetingViewHolder) holder).bind(item.getData());

                ((GreetingViewHolder) holder).settingsBtn.setOnClickListener(v -> {
                    context.startActivity(new Intent(context, SettingsActivity.class));
                });

                break;

//            case DashboardItemType.SAFETY_SCORE:
//
//
//
//
//                ((SafetyScoreViewHolder) holder).safetyScoreText.setText("");

            case DashboardItemType.LOCATION:
                LocationViewHolder locationHolder = (LocationViewHolder) holder;
                locationHolder.locationTextView.setText(item.getData());

                locationHolder.refreshButton.setOnClickListener(v -> {
                    if (refreshListener != null) {
                        refreshListener.onRefreshRequested();
                    }
                });

                break;

            // Similarly handle other view types with dynamic data
            case DashboardItemType.QUICK_ACCESS:
                ((QuickAccessViewHolder) holder).planButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, ItineraryActivity.class));
                    }
                });

                ((QuickAccessViewHolder) holder).sosButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, SosActivity.class));
                    }
                });

                ((QuickAccessViewHolder) holder).viewIdButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, DigitalIdActivity.class));
                    }
                });

                ((QuickAccessViewHolder) holder).alertButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertClicked.onAlertReceived();

                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnLocationRefreshListener {
        void onRefreshRequested();
    }

    public interface OnAlertClicked {
        void onAlertReceived();
    }
//    public interface OnLogoutClicked {
//        void onLogout();
//    }


}

