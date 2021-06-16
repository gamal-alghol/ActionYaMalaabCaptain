package com.malaab.ya.action.actionyamalaab.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.PermissionsCodes;
import com.malaab.ya.action.actionyamalaab.annotations.SideBarAction;
import com.malaab.ya.action.actionyamalaab.annotations.ToolbarOption;
import com.malaab.ya.action.actionyamalaab.custom.BSheetBook;
import com.malaab.ya.action.actionyamalaab.custom.CustomToolbar;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventSendFirebaseTokenToServer;
import com.malaab.ya.action.actionyamalaab.events.OnEventShowPlayground;
import com.malaab.ya.action.actionyamalaab.events.OnEventSideBarClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventToolbarItemClicked;
import com.malaab.ya.action.actionyamalaab.ui.aboutus.AboutUsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountActivity;
import com.malaab.ya.action.actionyamalaab.ui.adapter.FragmentsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.contactus.ContactUsActivity;
import com.malaab.ya.action.actionyamalaab.ui.favourite.FavouriteActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.full.FullFragment;
import com.malaab.ya.action.actionyamalaab.ui.home.individual.IndividualFragment;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchActivity;
import com.malaab.ya.action.actionyamalaab.ui.termsandconditions.TermsConditionsActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.PermissionsUtils;
import com.yanzhenjie.permission.Permission;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends BaseActivity implements HomeMvpView, SidebarDrawerListener {

    @BindView(R.id.mAppBar)
    public AppBarLayout mAppBar;
    @BindView(R.id.mCustomToolbar)
    public CustomToolbar mCustomToolbar;

    @BindView(R.id.tl_container)
    public TabLayout tl_container;
    @BindView(R.id.vp_content)
    public ViewPager vp_content;

    @Inject
    HomeMvpPresenter<HomeMvpView> mPresenter;

    private FragmentsAdapter mAdapter;
    private SidebarDrawer mSidebarDrawer;

    //    private BSheetFragmentBook mBSheetFragmentBook;
    private BSheetBook mBSheetBook;
    private User mUser;
    private boolean isLoaded = false;
    private boolean doubleBackToExit = false;
    private int activity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityUtils.finishInstance(LoginActivity.class);
        ActivityUtils.setAsLanding(this);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

//        if (getIntent() != null && getIntent().hasExtra(Constants.INTENT_KEY_ISGUEST)) {
//            isGuest = getIntent().getBooleanExtra(Constants.INTENT_KEY_ISGUEST, false);
//        }

        mPresenter.processIntent(getIntent());
    }

    @Override
    protected void initUI() {
        setSupportActionBar(mCustomToolbar);

        mAdapter = new FragmentsAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new FullFragment(), getString(R.string.full));
        mAdapter.addFragment(new IndividualFragment(), getString(R.string.individual));

        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
       activity=i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
});
        vp_content.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View view, float position) {

                if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
                    view.setAlpha(0.0F);
                    view.setVisibility(View.GONE);
                } else if( position == 0.0F ) {     // [0]
                    view.setAlpha(1.0F);
                    view.setVisibility(View.VISIBLE);
                } else {

                    // Position is between [-1,1]
                    view.setAlpha(1.0F - Math.abs(position));
                    view.setTranslationX(-position * (view.getWidth() / 2));
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        vp_content.setOffscreenPageLimit(mAdapter.getCount() - 1);
        vp_content.setAdapter(mAdapter);
        tl_container.setupWithViewPager(vp_content);
        mSidebarDrawer = (SidebarDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mSidebarDrawer.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mCustomToolbar);
        mSidebarDrawer.setDrawerListener(this);

//        mBSheetFragmentBook = new BSheetFragmentBook();

        mBSheetBook = new BSheetBook();
        mBSheetBook.attachAndInit(this);
//        mBSheetBook.setOnBottomSheetListener(this);
    }


//    @OnClick(R.id.btn_login)
//    public void loginServer() {
//        txt_message.setVisibility(View.GONE);
//    }


    @Override
    public void onDrawerOpened() {
        mCustomToolbar.stopNotifications();
    }

    @Override
    public void onDrawerClosed() {

    }


    @Override
    public void onShowPlayground(String playgroundId, boolean isIndividual, String bookingId) {
        if (isIndividual) {
            vp_content.setCurrentItem(1, true);
        } else {
            vp_content.setCurrentItem(0, true);
        }

        EventBus.getDefault().postSticky(new OnEventShowPlayground(playgroundId, isIndividual, bookingId));
    }


    @Override
    public void onUserAsGuest() {
        mCustomToolbar.setUserAsGuest();
        mSidebarDrawer.setUserAsGuest();

        if (!isLoaded) {
            isLoaded = true;
        }
    }

    @Override
    public void onUserLoggedIn(User user) {
        mUser = user;

        mCustomToolbar.setUserAsLoggedIn();
        mSidebarDrawer.setUserAsLoggedIn(user);

        mPresenter.updateCounters();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.isUserAuthenticated();
            mPresenter.isDeviceRegisteredForNotifications(user);
        }
    }


    @Override
    public void onUserAuthenticationSuccess(String userUid) {
        mPresenter.getCurrentUserInfoOnline(userUid);
    }


    @Override
    public void onRegisterDeviceForNotification(User user) {
        mPresenter.registerForFirebaseNotifications(user);
    }


    @Override
    public void onUpdateCounters(int notificationsCount, int messagesCount) {
       // mCustomToolbar.showNotifications(notificationsCount + messagesCount);

     //   mSidebarDrawer.updateNotificationsCounter(notificationsCount, mUser == null);
      //  mSidebarDrawer.updateMessagesCounter(messagesCount, mUser == null);
    }


    @Override
    public void onPlaygroundInFavouriteList(boolean isFavourite) {
        mBSheetBook.setFavourite(isFavourite);
    }

    @Override
    public void onAddPlaygroundToFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(true);
    }

    @Override
    public void onRemovePlaygroundFromFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(false);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof HomeActivity) {

            Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof Playground) {
                if (action == ItemAction.DETAILS) {
                    mBSheetBook.show((Playground) item, mUser);
                    mPresenter.isPlaygroundInFavouriteList((Playground) item);

                    //                mBSheetFragmentBook.setPlayground((Playground) item);
//                mBSheetFragmentBook.show(getSupportFragmentManager(), "BSheetFragmentShare");

                } else if (action == ItemAction.LOCATION) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.INTENT_KEY_PLAYGROUND, (Playground) item);
                    ActivityUtils.goTo(HomeActivity.this, MapActivity.class, false, bundle);

                } else if (action == ItemAction.FAVOURITE_ADD) {
                    mPresenter.addPlaygroundToFavouriteList((Playground) item);

                } else if (action == ItemAction.FAVOURITE_REMOVE) {
                    mPresenter.removePlaygroundFromFavouriteList((Playground) item);
                }

            } else if (item instanceof CalendarDay && action == ItemAction.PICK) {
                mBSheetBook.setDaySelected(((CalendarDay) item), event.getPosition());

            } else if (item instanceof CalendarTime && action == ItemAction.PICK) {
                mBSheetBook.setTimeSelected((CalendarTime) event.getItem(), event.getPosition());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventToolbarItemClicked(OnEventToolbarItemClicked event) {
        switch (event.getAction()) {
            case ToolbarOption.MENU:

                mSidebarDrawer.openDrawer((CoordinatorLayout) findViewById(R.id.cl_container)
                        ,(View)findViewById(R.id.fragment_navigation_drawer),(ImageButton) findViewById(R.id.btn_menu));


                break;
            case ToolbarOption.SEARCH:
                ActivityUtils.goTo(HomeActivity.this, SearchActivity.class, false);
                break;
            case ToolbarOption.MAP:
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    PermissionsUtils.requestPermission(this, PermissionsCodes.LOCATION, Permission.LOCATION);
                }else{
                ActivityUtils.goTo(HomeActivity.this, MapActivity.class, activity);
                }
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventSideBarClicked(OnEventSideBarClicked event) {
        switch (event.getAction()) {
            case SideBarAction.MY_PROFILE:
            case SideBarAction.MY_PROFILE_AR:
                ActivityUtils.goTo(HomeActivity.this, AccountActivity.class, false);
                break;

            case SideBarAction.MY_FAVOURITE:
            case SideBarAction.MY_FAVOURITE_AR:
                ActivityUtils.goTo(HomeActivity.this, FavouriteActivity.class, false);
                break;

            case SideBarAction.CHANGE_LOCATION:
            case SideBarAction.CHANGE_LOCATION_AR:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.INTENT_KEY_IS_FIRST_TIME, true);
                ActivityUtils.goTo(HomeActivity.this, MapActivity.class, false, bundle);
                break;

            case SideBarAction.CONTACT_US:
            case SideBarAction.CONTACT_US_AR:
                ActivityUtils.goTo(HomeActivity.this, ContactUsActivity.class, false);
                break;

            case SideBarAction.ABOUT_US:
            case SideBarAction.ABOUT_US_AR:
                ActivityUtils.goTo(HomeActivity.this, AboutUsActivity.class, false);
                break;

            case SideBarAction.APP_POLICY:
            case SideBarAction.APP_POLICY_AR:
                ActivityUtils.goTo(HomeActivity.this, TermsConditionsActivity.class, false);
                break;

            case SideBarAction.LOG_OUT:
            case SideBarAction.LOG_OUT_AR:
                mPresenter.signOut();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventSendFirebaseTokenToServer(OnEventSendFirebaseTokenToServer event) {
        mPresenter.registerForFirebaseNotifications(mUser);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mPresenter.getCurrentUserInfoLocal();
//        mPresenter.isUserAuthenticated(isGuest);

//        AppLogger.w("FCM \n" + FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onPause() {
        super.onPause();

//        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        if (mSidebarDrawer.isDrawerOpen()) {
            mSidebarDrawer.closeDrawer();
            return;
        }

        if (mBSheetBook.onBackPressed()) {
            return;
        }

        if (vp_content.getCurrentItem() > 0) {
            vp_content.setCurrentItem(0, true);
            return;
        }

//        // TODO: 03-Apr-18 Fix This
//        if (mAdapter.getItem(vp_content.getCurrentItem()) instanceof FullFragment) {
//            if (((FullFragment) mAdapter.getItem(vp_content.getCurrentItem())).onBackPressed()) {
//                vp_content.setCurrentItem(0, true);
//                return;
//            }
//        }

        if (doubleBackToExit) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExit = true;
        showMessage(getString(R.string.msg_exit));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, Constants.EXIT_DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mSidebarDrawer.closeDrawer();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mCustomToolbar.onDestroy();
        mBSheetBook.onDetach();
        mPresenter.onDetach();
        super.onDestroy();
    }

}
