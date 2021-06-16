package com.malaab.ya.action.actionyamalaab.di.component;

import com.malaab.ya.action.actionyamalaab.SplashActivityGif;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.di.module.ActivityModule;
import com.malaab.ya.action.actionyamalaab.ui.SplashActivity;
import com.malaab.ya.action.actionyamalaab.ui.aboutus.AboutUsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.accountdetails.AccountDetailsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine.FinesFragment;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.past.PastBookingsFragment;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming.UpcomingBookingsFragment;
import com.malaab.ya.action.actionyamalaab.ui.account.rewards.RewardsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.settings.SettingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.contactus.ContactUsActivity;
import com.malaab.ya.action.actionyamalaab.ui.favourite.FavouriteActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.full.FullFragment;
import com.malaab.ya.action.actionyamalaab.ui.home.individual.IndividualFragment;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.ui.loginbyphone.LoginByPhoneActivity;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.ui.messages.MessagesActivity;
import com.malaab.ya.action.actionyamalaab.ui.messages.details.MessagesDetailsActivity;
import com.malaab.ya.action.actionyamalaab.ui.notifications.NotificationsActivity;
import com.malaab.ya.action.actionyamalaab.ui.register.RegisterActivity;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchActivity;
import com.malaab.ya.action.actionyamalaab.ui.search.search_age.SearchAgeFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.search_price.SearchPriceFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.search_region.SearchRegionFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.search_size.SearchSizeFragment;
import com.malaab.ya.action.actionyamalaab.ui.termsandconditions.TermsConditionsActivity;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity activity);

    void inject(SplashActivityGif activity);


    void inject(LoginByPhoneActivity activity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);


    void inject(HomeActivity activity);

    void inject(FullFragment fragment);

    void inject(IndividualFragment fragment);


    void inject(FavouriteActivity activity);

    void inject(MapActivity activity);

    void inject(AboutUsActivity activity);

    void inject(ContactUsActivity activity);

    void inject(TermsConditionsActivity activity);


    void inject(AccountActivity activity);

    void inject(AccountDetailsActivity activity);

    void inject(SettingsActivity activity);

    void inject(RewardsActivity activity);

    void inject(BookingsActivity activity);

    void inject(UpcomingBookingsFragment fragment);

    void inject(PastBookingsFragment fragment);

    void inject(FinesFragment fragment);


    void inject(SearchActivity activity);

    void inject(SearchPriceFragment fragment);

    void inject(SearchRegionFragment fragment);

    void inject(SearchAgeFragment fragment);

    void inject(SearchSizeFragment fragment);


    void inject(MessagesActivity activity);

    void inject(MessagesDetailsActivity activity);


    void inject(NotificationsActivity activity);

}
