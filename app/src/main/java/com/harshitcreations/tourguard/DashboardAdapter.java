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
    private GreetingViewHolder.OnSettingsClicked settingsClicked;

    private String tripId;

    public DashboardAdapter(Context context, List<DashboardItem> items,
                            OnLocationRefreshListener refreshListener,
                            OnAlertClicked alertClicked,
                            GreetingViewHolder.OnSettingsClicked settingsClicked,
                            String tripId) {

        this.context = context;
        this.items = items;
        this.refreshListener = refreshListener;
        this.alertClicked = alertClicked;
        this.settingsClicked = settingsClicked;
        this.tripId = tripId;
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
                return new GreetingViewHolder(view, settingsClicked);

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

                GreetingViewHolder greetingHolder = (GreetingViewHolder) holder;

                String userName = item.getData(); // username passed from Dashboard

                greetingHolder.bind(userName);

                break;


            case DashboardItemType.LOCATION:

                LocationViewHolder locationHolder = (LocationViewHolder) holder;

                String data = item.getData();

                if (data != null && data.contains("|")) {

                    String[] parts = data.split("\\|");

                    locationHolder.locationTextView.setText(parts[0]);
                    locationHolder.locationLastUpdateView.setText(parts[1]);

                } else {

                    locationHolder.locationTextView.setText(data);
                    locationHolder.locationLastUpdateView.setText("");
                }

                locationHolder.refreshButton.setOnClickListener(v -> {
                    if (refreshListener != null) {
                        refreshListener.onRefreshRequested();
                    }
                });

                break;


            case DashboardItemType.QUICK_ACCESS:

                QuickAccessViewHolder quickHolder = (QuickAccessViewHolder) holder;

                quickHolder.planButton.setOnClickListener(v -> {

                    Intent intent = new Intent(context, TripItineraryActivity.class);
                    intent.putExtra("tripId", tripId);

                    context.startActivity(intent);
                });

                quickHolder.sosButton.setOnClickListener(v ->
                        context.startActivity(new Intent(context, SosActivity.class))
                );

                quickHolder.alertButton.setOnClickListener(v -> {
                    if (alertClicked != null) {
                        alertClicked.onAlertReceived();
                    }
                });

                break;
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

