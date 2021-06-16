package com.malaab.ya.action.actionyamalaab.data.model;

import com.malaab.ya.action.actionyamalaab.annotations.SideBarAction;


public class SidebarDrawerItem {

    public @SideBarAction
    String title;
    public int icon;

    public boolean isVisible;   // To control SignOut Button
    public boolean isActive;   // To control User Logged in
    public int mNotificationCounter;


    public SidebarDrawerItem(@SideBarAction String title, int icon, boolean isVisible, boolean isActive) {
        this.title = title;
        this.icon = icon;
        this.isVisible = isVisible;
        this.isActive = isActive;
    }


    public SidebarDrawerItem showNotification(int notificationCounter) {
        mNotificationCounter = notificationCounter;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SidebarDrawerItem that = (SidebarDrawerItem) o;

        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}