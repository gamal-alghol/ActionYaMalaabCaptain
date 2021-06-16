package com.malaab.ya.action.actionyamalaab.ui.account.bookings;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface BookingsMvpPresenter<V extends BookingsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();
}
