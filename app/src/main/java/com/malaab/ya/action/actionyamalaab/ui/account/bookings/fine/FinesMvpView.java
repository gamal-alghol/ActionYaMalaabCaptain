package com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine;

import com.malaab.ya.action.actionyamalaab.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface FinesMvpView extends MvpView {

    void onGetCurrentUser(User user);

    void onGetMyFines(List<Fine> fines);
}
