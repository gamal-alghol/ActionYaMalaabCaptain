package com.malaab.ya.action.actionyamalaab.di.module;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.AppDataManager;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.prefs.AppPreferencesHelper;
import com.malaab.ya.action.actionyamalaab.data.prefs.PreferencesHelper;
import com.malaab.ya.action.actionyamalaab.di.ApplicationContext;
import com.malaab.ya.action.actionyamalaab.di.DatabaseInfo;
import com.malaab.ya.action.actionyamalaab.di.PreferenceInfo;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.firebase.FirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


@Module
public class ApplicationModule {

    private final Application mApplication;


    public ApplicationModule(Application application) {
        mApplication = application;
    }


    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


    @Provides
    FirebaseRemoteConfig providesFirebaseRemoteConfig() {
        return FirebaseRemoteConfig.getInstance();
    }


    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return Constants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return Constants.PREF_NAME;
    }


    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }


    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }


    @Provides
    @Singleton
    IFirebaseTracking provideFirebaseTracking(@ApplicationContext Context context) {
        int playServicesStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (playServicesStatus == ConnectionResult.SUCCESS) {
            return new FirebaseTracking(FirebaseAnalytics.getInstance(context));
        } else {
            return new IFirebaseTracking() {

                @Override
                public void LogEventScreen(String eventName) {

                }

                @Override
                public void LogEventClick(String eventName) {

                }

                @Override
                public void LogEvent(String category, String eventName) {

                }

                @Override
                public void LogException(String exception) {

                }

                @Override
                public void setUserProperty(String experimentName, String experimentVariant) {

                }
            };
        }
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open-sans/OpenSans-Regular.ttf")
//                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

}
