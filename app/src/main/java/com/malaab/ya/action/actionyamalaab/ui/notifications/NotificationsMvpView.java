package com.malaab.ya.action.actionyamalaab.ui.notifications;

import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface NotificationsMvpView extends MvpView {

    void onGetNotifications(List<Notification> notifications);
}
