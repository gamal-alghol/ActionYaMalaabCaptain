package com.malaab.ya.action.actionyamalaab.ui.loginbyphone;

import com.google.firebase.auth.PhoneAuthCredential;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;

import java.io.File;


@PerActivity
public interface LoginByPhoneMvpPresenter<V extends LoginByPhoneMvpView> extends MvpPresenter<V> {

    void getVerificationCode(String countryCode, String phoneNumber);

    void verifyPhoneNumberWithCode(String verificationId, String code);

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential);


    void isUserExistInDB(String userUId);

    void generateUserUniqueId(String userUId);


    void addUserToFirebaseDatabase(String userUId, long appUserId,
                                   final String fName, final String lName, final String email,
                                   final String countryCode, final String phone,
                                   final String password,
                                   final String referredBy);

    void uploadImage(User user, File profileImage);

}
