package com.malaab.ya.action.actionyamalaab.ui.notifications;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.messages.MessagesActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotificationsActivity extends BaseActivity implements NotificationsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    NotificationsMvpPresenter<NotificationsMvpView> mPresenter;

    private boolean isLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_notifications);

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(this));
//        rv_items.addItemDecoration(new SimpleDividerItemDecoration(this));
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void showLoading() {
        pBar_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar_loading.setVisibility(View.GONE);
    }


    @Override
    public void onGetNotifications(List<Notification> notifications) {
        NotificationsAdapter adapter = new NotificationsAdapter(notifications);
        adapter.setOnItemClickListener(new NotificationsAdapter.OnNotificationClicked() {
            @Override
            public void setOnNotificationClicked(Notification notification, int position) {
                Class<?> to = HomeActivity.class;

                switch (notification.type) {
                    case NotificationType.CAPTAIN_NEW:
                    case NotificationType.BOOKING_INDIVIDUAL_NEW:
                    case NotificationType.OWNER_PLAYGROUND_NEW:
                        to = HomeActivity.class;
                        break;

                    case NotificationType.BOOKING_ADMIN_APPROVED:
                    case NotificationType.BOOKING_ADMIN_REJECTED:
                    case NotificationType.BOOKING_INDIVIDUAL_COMPLETED:
                    case NotificationType.BOOKING_INDIVIDUAL_NOT_COMPLETED:
                    case NotificationType.FINE_ISSUED:
                        to = BookingsActivity.class;
                        break;

                    case NotificationType.MESSAGE_CONTACT_US:
                        to = MessagesActivity.class;
                        break;
                }

                ActivityUtils.goTo(NotificationsActivity.this, to, false);

//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Constants.INTENT_KEY_MESSAGE, message);
//
//                ActivityUtils.goTo(NotificationsActivity.this, MessagesDetailsActivity.class, false, bundle);
            }
        });

        rv_items.setAdapter(adapter);
        rv_items.setVisibility(View.VISIBLE);

        mPresenter.resetMessagesCounters();
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void OnEventItemClicked(OnEventSort event) {
//        if (TheActivityManager.getInstance().getCurrentActivity() instanceof MessagesActivity) {
//
//            if (adapter == null) {
//                return;
//            }
//
//            if (event.getSortOption() == SortOption.RECENT) {
//                adapter.setItems(ListUtils.sortUsersList(users, SortOption.RECENT));
//
//            } else if (event.getSortOption() == SortOption.DEACTIVATED) {
//                List<User> list = ListUtils.sortUsersList(users, SortOption.DEACTIVATED);
//                adapter.setItems(list);
//
//            } else if (event.getSortOption() == SortOption.NO_OF_BOOKINGS) {
//                adapter.setItems(ListUtils.sortUsersList(users, SortOption.NO_OF_BOOKINGS));
//            }
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isLoaded) {
            isLoaded = true;
            mPresenter.getNotifications();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
