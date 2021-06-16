package com.malaab.ya.action.actionyamalaab.ui.account.accountdetails;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface AccountDetailsMvpView extends MvpView {

    void onGetCurrentUser(User user);

    void onUpdateCurrentUserSuccess();

}
