package com.malaab.ya.action.actionyamalaab.ui.home;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface HomeMvpView extends MvpView {

    void onShowPlayground(String playgroundId, boolean isIndividual, String bookingId);

    void onUserAsGuest();
    void onUserLoggedIn(User user);

    void onUserAuthenticationSuccess(String userUid);

    void onRegisterDeviceForNotification(User user);

    void onUpdateCounters(int notificationsCount, int messagesCount);

    void onPlaygroundInFavouriteList(boolean isFavourite);
    void onAddPlaygroundToFavouriteList(boolean isSuccess);
    void onRemovePlaygroundFromFavouriteList(boolean isSuccess);
}
