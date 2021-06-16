package com.malaab.ya.action.actionyamalaab.ui.messages;

import com.malaab.ya.action.actionyamalaab.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface MessagesMvpView extends MvpView {

    void onGetMessages(List<Message> messages);
}
