package com.malaab.ya.action.actionyamalaab.ui.home.individual;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface IndividualMvpView extends MvpView {

    void onUserAsGuest();

    void onGetCurrentUser(User user);

    void onGetIndividualPlayground(List<Playground> playgrounds);
}
