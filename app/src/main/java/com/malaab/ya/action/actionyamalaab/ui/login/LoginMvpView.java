package com.malaab.ya.action.actionyamalaab.ui.login;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface LoginMvpView extends MvpView {

    void onLanguageUpdatedSuccess();

    void onServerLoginSuccess(String userUId);

    void onUserExistInDB(User user);
    void onUserNotExistInDB();

    void onResetPassword(String message);
}
