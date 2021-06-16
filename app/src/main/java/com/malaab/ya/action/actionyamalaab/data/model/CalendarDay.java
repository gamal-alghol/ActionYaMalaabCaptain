package com.malaab.ya.action.actionyamalaab.data.model;

import java.util.Calendar;

public class CalendarDay {

    public Calendar date;

    public boolean isSelectedByUser;


    public CalendarDay(Calendar date, boolean isSelectedByUser) {
        this.date = date;
        this.isSelectedByUser = isSelectedByUser;
    }
}
