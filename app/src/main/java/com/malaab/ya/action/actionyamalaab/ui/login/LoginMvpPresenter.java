package com.malaab.ya.action.actionyamalaab.ui.login;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    void updateLanguage(String languageCode);


    void doServerLogin(String username, String password);

    void isUserExistInDB(String userUId);


    void forgotPassword(String username, String email);
}
