package com.malaab.ya.action.actionyamalaab.ui.home.full;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface FullMvpPresenter<V extends FullMvpView> extends MvpPresenter<V> {

    void getCurrentUser();

    void getPlaygroundsForGuest();

    void getPlaygrounds(String userCity, double latitude, double longitude);
}
