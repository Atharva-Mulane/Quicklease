package com.quicklease.quicklease_backend.dto;

public class ProfileResponse {
    private String fullName;
    private String email;
    private Boolean emailVerified;
    private String phone;
    private String address;
    private String city;

    public ProfileResponse(String fullName, String email, Boolean emailVerified,
                           String phone, String address, String city) {
        this.fullName = fullName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.phone = phone;
        this.address = address;
        this.city = city;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Boolean getEmailVerified() { return emailVerified; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
}