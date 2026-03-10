package com.harshitcreations.tourguard;

public class UserLocation {
    private double latitude;
    private double longitude;
    private String city;
    private String state;

    public UserLocation(double latitude, double longitude, String city, String state) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.state = state;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCity() { return city; }
    public String getState() { return state; }
}
