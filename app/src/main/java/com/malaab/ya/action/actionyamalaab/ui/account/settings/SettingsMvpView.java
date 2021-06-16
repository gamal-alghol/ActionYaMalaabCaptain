package com.malaab.ya.action.actionyamalaab.ui.account.settings;

import com.malaab.ya.action.actionyamalaab.data.model.AppSettings;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;


public interface SettingsMvpView extends MvpView {

    void onGetAppSettings(AppSettings appSettings);


    void onChangeAppLanguage(boolean isSuccess);


    void onEnableNotifications(boolean isSuccess);

    void onEnableOffersNotifications(boolean isSuccess);

    void onEnableMessagesNotifications(boolean isSuccess);
}
