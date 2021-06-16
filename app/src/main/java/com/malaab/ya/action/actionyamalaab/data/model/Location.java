package com.malaab.ya.action.actionyamalaab.data.model;

public class Location {

    public String title;
    public String snippet;

    public double latitude;
    public double longitude;

    public String profileImageUrl;


    public Location(String title, String snippet, double latitude, double longitude, String profileImageUrl) {
        this.title = title;
        this.snippet = snippet;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profileImageUrl = profileImageUrl;
    }
}
