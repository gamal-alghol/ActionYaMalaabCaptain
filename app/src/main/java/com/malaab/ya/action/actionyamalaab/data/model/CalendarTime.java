package com.malaab.ya.action.actionyamalaab.data.model;

import java.util.Objects;

public class CalendarTime {

    public long timeStart;
    public long timeEnd;
    public int duration;
    public int image;

    public boolean isSelectedByUser;
    public boolean isAvailable;


    public CalendarTime() {
        // Default constructor required for calls to DataSnapshot.getValue(CalendarTime.class)
    }

    public CalendarTime(long timeStart, long timeEnd, int duration, int image, boolean isSelectedByUser, boolean isAvailable) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.duration = duration;
        this.image = image;
        this.isSelectedByUser = isSelectedByUser;
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarTime that = (CalendarTime) o;
        return Objects.equals(timeStart, that.timeStart) &&
                Objects.equals(timeEnd, that.timeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStart, timeEnd);
    }
}
