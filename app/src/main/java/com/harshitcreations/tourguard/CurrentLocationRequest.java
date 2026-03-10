package com.harshitcreations.tourguard;

public class CurrentLocationRequest {

    private double latitude;
    private double longitude;
    private String city;
    private String state;
    private boolean isDanger;

    public CurrentLocationRequest(double latitude, double longitude,
                                  String city, String state,
                                  boolean isDanger) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.state = state;
        this.isDanger = isDanger;
    }
}