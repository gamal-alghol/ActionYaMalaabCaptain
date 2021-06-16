package com.malaab.ya.action.actionyamalaab.ui.account.settings;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountMvpView;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface SettingsMvpPresenter<V extends SettingsMvpView> extends MvpPresenter<V> {

    void getAppSettings();

    void changeLanguage(String appLanguage);

    void enableNotifications(boolean isEnabled);
    void enableOffersNotifications(boolean isEnabled);
    void enableMessagesNotifications(boolean isEnabled);

}
