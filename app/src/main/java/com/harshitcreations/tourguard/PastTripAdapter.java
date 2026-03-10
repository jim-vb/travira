package com.harshitcreations.tourguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.ViewHolder> {

    List<Trip> tripList;

    public PastTripAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tripName, tripRoute, tripDates;

        public ViewHolder(View itemView) {
            super(itemView);

            tripName = itemView.findViewById(R.id.tripName);
            tripRoute = itemView.findViewById(R.id.tripRoute);
            tripDates = itemView.findViewById(R.id.tripDates);
        }
    }

    @NonNull
    @Override
    public PastTripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastTripAdapter.ViewHolder holder, int position) {

        Trip trip = tripList.get(position);

        holder.tripName.setText(trip.getTripName());
        holder.tripRoute.setText(trip.getStartingLocation() + " → " + trip.getEndingLocation());
        holder.tripDates.setText(trip.getStartingDandT() + " → " + trip.getEndingDandT());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}