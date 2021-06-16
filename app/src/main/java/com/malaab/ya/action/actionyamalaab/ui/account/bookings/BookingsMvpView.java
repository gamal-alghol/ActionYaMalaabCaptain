package com.malaab.ya.action.actionyamalaab.ui.account.bookings;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface BookingsMvpView extends MvpView {

    void onGetCurrentUser(User user);
}
