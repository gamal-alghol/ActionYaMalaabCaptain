package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class BookingPlayground {

    public String playgroundId;
    public String ownerId;

    public String name;
    public String address_city;
    public String address_direction;
    public String address_region;

    public List<String> images;


    public BookingPlayground() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "BookingPlayground{" +
                "playgroundId='" + playgroundId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", address_city='" + address_city + '\'' +
                ", address_direction='" + address_direction + '\'' +
                ", address_region='" + address_region + '\'' +
                ", images=" + images +
                '}';
    }
}
