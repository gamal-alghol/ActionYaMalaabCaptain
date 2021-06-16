package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class MetaData {

    public String appUserId;


    public MetaData() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "MetaData{" +
                "appUserId='" + appUserId + '\'' +
                '}';
    }
}
