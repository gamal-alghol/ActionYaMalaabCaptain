package com.malaab.ya.action.actionyamalaab.ui.home;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.SideBarAction;
import com.malaab.ya.action.actionyamalaab.custom.CircularTextView;
import com.malaab.ya.action.actionyamalaab.custom.SpanningGridLayoutManager;
import com.malaab.ya.action.actionyamalaab.data.model.SidebarDrawerItem;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.events.OnEventSideBarClicked;
import com.malaab.ya.action.actionyamalaab.ui.account.AccountActivity;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.ui.messages.MessagesActivity;
import com.malaab.ya.action.actionyamalaab.ui.notifications.NotificationsActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.LocaleHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class SidebarDrawer extends Fragment {

    @BindView(R.id.fr_messages)
    public FrameLayout fr_messages;
    @BindView(R.id.img_messages)
    public CircleImageView img_messages;
    @BindView(R.id.txt_messagesCount)
    public CircularTextView txt_messagesCount;

    @BindView(R.id.img_profile)
    public CircleImageView img_profile;

    @BindView(R.id.fr_notifications)
    public FrameLayout fr_notifications;
    @BindView(R.id.img_notifications)
    public CircleImageView img_notifications;
    @BindView(R.id.txt_notificationsCount)
    public CircularTextView txt_notificationsCount;

    @BindView(R.id.txt_username)
    public TextView txt_username;
    @BindView(R.id.txt_userid)
    public TextView txt_userid;

    @BindView(R.id.cv_login)
    public CardView cv_login;

    @BindView(R.id.rv_menu)
    public RecyclerView rv_menu;

    @BindView(R.id.ln_logout)
    public LinearLayout ln_logout;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private SidebarDrawerAdapter adapter;
    CoordinatorLayout drawerLayout;
    private Class<?> txtTarget, lblTarget;
    private boolean isLoggedIn;

    private SidebarDrawerListener mListener;


    public SidebarDrawer() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.custom_sidebar_drawer, container, false);

        ButterKnife.bind(this, root);

        rv_menu.setHasFixedSize(true);
        rv_menu.setLayoutManager(new SpanningGridLayoutManager(getContext(), 2));
        adapter = new SidebarDrawerAdapter(getActivity(), getMenu(getActivity()));
        rv_menu.setAdapter(adapter);

//        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                //Called when a drawer's position changes.
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                //Called when a drawer has settled in a completely open state.
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Called when a drawer has settled in a completely closed state.
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
//            }
//        });

//        rv_menu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_menu, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                drawerListener.onDrawerItemSelected(view, position);
//                mDrawerLayout.closeDrawer(containerView);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        return root;
    }


    public void setDrawerListener(SidebarDrawerListener listener) {
        mListener = listener;
    }

    // preparing navigation drawer items
    public static List<SidebarDrawerItem> getMenu(Activity activity) {
        List<SidebarDrawerItem> data = new ArrayList<>();
        String selectedLanguage = LocaleHelper.getLanguage(activity);
        if (selectedLanguage.equalsIgnoreCase(Constants.LANGUAGE_ARABIC_CODE)) {
            data.add(new SidebarDrawerItem(SideBarAction.MY_PROFILE_AR, R.drawable.icon_profile,true, false));
            data.add(new SidebarDrawerItem(SideBarAction.MY_FAVOURITE_AR, R.drawable.icon_star_empty,true, false));
            data.add(new SidebarDrawerItem(SideBarAction.CHANGE_LOCATION_AR, R.drawable.icon_direction,true, false));
            data.add(new SidebarDrawerItem(SideBarAction.CONTACT_US_AR, R.drawable.icon_contact_us,true, false));
            data.add(new SidebarDrawerItem(SideBarAction.ABOUT_US_AR, R.drawable.icon_about_us,true, true));
            data.add(new SidebarDrawerItem(SideBarAction.APP_POLICY_AR, R.drawable.icon_policy,true, true));
//            data.add(new SidebarDrawerItem(SideBarAction.LOG_OUT_AR, R.drawable.icon_logout, false, false));

        } else {
            data.add(new SidebarDrawerItem(SideBarAction.MY_PROFILE, R.drawable.icon_profile, true, false));
            data.add(new SidebarDrawerItem(SideBarAction.MY_FAVOURITE, R.drawable.icon_star_empty, true, false));
            data.add(new SidebarDrawerItem(SideBarAction.CHANGE_LOCATION, R.drawable.icon_direction, true, false));
            data.add(new SidebarDrawerItem(SideBarAction.CONTACT_US, R.drawable.icon_contact_us, true, false));
            data.add(new SidebarDrawerItem(SideBarAction.ABOUT_US, R.drawable.icon_about_us, true, true));
            data.add(new SidebarDrawerItem(SideBarAction.APP_POLICY, R.drawable.icon_policy, true, true));
//            data.add(new SidebarDrawerItem(SideBarAction.LOG_OUT, R.drawable.icon_logout, false, false));
        }

//        data.add(new SidebarDrawerItem(SideBarAction.HELP, R.drawable.icon_menu_help, true, true, false));
//        data.add(new SidebarDrawerItem(SideBarAction.LOG_OUT, R.drawable.icon_menu_logout, true, true, false));

        return data;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
//        View containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }

                if (mListener != null) {
                    mListener.onDrawerOpened();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }

                if (mListener != null) {
                    mListener.onDrawerClosed();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.icon_menu);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public void updateWishlistNotificationCounter(int notificationCounter) {
        if (adapter != null) {

//            String selectedLanguage = LocaleHelper.getLanguage(getActivity());
//            if (selectedLanguage.equalsIgnoreCase(Constants.LANGUAGE_ARABIC_CODE)) {
//                adapter.updateItem(new SidebarDrawerItem(SideBarAction.WISH_LIST_BM, R.drawable.icon_menu_wishlist, true, true).showNotification(notificationCounter));
//
//            } else if (selectedLanguage.equalsIgnoreCase(Constants.LANGUAGE_CHINESE_CODE)) {
//                adapter.updateItem(new SidebarDrawerItem(SideBarAction.WISH_LIST_CH, R.drawable.icon_menu_wishlist, true, true).showNotification(notificationCounter));
//
//            } else {
//                adapter.updateItem(new SidebarDrawerItem(SideBarAction.WISH_LIST, R.drawable.icon_menu_wishlist, true, true).showNotification(notificationCounter));
//            }
//        }
        }
    }


    public void setUserAsGuest() {
        isLoggedIn = false;

        Glide.with(getActivity())
                .load("")
                .error(R.drawable.icon_notification_circle)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_notifications);

        updateMessagesCounter(0, true);
        updateNotificationsCounter(0, true);

        txt_username.setText("");
        txt_userid.setText("");

        cv_login.setVisibility(View.VISIBLE);
        txt_username.setVisibility(View.GONE);
        txt_userid.setVisibility(View.GONE);

        adapter.setIsItemActive(0, false);
        adapter.setIsItemActive(1, false);
        adapter.setIsItemActive(2, false);
        adapter.setIsItemActive(3, false);
        adapter.setIsItemActive(4, true);
        adapter.setIsItemActive(5, true);

        ln_logout.setVisibility(View.GONE);
    }

    public void setUserAsLoggedIn(User user) {
        isLoggedIn = true;

        updateMessagesCounter(0, false);
        updateNotificationsCounter(0, false);

        Glide.with(getActivity())
                .load(R.drawable.icon_message_circle)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_messages);

        Glide.with(getActivity())
                .load(user.profileImageUrl)
                .error(R.drawable.img_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.img_profile_default)
                .into(img_profile);

        Glide.with(getActivity())
                .load(R.drawable.icon_notification_circle)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_notifications);

        txt_username.setText(user.getUserFullName());
        txt_userid.setText(String.valueOf(user.appUserId));

        cv_login.setVisibility(View.GONE);
        txt_username.setVisibility(View.VISIBLE);
        txt_userid.setVisibility(View.VISIBLE);

        adapter.setIsItemActive(0, true);
        adapter.setIsItemActive(1, true);
        adapter.setIsItemActive(2, true);
        adapter.setIsItemActive(3, true);
        adapter.setIsItemActive(4, true);
        adapter.setIsItemActive(5, true);

        ln_logout.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.img_messages)
    public void openMessages() {
        ActivityUtils.goTo(getActivity(), MessagesActivity.class, false);
    }

    @OnClick(R.id.img_notifications)
    public void openNotifications() {
        ActivityUtils.goTo(getActivity(), NotificationsActivity.class, false);
    }

    @OnClick({R.id.img_profile, R.id.txt_username, R.id.txt_userid})
    public void openProfile() {
        if (isLoggedIn) {
            ActivityUtils.goTo(getActivity(), AccountActivity.class, false);
        }
    }

    @OnClick(R.id.cv_login)
    public void login() {
        ActivityUtils.goTo(getActivity(), LoginActivity.class, false);
    }

    @OnClick(R.id.ln_logout)
    public void logout() {
        EventBus.getDefault().post(new OnEventSideBarClicked(SideBarAction.LOG_OUT));
    }


    public void updateMessagesCounter(int count, boolean isGuest) {
        if (isGuest) {
            fr_messages.setVisibility(View.GONE);
        } else {
            fr_messages.setVisibility(View.VISIBLE);
        }

        if (count == 0) {
            txt_messagesCount.setText("");
            txt_messagesCount.setVisibility(View.GONE);
        } else {
            txt_messagesCount.setText(String.valueOf(count));
            txt_messagesCount.setVisibility(View.VISIBLE);
        }
    }

    public void updateNotificationsCounter(int count, boolean isGuest) {
        if (isGuest) {
            fr_notifications.setVisibility(View.GONE);
        } else {
            fr_notifications.setVisibility(View.VISIBLE);
        }

        if (count == 0) {
            txt_notificationsCount.setText("");
            txt_notificationsCount.setVisibility(View.GONE);
        } else {
            txt_notificationsCount.setText(String.valueOf(count));
            txt_notificationsCount.setVisibility(View.VISIBLE);
        }
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    public void openDrawer(final CoordinatorLayout drawerLayout, final View fragment, final ImageButton imageButton) {
        if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.openDrawer(Gravity.START);
            mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
            mDrawerLayout.setDrawerElevation(0);
            mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDrawerSlide(View drawer, float slideOffset) {
                    drawerLayout.setX(fragment.getWidth() * -slideOffset);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lp.height = drawer.getHeight() - (int) (drawer.getHeight() * slideOffset * 0.3f);
                    lp.topMargin = (drawer.getHeight() - lp.height) / 2;

                    drawerLayout.setLayoutParams(lp);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    imageButton.setImageResource(R.drawable.ic_baseline_dehaze_24);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    imageButton.setImageResource(R.drawable.icon_left_arrow);

                }
            });

        }
    }

    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }
}