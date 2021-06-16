package com.malaab.ya.action.actionyamalaab.ui.account.settings;

import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.model.AppSettings;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class SettingsPresenter<V extends SettingsMvpView>
        extends BasePresenter<V>
        implements SettingsMvpPresenter<V> {

    @Inject
    public SettingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Settings Screen");
    }


    @Override
    public void getAppSettings() {
        AppSettings appSettings = getDataManager().getAppSettings();

        if (appSettings == null) {
            appSettings = new AppSettings(Constants.LANGUAGE_ARABIC_CODE);
            appSettings.isFirstLaunch = true;
            appSettings.isNotificationsEnabled = true;
            appSettings.isOffersNotificationsEnabled = true;
            appSettings.isMessagesNotificationsEnabled = true;

            getDataManager().setAppSettings(appSettings);
        }

        getMvpView().onGetAppSettings(appSettings);
    }


    @Override
    public void changeLanguage(String appLanguage) {
        AppSettings appSettings = getDataManager().getAppSettings();

        getDataManager().updateAppSettings(appLanguage, appSettings.isNotificationsEnabled, appSettings.isOffersNotificationsEnabled, appSettings.isMessagesNotificationsEnabled);

        getMvpView().onChangeAppLanguage(true);
    }


    @Override
    public void enableNotifications(boolean isEnabled) {
        AppSettings appSettings = getDataManager().getAppSettings();

        getDataManager().updateAppSettings(appSettings.appLanguage, isEnabled, appSettings.isOffersNotificationsEnabled, appSettings.isMessagesNotificationsEnabled);

        getMvpView().onEnableNotifications(true);
    }

    @Override
    public void enableOffersNotifications(boolean isEnabled) {
        AppSettings appSettings = getDataManager().getAppSettings();

        getDataManager().updateAppSettings(appSettings.appLanguage, appSettings.isNotificationsEnabled, isEnabled, appSettings.isMessagesNotificationsEnabled);

        getMvpView().onEnableOffersNotifications(true);
    }

    @Override
    public void enableMessagesNotifications(boolean isEnabled) {
        AppSettings appSettings = getDataManager().getAppSettings();

        getDataManager().updateAppSettings(appSettings.appLanguage, appSettings.isNotificationsEnabled, appSettings.isOffersNotificationsEnabled, isEnabled);

        getMvpView().onEnableMessagesNotifications(true);
    }
}
