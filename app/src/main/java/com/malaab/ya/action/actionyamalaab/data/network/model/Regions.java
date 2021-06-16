package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Regions {

    public String uid;
    public List<Region> regions;


    public Regions() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
}
