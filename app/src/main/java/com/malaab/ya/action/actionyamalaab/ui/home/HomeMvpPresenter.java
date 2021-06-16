package com.malaab.ya.action.actionyamalaab.ui.home;

import android.content.Intent;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface HomeMvpPresenter<V extends HomeMvpView> extends MvpPresenter<V> {

    void processIntent(Intent intent);


    void getCurrentUserInfoLocal();

    void getCurrentUserInfoOnline(String userUid);


    void isUserAuthenticated();


    void updateCounters();


    void isDeviceRegisteredForNotifications(User user);

    void registerForFirebaseNotifications(User user);


    void isPlaygroundInFavouriteList(Playground playground);

    void addPlaygroundToFavouriteList(Playground playground);

    void removePlaygroundFromFavouriteList(Playground playground);


    void pushFutsalCourts();

    void signOut();
}
