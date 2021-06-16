package com.malaab.ya.action.actionyamalaab.ui.notifications;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface NotificationsMvpPresenter<V extends NotificationsMvpView> extends MvpPresenter<V> {

    void getNotifications();

    void resetMessagesCounters();
}
