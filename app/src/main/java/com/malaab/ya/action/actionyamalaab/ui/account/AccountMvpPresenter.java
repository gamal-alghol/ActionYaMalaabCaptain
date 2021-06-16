package com.malaab.ya.action.actionyamalaab.ui.account;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.contactus.ContactUsMvpView;


@PerActivity
public interface AccountMvpPresenter<V extends AccountMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

}
