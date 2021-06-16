package com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming;

import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface UpcomingBookingsMvpView extends MvpView {

    void onGetCurrentUser(User user);


    void onGetUpcomingFullBookings(List<Booking> bookings);

    void onGetUpcomingIndividualBookings(List<Booking> bookings);


    void onBookingCancelledSuccess(Booking booking);


    void onFineCreateSuccess(Booking booking);
}
