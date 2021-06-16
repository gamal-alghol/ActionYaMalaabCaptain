package com.malaab.ya.action.actionyamalaab.ui.account.bookings.past;

import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface PastBookingsMvpView extends MvpView {

    void onGetCurrentUser(User user);


    void onGetPastFullBookings(List<Booking> bookings);

    void onGetPastIndividualBookings(List<Booking> bookings);
}
