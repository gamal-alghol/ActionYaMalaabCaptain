package com.malaab.ya.action.actionyamalaab.ui.loginbyphone;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface LoginByPhoneMvpView extends MvpView {

    void onCodeSent(String phoneVerificationId, PhoneAuthProvider.ForceResendingToken resendToken);
    void onGetVerificationCodeFailed();
    void onVerificationCompleted(PhoneAuthCredential credential);

    void onUserSignedInSuccess(String userUId);
    void onUserSignedInFailed();

    void onUserIsActive(User user);
    void onUserNotActive(String message);

    void onCreateNewUser(String userUId);
    void onUserUniqueIdGenerated(String userUId, long appUserId);

    void onUserAddedToFirebaseSuccess(User user);
    void onUploadProfileImageSuccess(User user);
}

