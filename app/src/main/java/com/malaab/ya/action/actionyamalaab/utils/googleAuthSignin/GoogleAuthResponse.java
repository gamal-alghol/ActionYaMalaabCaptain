package com.malaab.ya.action.actionyamalaab.utils.googleAuthSignin;


public interface GoogleAuthResponse {

    void onGoogleAuthSignIn(GoogleAuthUser user);

    void onGoogleAuthSignInFailed();

    void onGoogleAuthSignOut(boolean isSuccess);
}
