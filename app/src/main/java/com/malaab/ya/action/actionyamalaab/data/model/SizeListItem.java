package com.malaab.ya.action.actionyamalaab.data.model;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class SizeListItem {

    public String name;
    public int value;

    @Exclude
    public boolean isSelected;


    public SizeListItem() {
        // Default constructor required for calls to DataSnapshot.getValue(SizeListItem.class)
    }


    public SizeListItem(String name, int value, boolean isSelected) {
        this.name = name;
        this.value = value;
        this.isSelected = isSelected;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizeListItem item = (SizeListItem) o;
        return Objects.equals(value, item.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value);
    }
}
