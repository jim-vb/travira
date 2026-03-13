package com.harshitcreations.tourguard;

public class MonitoringRequest {

    private String tripId;
    private String userId;

    public MonitoringRequest(String tripId, String userId) {
        this.tripId = tripId;
        this.userId = userId;
    }
}