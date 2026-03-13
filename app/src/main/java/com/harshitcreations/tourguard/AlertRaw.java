package com.harshitcreations.tourguard;

public class AlertRaw {

    public int id;
    public String title;
    public String message;
    public String severity;
    public String type;
    public String created_at;

    public Metadata metadata;

    public static class Metadata {

        public Double lat;
        public Double lng;

    }

}