package com.malaab.ya.action.actionyamalaab.utils.firebase;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import dagger.internal.Preconditions;


public class FirebaseTracking implements IFirebaseTracking {

    private final String CATEGORY_SCREEN = "screen";
    private final String CATEGORY_CLICK = "click";

    private final FirebaseAnalytics firebaseAnalytics;


    public FirebaseTracking(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = Preconditions.checkNotNull(firebaseAnalytics);
    }


    @Override
    public void LogEventScreen(String eventName) {
        LogEvent(CATEGORY_SCREEN, eventName);
    }

    @Override
    public void LogEventClick(String eventName) {
        LogEvent(CATEGORY_CLICK, eventName);
    }

    @Override
    public void LogEvent(String category, String eventName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventName);

//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        synchronized (firebaseAnalytics) {
            firebaseAnalytics.logEvent(category, bundle);
        }
    }

    @Override
    public void LogException(String exception) {
        Crashlytics.log(exception);
    }

    @Override
    public void setUserProperty(String experimentName, String experimentVariant) {
        synchronized (firebaseAnalytics) {
            firebaseAnalytics.setUserProperty(experimentName, experimentVariant);
        }
    }
}