package com.harshitcreations.tourguard;

public class DashboardItem {
    private int type;
    private String data; // For dynamic content like greeting text

    public DashboardItem(int type, String data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}

