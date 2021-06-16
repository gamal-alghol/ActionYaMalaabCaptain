package com.malaab.ya.action.actionyamalaab.ui.home.individual;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface IndividualMvpPresenter<V extends IndividualMvpView> extends MvpPresenter<V> {

    void getCurrentUser();

    void getPlaygroundsForGuest();

    void getIndividualPlayground(String userCity, double latitude, double longitude);

//    void listenToFutsalCourtsUpdates();

//    void removeListeners();
}
