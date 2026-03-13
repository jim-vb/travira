package com.harshitcreations.tourguard;

public class LocationUpdateRequest {

    public String user_id;
    public int trip_id;
    public double lat;
    public double lng;
    public double speed;
    public String timestamp;

    public LocationUpdateRequest(String user_id, int trip_id,
                                 double lat, double lng,
                                 double speed, String timestamp) {

        this.user_id = user_id;
        this.trip_id = trip_id;
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
        this.timestamp = timestamp;
    }
}