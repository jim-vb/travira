package com.harshitcreations.tourguard;

public class LocationRequest {
    private String phoneNumber;
    private double latitude;
    private double longitude;

    public LocationRequest(String phoneNumber, double latitude, double longitude) {
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getters & setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
