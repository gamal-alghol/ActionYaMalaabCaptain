package com.malaab.ya.action.actionyamalaab.data;

import android.content.Context;

import com.google.firebase.auth.PhoneAuthProvider;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.model.AppSettings;
import com.malaab.ya.action.actionyamalaab.data.model.Counter;
import com.malaab.ya.action.actionyamalaab.data.model.FirebaseSettings;
import com.malaab.ya.action.actionyamalaab.data.model.Notifications;
import com.malaab.ya.action.actionyamalaab.data.model.NotificationsSettings;
import com.malaab.ya.action.actionyamalaab.data.model.UserFavouritePlaygrounds;
import com.malaab.ya.action.actionyamalaab.data.model.UsersFavouritePlaygrounds;
import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.data.prefs.PreferencesHelper;
import com.malaab.ya.action.actionyamalaab.di.ApplicationContext;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.LocaleHelper;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class AppDataManager implements DataManager {

    private final PreferencesHelper mPreferencesHelper;

    @Inject
    @ApplicationContext
    Context mContext;


    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }


    @Override
    public void updateAppSettings(String languageCode, boolean isNotificationEnabled, boolean isOffersEnabled, boolean isMessageEnabled) {
        AppSettings mAppSettings = mPreferencesHelper.getAppSettings();

        if (mAppSettings == null) {
            mAppSettings = new AppSettings(Constants.LANGUAGE_ARABIC_CODE);
            mAppSettings.isFirstLaunch = true;
            mAppSettings.isNotificationsEnabled = true;
            mAppSettings.isOffersNotificationsEnabled = true;
            mAppSettings.isMessagesNotificationsEnabled = true;

        } else {
            mAppSettings.isFirstLaunch = false;
            if (!StringUtils.isEmpty(languageCode)) {
                mAppSettings.appLanguage = languageCode;
            }

            mAppSettings.isNotificationsEnabled = isNotificationEnabled;
            mAppSettings.isOffersNotificationsEnabled = isOffersEnabled;
            mAppSettings.isMessagesNotificationsEnabled = isMessageEnabled;
        }

        mPreferencesHelper.setAppSettings(mAppSettings);

        LocaleHelper.setLocale(mContext, mAppSettings.appLanguage);
    }

    @Override
    public void updateFirebaseSettings(String phoneVerificationId, PhoneAuthProvider.ForceResendingToken resendingToken) {
        FirebaseSettings mFirebaseSettings = mPreferencesHelper.getFirebaseSettings();
        if (mFirebaseSettings == null) {
            mFirebaseSettings = new FirebaseSettings(phoneVerificationId, resendingToken);
        } else {
            mFirebaseSettings.phoneVerificationId = phoneVerificationId;
            mFirebaseSettings.resendingToken = resendingToken;
        }

        setFirebaseSettings(mFirebaseSettings);
    }


    @Override
    public void updateCurrentUserInfo(String userUid, long appUserId,
                                      String email, String password, String fName, String lName, String dob, int age, String mobileNo,
                                      long modifyDate, long createdDate,
                                      String imageUrl,
                                      String referralCode, String referredBy,
                                      String role,
                                      String fcmToken,
                                      String city, String direction, String region,
                                      double latitude, double longitude,
                                      int loggedInMode, boolean isActive) {

        User user = new User(new User.UserBuilder(email, password, loggedInMode)
                .withOptionalUID(userUid)
                .withOptionalAppUserId(appUserId)
                .withOptionalIsActive(isActive)

                .withOptionalFirstName(fName)
                .withOptionalLastName(lName)
                .withOptionalDob(dob)
                .withOptionalAge(age)
                .withOptionalMobileNo(mobileNo)

                .withOptionalModifyDate(modifyDate)
                .withOptionalCreatedDate(createdDate)

                .withOptionalImageUrl(imageUrl)
                .withOptionalReferralCode(referralCode)
                .withOptionalReferredBy(referredBy)

                .withOptionalFCMToken(fcmToken)

                .withOptionalCity(city)
                .withOptionalDirection(direction)
                .withOptionalRegion(region)

                .withOptionalLatitude(latitude)
                .withOptionalLongitude(longitude)

                .withOptionalRole(role)
        );

        setCurrentUser(user);
    }


    @Override
    public void setUserAsLoggedOut() {
        User user = mPreferencesHelper.getCurrentUser();
        if (user != null) {
            updateCurrentUserInfo(null, 0,
                    null, null, null, null, null, 0, null,
                    0, 0,
                    null,
                    null, null,
                    null,
                    null,
                    null, null, null,
                    0, 0,
                    LoginMode.LOGGED_IN_MODE_LOGGED_OUT, false);
        }
    }


    //region SHARED_PREFERENCES
    @Override
    public void setAppSettings(AppSettings appSettings) {
        mPreferencesHelper.setAppSettings(appSettings);
    }

    @Override
    public AppSettings getAppSettings() {
        return mPreferencesHelper.getAppSettings();
    }


    @Override
    public void setFirebaseSettings(FirebaseSettings firebaseSettings) {
        mPreferencesHelper.setFirebaseSettings(firebaseSettings);
    }

    @Override
    public FirebaseSettings getFirebaseSettings() {
        return mPreferencesHelper.getFirebaseSettings();
    }


    @Override
    public void setCounters(Counter counters) {
        mPreferencesHelper.setCounters(counters);
    }

    @Override
    public void resetNotificationsCounters() {
        mPreferencesHelper.resetNotificationsCounters();
    }

    @Override
    public void resetMessagesCounters() {
        mPreferencesHelper.resetMessagesCounters();
    }

    @Override
    public Counter getCounters() {
        return mPreferencesHelper.getCounters();
    }


    @Override
    public void setNotificationsSettings(String firebaseToken, boolean isRegistered) {
        mPreferencesHelper.setNotificationsSettings(firebaseToken, isRegistered);
    }

    @Override
    public void updateNotificationsSettings(String firebaseToken, boolean isRegistered) {
        mPreferencesHelper.updateNotificationsSettings(firebaseToken, isRegistered);
    }

    @Override
    public NotificationsSettings getNotificationsSettings() {
        return mPreferencesHelper.getNotificationsSettings();
    }


    @Override
    public void addNotificationContent(Notification notification) {
        mPreferencesHelper.addNotificationContent(notification);
    }

    @Override
    public void removeNotificationContent(Notification notification) {
        mPreferencesHelper.removeNotificationContent(notification);
    }

    @Override
    public Notifications getNotifications() {
        return mPreferencesHelper.getNotifications();
    }


    @Override
    public void setCurrentUser(User user) {
        mPreferencesHelper.setCurrentUser(user);
    }

    @Override
    public User getCurrentUser() {
        return mPreferencesHelper.getCurrentUser();
    }

    @Override
    public boolean isPlaygroundInFavouriteList(String userUid, Playground playground) {
        return mPreferencesHelper.isPlaygroundInFavouriteList(userUid, playground);
    }


    @Override
    public void addPlaygroundToFavouriteList(String userUid, Playground playground) {
        mPreferencesHelper.addPlaygroundToFavouriteList(userUid, playground);
    }

    @Override
    public void removePlaygroundTFromFavouriteList(String userUid, Playground playground) {
        mPreferencesHelper.removePlaygroundTFromFavouriteList(userUid, playground);
    }

    @Override
    public UserFavouritePlaygrounds getUserFavouritePlaygrounds(String userUid) {
        return mPreferencesHelper.getUserFavouritePlaygrounds(userUid);
    }

    @Override
    public UsersFavouritePlaygrounds getUsersFavouritePlaygrounds() {
        return mPreferencesHelper.getUsersFavouritePlaygrounds();
    }

    //endregion


    //region API

    //endregion
}