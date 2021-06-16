package com.malaab.ya.action.actionyamalaab.data.model;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class DurationListItem {

    public String name;
    public int value;

    @Exclude
    public boolean isSelected;


    public DurationListItem() {
        // Default constructor required for calls to DataSnapshot.getValue(DurationListItem.class)
    }


    public DurationListItem(String name, int value, boolean isSelected) {
        this.name = name;
        this.value = value;
        this.isSelected = isSelected;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DurationListItem that = (DurationListItem) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }
}
