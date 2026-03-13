package com.harshitcreations.tourguard;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;

import androidx.recyclerview.widget.RecyclerView;

public class UserMapViewHolder extends RecyclerView.ViewHolder {

    public WebView mapView;

    private JSONArray pendingRoute;
    private boolean mapLoaded = false;

    // ⭐ Map ready listener
    public interface OnMapReadyListener {
        void onMapReady();
    }

    private OnMapReadyListener mapReadyListener;

    public void setOnMapReadyListener(OnMapReadyListener listener){
        this.mapReadyListener = listener;
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    public UserMapViewHolder(View itemView) {
        super(itemView);

        mapView = itemView.findViewById(R.id.mapView);

        WebSettings webSettings = mapView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Enable zoom
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        mapView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

                mapLoaded = true;
                Log.d("MAP_DEBUG","Map page loaded");

                // ⭐ Notify adapter that map is ready
                if(mapReadyListener != null){
                    mapReadyListener.onMapReady();
                }

                // ⭐ If route arrived earlier
                if(pendingRoute != null){
                    sendRoute(pendingRoute);
                    pendingRoute = null;
                }
            }
        });

        mapView.loadUrl("file:///android_asset/maps.html");

        // Prevent RecyclerView scroll conflict
        mapView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        });
    }

    public void updateLocation(double lat, double lng){

        if(mapView == null) return;

        String js = "updateUserLocation(" + lat + "," + lng + ")";

        mapView.post(() ->
                mapView.evaluateJavascript(js, null)
        );
    }

    public void drawExpectedRoute(JSONArray route){

        if(!mapLoaded){
            pendingRoute = route;
            Log.d("MAP_DEBUG","Route stored until map loads");
            return;
        }

        sendRoute(route);
    }

    private void sendRoute(JSONArray route){

        String js = "receiveRouteFromAndroid(" + route.toString() + ")";

        Log.d("ROUTE_DEBUG","JS -> " + js);

        mapView.post(() ->
                mapView.evaluateJavascript(js, null)
        );
    }
    public void updateEscortLocation(double lat,double lng){

        if(mapView == null) return;

        String js = "updateEscortLocation(" + lat + "," + lng + ")";

        mapView.post(() ->
                mapView.evaluateJavascript(js, null)
        );
    }
}