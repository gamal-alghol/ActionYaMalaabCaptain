package com.malaab.ya.action.actionyamalaab.utils.facebookSignIn;

public interface FacebookResponse {

    void onFbSignInSuccess();

    void onFbProfileReceived(FacebookUser facebookUser);

    void onFbSignInFail();

    void onFBSignOut();
}
