package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Objects;

@IgnoreExtraProperties
public class PlaygroundSearch {

    public String playgroundId;

    public String name;
    public String address_region;
    public String address_city;
    public String address_direction;

    public double latitude;
    public double longitude;

    public List<String> images;

    public boolean isIndividuals;
    public String bookingId;
    public boolean hasAgeYoung;
    public boolean hasAgeMiddle;
    public boolean hasAgeOld;
    public long timeStart;
    public long timeEnd;

    public float price;  // To be used in searching response
    public int size;  // To be used in searching response

    public boolean isActive;  // To be used in Admin for deactivating playground


    public PlaygroundSearch() {
        // Default constructor required for calls to DataSnapshot.getValue(Playground.class)
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaygroundSearch that = (PlaygroundSearch) o;
        return Objects.equals(playgroundId, that.playgroundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playgroundId);
    }
}