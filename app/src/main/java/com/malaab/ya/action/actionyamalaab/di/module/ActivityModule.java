package com.malaab.ya.action.actionyamalaab.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.malaab.ya.action.actionyamalaab.custom.DialogConfirmation;
import com.malaab.ya.action.actionyamalaab.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.di.ActivityContext;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.accountdetails.AccountDetailsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.accountdetails.AccountDetailsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.accountdetails.AccountDetailsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine.FinesMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine.FinesMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine.FinesPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.past.PastBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.past.PastBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.past.PastBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming.UpcomingBookingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming.UpcomingBookingsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming.UpcomingBookingsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.settings.SettingsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.account.settings.SettingsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.account.settings.SettingsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.contactus.ContactUsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.contactus.ContactUsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.contactus.ContactUsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.favourite.FavouriteMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.favourite.FavouriteMvpView;
import com.malaab.ya.action.actionyamalaab.ui.favourite.FavouritePresenter;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeMvpView;
import com.malaab.ya.action.actionyamalaab.ui.home.HomePresenter;
import com.malaab.ya.action.actionyamalaab.ui.home.full.FullMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.home.full.FullMvpView;
import com.malaab.ya.action.actionyamalaab.ui.home.full.FullPresenter;
import com.malaab.ya.action.actionyamalaab.ui.home.individual.IndividualMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.home.individual.IndividualMvpView;
import com.malaab.ya.action.actionyamalaab.ui.home.individual.IndividualPresenter;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginMvpView;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginPresenter;
import com.malaab.ya.action.actionyamalaab.ui.loginbyphone.LoginByPhoneMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.loginbyphone.LoginByPhoneMvpView;
import com.malaab.ya.action.actionyamalaab.ui.loginbyphone.LoginByPhonePresenter;
import com.malaab.ya.action.actionyamalaab.ui.map.MapMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.map.MapMvpView;
import com.malaab.ya.action.actionyamalaab.ui.map.MapPresenter;
import com.malaab.ya.action.actionyamalaab.ui.messages.MessagesMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.messages.MessagesMvpView;
import com.malaab.ya.action.actionyamalaab.ui.messages.MessagesPresenter;
import com.malaab.ya.action.actionyamalaab.ui.messages.details.MessagesDetailsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.messages.details.MessagesDetailsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.messages.details.MessagesDetailsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.notifications.NotificationsMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.notifications.NotificationsMvpView;
import com.malaab.ya.action.actionyamalaab.ui.notifications.NotificationsPresenter;
import com.malaab.ya.action.actionyamalaab.ui.register.RegisterMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.register.RegisterMvpView;
import com.malaab.ya.action.actionyamalaab.ui.register.RegisterPresenter;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchMvpView;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchPresenter;
import com.malaab.ya.action.actionyamalaab.ui.search.search_region.SearchRegionMvpPresenter;
import com.malaab.ya.action.actionyamalaab.ui.search.search_region.SearchRegionMvpView;
import com.malaab.ya.action.actionyamalaab.ui.search.search_region.SearchRegionPresenter;
import com.malaab.ya.action.actionyamalaab.utils.Validator;
import com.malaab.ya.action.actionyamalaab.utils.rx.AppSchedulerProvider;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;


@Module
public class ActivityModule {

    private AppCompatActivity mActivity;


    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }


    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }


    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @PerActivity
    Validator provideValidator(AppCompatActivity activity) {
        return new Validator(activity);
    }


    @Provides
    @PerActivity
    DialogConfirmation provideDialogConfirmation(AppCompatActivity activity) {
        return new DialogConfirmation().with(activity);
    }

    @Provides
    @PerActivity
    DialogList provideDialogList(AppCompatActivity activity) {
        return new DialogList().with(activity);
    }


    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginByPhoneMvpPresenter<LoginByPhoneMvpView> provideLoginByPhonePresenter(LoginByPhonePresenter<LoginByPhoneMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(RegisterPresenter<RegisterMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    HomeMvpPresenter<HomeMvpView> provideHomePresenter(HomePresenter<HomeMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    FullMvpPresenter<FullMvpView> provideFullPresenter(FullPresenter<FullMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IndividualMvpPresenter<IndividualMvpView> provideIndividualPresenter(IndividualPresenter<IndividualMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    FavouriteMvpPresenter<FavouriteMvpView> provideFavouritePresenter(FavouritePresenter<FavouriteMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MapMvpPresenter<MapMvpView> provideMapPresenter(MapPresenter<MapMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    ContactUsMvpPresenter<ContactUsMvpView> provideContactUsPresenter(ContactUsPresenter<ContactUsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    AccountMvpPresenter<AccountMvpView> provideAccountPresenter(AccountPresenter<AccountMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    AccountDetailsMvpPresenter<AccountDetailsMvpView> provideAccountDetailsPresenter(AccountDetailsPresenter<AccountDetailsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SettingsMvpPresenter<SettingsMvpView> provideSettingsPresenter(SettingsPresenter<SettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BookingsMvpPresenter<BookingsMvpView> provideBookingsPresenter(BookingsPresenter<BookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UpcomingBookingsMvpPresenter<UpcomingBookingsMvpView> provideUpcomingBookingsPresenter(UpcomingBookingsPresenter<UpcomingBookingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PastBookingsMvpPresenter<PastBookingsMvpView> providePastBookingsPresenter(PastBookingsPresenter<PastBookingsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    SearchMvpPresenter<SearchMvpView> provideSearchPresenter(SearchPresenter<SearchMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SearchRegionMvpPresenter<SearchRegionMvpView> provideSearchRegionPresenter(SearchRegionPresenter<SearchRegionMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MessagesMvpPresenter<MessagesMvpView> provideMessagesPresenter(MessagesPresenter<MessagesMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MessagesDetailsMvpPresenter<MessagesDetailsMvpView> provideMessagesDetailsPresenter(MessagesDetailsPresenter<MessagesDetailsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    NotificationsMvpPresenter<NotificationsMvpView> provideNotificationsPresenter(NotificationsPresenter<NotificationsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    FinesMvpPresenter<FinesMvpView> provideFinesPresenter(FinesPresenter<FinesMvpView> presenter) {
        return presenter;
    }
}
