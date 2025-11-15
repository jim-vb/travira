package com.harshitcreations.tourguard;

public class EmergencyRequest {
    private String phone;
    private double lat;
    private double lng;
    private String message;

    public EmergencyRequest(String phone, double lat, double lng, String message) {
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.message = message;
    }

    // Optional: getters & setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
