package com.malaab.ya.action.actionyamalaab.ui.contactus;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface ContactUsMvpView extends MvpView {

    void onGetCurrentUser(User user);

    void onSendMessageSuccess();

}
