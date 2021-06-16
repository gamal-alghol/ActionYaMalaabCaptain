package com.malaab.ya.action.actionyamalaab.ui.messages;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface MessagesMvpPresenter<V extends MessagesMvpView> extends MvpPresenter<V> {

    void getMessages();

    void resetMessagesCounters();
}
