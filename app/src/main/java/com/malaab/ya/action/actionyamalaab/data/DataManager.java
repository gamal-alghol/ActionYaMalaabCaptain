package com.malaab.ya.action.actionyamalaab.data;

import com.google.firebase.auth.PhoneAuthProvider;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.prefs.PreferencesHelper;


public interface DataManager extends PreferencesHelper {

    void updateAppSettings(String languageCode, boolean isNotificationEnabled, boolean isOffersEnabled, boolean isMessageEnabled);

    void updateFirebaseSettings(String phoneVerificationId, PhoneAuthProvider.ForceResendingToken resendingToken);

    void updateCurrentUserInfo(String userUid, long appUserId,
                               String email, String password, String fName, String lName, String dob, int age, String mobileNo,
                               long modifyDate, long createdDate,
                               String imageUrl,
                               String referralCode, String referredBy,
                               String role,
                               String fcmToken,
                               String city, String direction, String region,
                               double latitude, double longitude,
                               @LoginMode int loggedInMode, boolean isActive);

    void setUserAsLoggedOut();

}
