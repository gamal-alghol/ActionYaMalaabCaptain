package com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine;

import  com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface FinesMvpPresenter<V extends FinesMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void getMyFines(String userUid);
}
