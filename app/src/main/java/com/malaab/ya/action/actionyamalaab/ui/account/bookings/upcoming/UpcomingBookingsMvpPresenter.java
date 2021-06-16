package com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming;

import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;

import java.util.List;


@PerActivity
public interface UpcomingBookingsMvpPresenter<V extends UpcomingBookingsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();


    void getUpcomingFullBookings(String userUid);

    void getUpcomingIndividualBookings(String userUid, List<Booking> bookings);


    void cancelFullBooking(Booking booking, User user, boolean hasFine);

    void cancelIndividualBooking(Booking booking, User user);


    void createFine(Booking booking, User user,int x);
}
