package com.malaab.ya.action.actionyamalaab.ui.messages.details;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface MessagesDetailsMvpPresenter<V extends MessagesDetailsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void getMessageDetails(String uid);

    void getUserDetails(String uid);
}
