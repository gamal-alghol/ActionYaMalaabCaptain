package com.malaab.ya.action.actionyamalaab.ui.messages.details;

import com.malaab.ya.action.actionyamalaab.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface MessagesDetailsMvpView extends MvpView {

    void onGetCurrentUser(User user);

    void onGetMessageDetails(Message message);

    void onGetUserDetails(User user);
}
