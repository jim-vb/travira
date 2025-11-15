package com.harshitcreations.tourguard;

public class UserRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private String aadhaarNumber;

    public UserRequest(String fullName, String email, String phoneNumber, String password, String aadhaarNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.aadhaarNumber = aadhaarNumber;
    }
}
