package com.quicklease.quicklease_backend.dto;

public class ProfileRequest {
    private String phone;
    private String address;
    private String city;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}