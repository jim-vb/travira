package com.harshitcreations.tourguard;

public class LocationResponse {

    public boolean deviation;
    public int safety_score;
    public String safety_level;

    public Anomaly anomaly;

    public static class Anomaly{
        public double score;
        public boolean is_anomaly;
    }
}