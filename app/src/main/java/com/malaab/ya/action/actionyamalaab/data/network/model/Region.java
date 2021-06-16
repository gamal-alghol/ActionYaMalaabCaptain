package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Objects;

@IgnoreExtraProperties
public class Region {

    public String uid;

    public List<City> cities;


    public Region() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Region(String uid, List<City> cities) {
        this.uid = uid;
        this.cities = cities;
    }


    @Exclude
    @Override
    public String toString() {
        return "Region{" +
                "uid='" + uid + '\'' +
                ", cities=" + cities +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(uid, region.uid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uid);
    }
}
