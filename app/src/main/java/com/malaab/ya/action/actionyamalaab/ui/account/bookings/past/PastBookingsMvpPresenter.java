package com.malaab.ya.action.actionyamalaab.ui.account.bookings.past;

import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;

import java.util.List;


@PerActivity
public interface PastBookingsMvpPresenter<V extends PastBookingsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();


    void getPastFullBookings(String userUid);

    void getPastIndividualBookings(String userUid, List<Booking> bookings);

}
