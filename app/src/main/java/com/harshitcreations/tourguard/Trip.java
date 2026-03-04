package com.harshitcreations.tourguard;

import java.util.ArrayList;

public class Trip {

    private String tripName;
    private String startingLocation;
    private String endingLocation;
    private String startingDandT;
    private String endingDandT;
    private String intermediateStops;
    private String transportation;
    private String hotelName;
    private String contactName;
    private String emergencyContact;

    public Trip(String tripName, String startingLocation, String endingLocation, String startingDandT, String endingDandT, String intermediateStops, String transportation, String hotelName, String contactName, String emergencyContact) {
        this.tripName = tripName;
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
        this.startingDandT = startingDandT;
        this.endingDandT = endingDandT;
        this.intermediateStops = intermediateStops;
        this.transportation = transportation;
        this.hotelName = hotelName;
        this.contactName = contactName;
        this.emergencyContact = emergencyContact;
    }
}
