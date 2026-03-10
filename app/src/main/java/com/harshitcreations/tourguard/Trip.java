package com.harshitcreations.tourguard;

import com.google.gson.annotations.SerializedName;

public class Trip {

    @SerializedName("_id")
    private String id;

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

    // Empty constructor (required for Retrofit/Gson)
    public Trip() {
    }

    // Constructor used when creating/updating a trip
    public Trip(String tripName, String startingLocation, String endingLocation,
                String startingDandT, String endingDandT, String intermediateStops,
                String transportation, String hotelName, String contactName,
                String emergencyContact) {

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

    // ---------- GETTERS ----------

    public String getId() {
        return id;
    }

    public String getTripName() {
        return tripName;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public String getEndingLocation() {
        return endingLocation;
    }

    public String getStartingDandT() {
        return startingDandT;
    }

    public String getEndingDandT() {
        return endingDandT;
    }

    public String getIntermediateStops() {
        return intermediateStops;
    }

    public String getTransportation() {
        return transportation;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }
}