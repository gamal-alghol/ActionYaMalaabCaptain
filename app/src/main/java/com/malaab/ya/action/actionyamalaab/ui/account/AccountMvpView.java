package com.malaab.ya.action.actionyamalaab.ui.account;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface AccountMvpView extends MvpView {

    void onGetCurrentUser(User user);

}
