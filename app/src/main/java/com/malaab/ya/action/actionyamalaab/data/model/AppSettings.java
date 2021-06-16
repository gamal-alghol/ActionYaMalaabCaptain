package com.malaab.ya.action.actionyamalaab.data.model;

public class AppSettings {

    public boolean isFirstLaunch;

    public String appLanguage;

    public boolean isNotificationsEnabled;
    public boolean isOffersNotificationsEnabled;
    public boolean isMessagesNotificationsEnabled;


    public AppSettings(String appLanguage) {
        this.appLanguage = appLanguage;
    }

}
