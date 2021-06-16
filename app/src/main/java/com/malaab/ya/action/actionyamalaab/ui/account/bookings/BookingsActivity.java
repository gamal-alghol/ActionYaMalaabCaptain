package com.malaab.ya.action.actionyamalaab.ui.account.bookings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.custom.BSheetBook;
import com.malaab.ya.action.actionyamalaab.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
    import com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine.FinesFragment;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.past.PastBookingsFragment;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming.UpcomingBookingsFragment;
import com.malaab.ya.action.actionyamalaab.ui.adapter.FragmentsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.favourite.FavouriteActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookingsActivity extends BaseActivity implements BookingsMvpView, OnBottomSheetListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;
    @BindView(R.id.tl_container)
    public TabLayout tl_container;
    @BindView(R.id.vp_content)
    public ViewPager vp_content;
    @BindView(R.id.fab_makeBooking)
    public FloatingActionButton fab_makeBooking;
    @Inject
    BookingsMvpPresenter<BookingsMvpView> mPresenter;
    private BSheetBook mBSheetBook;
    private User mUser;
    float dX, dY;

    int lastAction;

    private short touchTimeFactor = 200;
    private long touchDownTime = 0L;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);





                fab_makeBooking.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                dX = view.getX() - event.getRawX();
                                dY = view.getY() - event.getRawY();

                                touchDownTime = System.currentTimeMillis();
                                lastAction=MotionEvent.ACTION_DOWN;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                    view.animate()
                                            .x(event.getRawX() + dX)
                                            .y(event.getRawY() + dY)
                                            .setDuration(0)
                                            .start();
                                break;
case MotionEvent.ACTION_UP:
    if (System.currentTimeMillis() - touchDownTime < touchTimeFactor){
      ActivityUtils.toLanding();
    }
    break;
                            default:
                                return false;
                        }
                        return true;

            }
        });

    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_my_bookings);
        FragmentsAdapter mAdapter = new FragmentsAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new UpcomingBookingsFragment(), getString(R.string.upcoming_bookings));
        mAdapter.addFragment(new PastBookingsFragment(), getString(R.string.past_bookings));
        mAdapter.addFragment(new FinesFragment(), getString(R.string.fines));

        vp_content.setOffscreenPageLimit(mAdapter.getCount() - 1);
        vp_content.setAdapter(mAdapter);

        tl_container.setupWithViewPager(vp_content);

        mBSheetBook = new BSheetBook();
        mBSheetBook.attachAndInit(this);
        mBSheetBook.setOnBottomSheetListener(this);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.fab_makeBooking)
    public void makeBooking() {
       // ActivityUtils.toLanding();
    }

    @Override
    public void onGetCurrentUser(User user) {
        mUser = user;
    }


    @Override
    public void onSlide(float slideOffset) {

    }

    @Override
    public void onBottomSheetExpanded() {
        fab_makeBooking.hide();
    }

    @Override
    public void onBottomSheetCollapsed(String... args) {
        fab_makeBooking.show();
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof BookingsActivity) {

            Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof Booking) {
                if (action == ItemAction.PLAYGROUND_VIEW) {
                    mBSheetBook.showUserBookingInfo((Booking) item, mUser);
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mPresenter.getCurrentUserLocal();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mBSheetBook.onBackPressed()) {
            return;
        }

        if (vp_content.getCurrentItem() > 0) {
            vp_content.setCurrentItem(0, true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mBSheetBook.onDetach();
        mPresenter.onDetach();
        super.onDestroy();
    }
}
