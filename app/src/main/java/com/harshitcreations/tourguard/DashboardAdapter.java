package com.harshitcreations.tourguard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DashboardItem> items;
    private Context context;
    private SafetyScoreViewHolder safetyHolder;
    private OnLocationRefreshListener refreshListener;
    private OnAlertClicked alertClicked;
    private GreetingViewHolder.OnSettingsClicked settingsClicked;

    private String tripId;

    // ⭐ Reference to map holder
    private UserMapViewHolder mapViewHolder;
    private List<List<Double>> cachedRoute;

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

            case DashboardItemType.USER_MAP:
                view = LayoutInflater.from(context).inflate(R.layout.user_map_item, parent, false);
                return new UserMapViewHolder(view);

            case DashboardItemType.SAFETY_SCORE:
                view = LayoutInflater.from(context).inflate(R.layout.safety_score_item, parent, false);
                return new SafetyScoreViewHolder(view);

            case DashboardItemType.QUICK_ACCESS:
                view = LayoutInflater.from(context).inflate(R.layout.quick_access_item, parent, false);
                return new QuickAccessViewHolder(view);

            case DashboardItemType.LOCATION:
                view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
                return new LocationViewHolder(view);

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
                String userName = item.getData();
                greetingHolder.bind(userName);

                break;

            case DashboardItemType.USER_MAP:
                mapViewHolder = (UserMapViewHolder) holder;

                mapViewHolder.setOnMapReadyListener(() -> {

                    Log.d("ROUTE_DEBUG","Map ready, sending cached route");

                    sendRouteToMap();
                });

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

            case DashboardItemType.SAFETY_SCORE:

                safetyHolder = (SafetyScoreViewHolder) holder;

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

                quickHolder.viewIdButton.setOnClickListener(v ->
                        context.startActivity(new Intent(context, DigitalIdActivity.class))
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

    // ⭐ Method to update map location
    public void updateMapLocation(double lat, double lng) {

        if (mapViewHolder != null) {
            mapViewHolder.updateLocation(lat, lng);
        }
    }
    public void updateEscortLocation(double lat, double lng){

        if(mapViewHolder != null){
            mapViewHolder.updateEscortLocation(lat, lng);
        }
    }

    public void drawExpectedRoute(List<List<Double>> route){
        Log.d("ROUTE_DEBUG","Adapter received route: "+route.size());

        cachedRoute = route;

        if(mapViewHolder != null){
            sendRouteToMap();
        }
    }

    private void sendRouteToMap(){

        Log.d("ROUTE_DEBUG","Sending route to map");

        if(mapViewHolder == null || cachedRoute == null) return;

        try {

            JSONArray array = new JSONArray();

            for(List<Double> point : cachedRoute){

                JSONArray coord = new JSONArray();
                coord.put(point.get(0)); // lng
                coord.put(point.get(1)); // lat

                array.put(coord);
            }

            mapViewHolder.drawExpectedRoute(array);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface OnLocationRefreshListener {
        void onRefreshRequested();
    }
    public void updateSafetyScore(int score,String level){

        if(safetyHolder != null){
            safetyHolder.updateScore(score,level);
        }
    }

    public interface OnAlertClicked {
        void onAlertReceived();
    }
}