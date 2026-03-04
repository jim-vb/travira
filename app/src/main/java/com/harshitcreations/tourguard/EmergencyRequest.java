package com.harshitcreations.tourguard;

public class EmergencyRequest {
    private double latitude;
    private double longitude;
    private String currentLocation;
    private String message;

    public EmergencyRequest(double latitude, double longitude, String currentLocation, String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentLocation = currentLocation;
        this.message = message;
    }

    // Optional: getters & setters

    public double getLat() { return latitude; }
    public void setLat(double lat) { this.latitude = lat; }

    public double getLng() { return longitude; }
    public void setLng(double lng) { this.longitude = lng; }

    public String getCity() { return currentLocation; }
    public void setCity(String city) { this.currentLocation = city; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
