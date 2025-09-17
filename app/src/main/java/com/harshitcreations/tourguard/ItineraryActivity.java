package com.harshitcreations.tourguard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {

    private RecyclerView rvStops;
    private List<GeoPoint> stops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_itinerary);

        rvStops = findViewById(R.id.rvStops);
        rvStops.setLayoutManager(new LinearLayoutManager(this));

        // Stops
        stops = new ArrayList<>();
        GeoPoint currentLocation = new GeoPoint(28.41252, 77.31977); // ACEM Faridabad / current location
        GeoPoint delhi = new GeoPoint(28.6139, 77.2090);
        GeoPoint mumbai = new GeoPoint(19.0760, 72.8777);
        GeoPoint goa = new GeoPoint(15.2993, 74.1240);

        stops.add(currentLocation);
        stops.add(delhi);
        stops.add(mumbai);
        stops.add(goa);

        StopsAdapter adapter = new StopsAdapter(this, stops);
        rvStops.setAdapter(adapter);
    }

    // RecyclerView Adapter
    private static class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.StopViewHolder> {

        private List<GeoPoint> stops;
        private Context context;

        public StopsAdapter(Context context, List<GeoPoint> stops) {
            this.context = context;
            this.stops = stops;
        }

        @NonNull
        @Override
        public StopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_stop, parent, false);
            return new StopViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StopViewHolder holder, int position) {
            GeoPoint stop = stops.get(position);
            holder.tvStopName.setText(position == 0 ? "Current Location" : "Stop " + position);

            holder.itemView.setOnClickListener(v -> {
                if (holder.mapView.getVisibility() == View.GONE) {
                    holder.mapView.setVisibility(View.VISIBLE);
                    holder.btnGo.setVisibility(View.VISIBLE);

                    // Setup OSMDroid map
                    holder.mapView.setTileSource(TileSourceFactory.MAPNIK);
                    holder.mapView.setMultiTouchControls(true);
                    holder.mapView.getController().setZoom(10.0);
                    holder.mapView.getController().setCenter(stop);

                    Marker marker = new Marker(holder.mapView);
                    marker.setPosition(stop);
                    marker.setTitle(position == 0 ? "Current Location" : "Stop " + position);
                    holder.mapView.getOverlays().clear();
                    holder.mapView.getOverlays().add(marker);
                    holder.mapView.invalidate();
                } else {
                    holder.mapView.setVisibility(View.GONE);
                    holder.btnGo.setVisibility(View.GONE);
                }
            });

            holder.btnGo.setOnClickListener(v -> {
                // Open Google Maps navigation
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + stop.getLatitude() + "," + stop.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            });
        }

        @Override
        public int getItemCount() {
            return stops.size();
        }

        static class StopViewHolder extends RecyclerView.ViewHolder {
            TextView tvStopName;
            MapView mapView;
            Button btnGo;

            public StopViewHolder(@NonNull View itemView) {
                super(itemView);
                tvStopName = itemView.findViewById(R.id.tvStopName);
                mapView = itemView.findViewById(R.id.stopMap);
                btnGo = itemView.findViewById(R.id.btnGo);
            }
        }
    }
}
