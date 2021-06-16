package com.malaab.ya.action.actionyamalaab.data.model;

import com.google.firebase.auth.PhoneAuthProvider;

public class FirebaseSettings {

    public String phoneVerificationId;
    public PhoneAuthProvider.ForceResendingToken resendingToken;


    public FirebaseSettings(String phoneVerificationId, PhoneAuthProvider.ForceResendingToken resendingToken) {
        this.phoneVerificationId = phoneVerificationId;
        this.resendingToken = resendingToken;
    }
}
