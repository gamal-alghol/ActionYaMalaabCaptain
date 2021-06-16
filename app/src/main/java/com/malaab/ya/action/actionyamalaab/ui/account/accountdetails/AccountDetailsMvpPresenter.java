package com.malaab.ya.action.actionyamalaab.ui.account.accountdetails;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;

import java.io.File;


@PerActivity
public interface AccountDetailsMvpPresenter<V extends AccountDetailsMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void updateCurrentUser(File image, String fName, String lName, String phone, String email, String age, String city);
}
