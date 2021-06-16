package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class City {

    public String uid;

    public String id;
    public String name;


    public City() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "Region{" +
                "uid='" + uid + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City region = (City) o;
        return Objects.equals(uid, region.uid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uid);
    }
}
