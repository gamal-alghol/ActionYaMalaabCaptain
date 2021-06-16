package com.malaab.ya.action.actionyamalaab.ui.contactus;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface ContactUsMvpPresenter<V extends ContactUsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void sendMessage(String fromUserUid, String fromUserAppId, String fromUsername, String fromUserEmail, String fromUserPhone, String fromUserProfileImage, String message);

}
