package com.malaab.ya.action.actionyamalaab.utils.firebase;

public interface IFirebaseTracking {

    void LogEventScreen(String eventName);

    void LogEventClick(String eventName);

    void LogEvent(String category, String eventName);

    void LogException(String exception);

    void setUserProperty(String experimentName, String experimentVariant);
}
