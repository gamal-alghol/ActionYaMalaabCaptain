package com.malaab.ya.action.actionyamalaab.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.annotations.PaymentMethod;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.model.DurationListItem;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.data.model.SizeListItem;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingPlayground;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.PlaygroundSchedule;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.data.network.model.UserBooking;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.adapter.DaysAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.TimesAdapter;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.individual.NoOfPlayers;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.utils.NotificationUtils;
import com.malaab.ya.action.actionyamalaab.utils.ShareUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.ViewUtils;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.rd.PageIndicatorView;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BSheetBook extends BottomSheetBehavior {

    @BindView(R.id.bSheet_book)
    RelativeLayout bSheet_book;

    @BindView(R.id.pBar_loading)
    ProgressBar pBar_loading;
    @BindView(R.id.pBar_times)
    ShimmerFrameLayout pBar_times;

    @BindView(R.id.nsc_content)
    NestedScrollView nsc_content;

    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_price)
    TextView txt_price;

    @BindView(R.id.btn_favourite)
    AppCompatImageButton btn_favourite;
    @BindView(R.id.txt_favourite)
    TextView txt_favourite;

    @BindView(R.id.vp_images)
    ViewPager vp_images;
    @BindView(R.id.mPageIndicatorView)
    PageIndicatorView mPageIndicatorView;

    @BindView(R.id.ln_amenitiesShower)
    LinearLayout ln_amenitiesShower;
    @BindView(R.id.ln_amenitiesWC)
    LinearLayout ln_amenitiesWC;
    @BindView(R.id.ln_amenitiesGrass)
    LinearLayout ln_amenitiesGrass;
    @BindView(R.id.ln_amenitiesWater)
    LinearLayout ln_amenitiesWater;
    @BindView(R.id.ln_amenitiesBall)
    LinearLayout ln_amenitiesBall;

    @BindView(R.id.ln_young)
    LinearLayout ln_young;
    @BindView(R.id.cpb_young)
    CircularProgressBar cpb_young;
    @BindView(R.id.total_old)
    TextView total_old;
    @BindView(R.id.chk_young)
    CheckBox chk_young;

    @BindView(R.id.txt_age)
    TextView txt_age;
    @BindView(R.id.ln_age)
    LinearLayout ln_age;
    @BindView(R.id.ln_middle)
    LinearLayout ln_middle;
    @BindView(R.id.cpb_middle)
    CircularProgressBar cpb_middle;
    @BindView(R.id.total_middle)
    TextView total_middle;
    @BindView(R.id.chk_middle)
    CheckBox chk_middle;

    @BindView(R.id.ln_old)
    LinearLayout ln_old;
    @BindView(R.id.cpb_old)
    CircularProgressBar cpb_old;
    @BindView(R.id.total_young)
    TextView total_young;
    @BindView(R.id.chk_old)
    CheckBox chk_old;

    @BindView(R.id.txt_duration)
    TextView txt_duration;
    @BindView(R.id.txt_peopleNumber)
    TextView txt_peopleNumber;
    @BindView(R.id.txt_size)
    TextView txt_size;

    @BindView(R.id.ln_duration)
    LinearLayout ln_duration;
    @BindView(R.id.ln_peopleNumber)
    LinearLayout ln_peopleNumber;
    @BindView(R.id.ln_size)
    LinearLayout ln_size;

    @BindView(R.id.btn_calendar)
    AppCompatImageButton btn_calendar;
    @BindView(R.id.txt_datetime)
    TextView txt_datetime;

    @BindView(R.id.rv_days)
    RecyclerView rv_days;
    @BindView(R.id.rv_times)
    RecyclerView rv_times;

    @BindView(R.id.btn_calendarIndividual)
    AppCompatImageButton btn_calendarIndividual;
    @BindView(R.id.txt_dateIndividual)
    TextView txt_dateIndividual;
    @BindView(R.id.btn_timeIndividual)
    AppCompatImageButton btn_timeIndividual;
    @BindView(R.id.txt_timeIndividual)
    TextView txt_timeIndividual;

    @BindView(R.id.btn_continue)
    Button btn_continue;


    @BindView(R.id.nsc_summary)
    NestedScrollView nsc_summary;
    @BindView(R.id.summary_txt_date)
    TextView summary_txt_date;
    @BindView(R.id.summary_txt_timeStart)
    TextView summary_txt_timeStart;
    @BindView(R.id.summary_txt_timeEnd)
    TextView summary_txt_timeEnd;
    @BindView(R.id.summary_txt_type)
    TextView summary_txt_type;
    @BindView(R.id.summary_lbl_ageRange)
    TextView summary_lbl_ageRange;
    @BindView(R.id.summary_txt_ageRange)
    TextView summary_txt_ageRange;
    @BindView(R.id.summary_lbl_playersNumber)
    TextView summary_lbl_playersNumber;
    @BindView(R.id.summary_txt_playersNumber)
    TextView summary_txt_playersNumber;
    @BindView(R.id.summary_txt_price)
    TextView summary_txt_price;
    @BindView(R.id.summary_txt_paymentMethod)
    TextView summary_txt_paymentMethod;
    @BindView(R.id.summary_btn_bookNow)
    Button summary_btn_bookNow;

    @BindView(R.id.txt_name_Owner)
    TextView txt_name_Owner;
    @BindView(R.id.txt_Owner_info)
    TextView txt_Owner_info;
    @BindView(R.id.txt_name_guard)
    TextView txt_name_guard;
    @BindView(R.id.txt_guard_info)
    TextView txt_guard_info;
    //    @BindView(R.id.ln_booking_info)
//    LinearLayout ln_booking_info;
    @BindView(R.id.ln_result)
    LinearLayout ln_result;
    @BindView(R.id.img_status)
    AppCompatImageView img_status;
    @BindView(R.id.txt_status)
    TextView txt_status;

    @Inject
    DialogConfirmation mDialogConfirmation;

    private DaysAdapter daysAdapter;
    private TimesAdapter timesAdapter;

    private Activity mActivity;
    private BottomSheetBehavior mBottomSheetBehavior;
    private OnBottomSheetListener mBottomSheetListener;

    private DialogList mDialogList;

    private DatabaseReference mDatabasePlaygrounds;
    private DatabaseReference mDatabaseBookings;
    private ValueEventListener mValueEventListener;
    //    private String mUserUid;
    private Playground  mPlayground;
    private PlaygroundSchedule playgroundSchedule;
    private Booking booking;

    private String bookingId;
    private BookingAgeCategory ageCategory;
    private User mUser;
    private CalendarDay mCalendarDay;
    private CalendarTime mCalendarTime;
    private boolean isCollapsed = true;
    private List<PlaygroundSchedule> playgroundSchedules;
    private SizeListItem sizeListItem;
    private DurationListItem durationListItem;
    private int invitees = 0;
    private int totalInvitees = 0;
    private boolean isParticipated = false;
    private boolean isShowUserBooking = false;
private  int totalYnungplayer,totalMiddleplayer,totaloldplayer;

    public void attachAndInit(final Activity activity) {
        Log.d("ttt","attachAndInit");
        if (activity != null) {
            mActivity = activity;

            View bottomSheet = mActivity.findViewById(R.id.bSheet_book);
            ButterKnife.bind(this, bottomSheet);

            nsc_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY == 0 && scrollY < oldScrollY) {
//                        AppLogger.w("TOP SCROLL && Scroll UP");
                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(false);
                        }
                    } else {
                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
                        }
                    }

//                    if (scrollY > oldScrollY) {
//                        AppLogger.w("Scroll DOWN");
//                        isLocked = true;
//                    }
//
//                    if (scrollY < oldScrollY) {
//                        AppLogger.w("Scroll UP");
//                        isLocked = true;
//                    }
//
//                    if (scrollY == 0) {
//                        AppLogger.w("TOP SCROLL");
//                        isLocked = false;
//                    }
//
//                    if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
//                        AppLogger.w("BOTTOM SCROLL");
//                        isLocked = false;
//                    }
//
//                    if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                        ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(isLocked);
//                    }
                }
            });

            vp_images.setPageTransformer(true, new CubeInRotationTransformation());

            mDialogList = new DialogList().with(mActivity);
            mDialogList.build();

            rv_days.setHasFixedSize(true);
            rv_days.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

            rv_times.setHasFixedSize(true);
            rv_times.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);

            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setPeekHeight(0);

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {


                    if (newState == STATE_EXPANDED) {
                        Log.d("ttt","STATE_EXPANDED");
//                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
//                        }

                        if (mBottomSheetListener != null) {
                            mBottomSheetListener.onBottomSheetExpanded();
                        }

                        if (isCollapsed) {
                            if (mPlayground == null) {
                                return;
                            }

                            isCollapsed = false;

                            if (isShowUserBooking) {
                                initUserBookingInfo();
                                return;
                            }

                            if (mPlayground.isIndividuals) {
                                initPlaygroundDetails();
                                initPlaygroundIndividualsDetails();

                            } else {
                                getPlaygroundSchedules();
                            }
                        }

                    } else if (newState == STATE_COLLAPSED) {
                        Log.d("ttt","STATE_EXPANDED");

                        txt_price.setText("");

                        ln_result.setVisibility(View.GONE);
                        nsc_summary.setVisibility(View.GONE);

                        ln_amenitiesShower.setVisibility(View.GONE);
                        ln_amenitiesWC.setVisibility(View.GONE);
                        ln_amenitiesGrass.setVisibility(View.GONE);
                        ln_amenitiesWater.setVisibility(View.GONE);
                        ln_amenitiesBall.setVisibility(View.GONE);

                        txt_age.setVisibility(View.GONE);
                        ln_age.setVisibility(View.GONE);
                        ln_young.setVisibility(View.GONE);
                        ln_middle.setVisibility(View.GONE);
                        ln_old.setVisibility(View.GONE);
                        chk_young.setChecked(false);
                        chk_middle.setChecked(false);
                        chk_old.setChecked(false);

                        totalMiddleplayer=0;
                        totaloldplayer=0;
                        totalYnungplayer=0;

                        invitees = 0;
                        totalInvitees = 0;
                        txt_peopleNumber.setText("");
                        ln_peopleNumber.setVisibility(View.GONE);

                        ln_peopleNumber.setEnabled(true);
                        ln_duration.setEnabled(true);
                        ln_size.setEnabled(true);
                        txt_Owner_info.setVisibility(View.GONE);
                        txt_name_Owner.setVisibility(View.GONE);
                        txt_guard_info.setVisibility(View.GONE);
                        txt_name_guard.setVisibility(View.GONE);
                        btn_calendarIndividual.setVisibility(View.GONE);
                        txt_dateIndividual.setVisibility(View.GONE);
                        btn_timeIndividual.setVisibility(View.GONE);
                        txt_timeIndividual.setVisibility(View.GONE);

                        btn_continue.setText(getString(R.string.continuee));

                        nsc_content.setVisibility(View.GONE);

                        btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                        btn_continue.setVisibility(View.GONE);

                        isCollapsed = true;
                        mPlayground = null;
                        isShowUserBooking = false;
                        isParticipated = false;
//                        pBar_loading.setVisibility(View.VISIBLE);

                        if (mBottomSheetListener != null) {
                            mBottomSheetListener.onBottomSheetCollapsed();
                        }
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if(mActivity.getClass().equals(HomeActivity.class)){

                    Log.d("ttt","onSlide");
                    CoordinatorLayout drawerLayout=mActivity.findViewById(R.id.cl_container);
                    CoordinatorLayout fragment=mActivity.findViewById(R.id.bsheet_book);


                    drawerLayout.setY(fragment.getHeight() * -slideOffset);
                    Log.d("ttt",fragment.getWidth() * slideOffset+"");
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lp.height = bottomSheet.getHeight() - (int) (bottomSheet.getHeight() * slideOffset * 0.3f);
                    lp.topMargin = (bottomSheet.getHeight() - lp.height) / 4;
                    drawerLayout.setLayoutParams(lp);
                    if (mBottomSheetListener == null)
                        return;
                    mBottomSheetListener.onSlide(slideOffset);

                }}
            });
        }
    }

    public void setOnBottomSheetListener(OnBottomSheetListener listener) {
        mBottomSheetListener = listener;
    }


    @OnClick({R.id.btn_share, R.id.txt_share})
    public void sharePlayground() {
        if (mPlayground.isIndividuals) {
            ShareUtils.sharePlayground(mActivity, mPlayground.playgroundId, mPlayground.name, true, mPlayground.booking.bookingUId);
        } else {
            ShareUtils.sharePlayground(mActivity, mPlayground.playgroundId, mPlayground.name, false, "");
        }
    }


    @OnClick({R.id.btn_location, R.id.txt_location})
    public void showPlaygroundLocation() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.INTENT_KEY_PLAYGROUND,mPlayground);
        ActivityUtils.goTo(mActivity, MapActivity.class, false, bundle);


 //   EventBus.getDefault().post(new OnEventItemClicked<>(mPlayground, ItemAction.LOCATION, 0));


    }

    @OnClick({R.id.btn_favourite, R.id.txt_favourite})
    public void setFavourite() {
        mPlayground.isFavourite = !mPlayground.isFavourite;
        if (mPlayground.isFavourite) {
            EventBus.getDefault().post(new OnEventItemClicked<>(mPlayground, ItemAction.FAVOURITE_ADD, 0));
        } else {
            EventBus.getDefault().post(new OnEventItemClicked<>(mPlayground, ItemAction.FAVOURITE_REMOVE, 0));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.chk_young)
    public void selectYoung() {
        chk_young.setChecked(true);
        chk_middle.setChecked(false);
        chk_old.setChecked(false);
        chk_young.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        chk_young.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.colorAccent)));

        chk_middle.setTextColor(mActivity.getResources().getColor(R.color.black));
        chk_middle.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));

        chk_old.setTextColor(mActivity.getResources().getColor(R.color.black));
        chk_old.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));
        ageCategory = new BookingAgeCategory();
        ageCategory.ageRangeStart = 8;
        ageCategory.ageRangeEnd = 12;
        ageCategory.name = "8 - 12";
    }

    @OnClick(R.id.chk_middle)
    public void selectMiddle() {
        chk_middle.setChecked(true);
        chk_young.setChecked(false);
        chk_old.setChecked(false);

        chk_young.setTextColor(mActivity.getResources().getColor(R.color.black));
        chk_young.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));

        chk_middle.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        chk_middle.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.colorAccent)));

        chk_old.setTextColor(mActivity.getResources().getColor(R.color.black));
        chk_old.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));

        ageCategory = new BookingAgeCategory();
        ageCategory.ageRangeStart = 13;
        ageCategory.ageRangeEnd = 17;
        ageCategory.name = "13 - 17";

    }

    @OnClick(R.id.chk_old)
    public void selectOld() {
        chk_old.setChecked(true);
        chk_young.setChecked(false);
        chk_middle.setChecked(false);

        chk_young.setTextColor(mActivity.getResources().getColor(R.color.black));
        chk_young.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));

        chk_middle.setTextColor(mActivity.getResources().getColor(R.color.black));
        chk_middle.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));

        chk_old.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        chk_old.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.colorAccent)));

        ageCategory = new BookingAgeCategory();
        ageCategory.ageRangeStart = 18;
        ageCategory.ageRangeEnd = 80;
        ageCategory.name = "+18";
    }


    @OnClick(R.id.ln_duration)
    public void selectDuration() {
        if (!mPlayground.isIndividuals) {
            mDialogList.showDurations();
        }
    }

    @OnClick(R.id.ln_peopleNumber)
    public void selectPeopleNumber() {
        if (mPlayground.isIndividuals) {
            mDialogList
                    .withTitle(R.string.title_invitees_select)
                    .showItems();
        }
    }

    @OnClick(R.id.ln_size)
    public void selectSize() {
        if (!mPlayground.isIndividuals) {
            mDialogList.showSizes();
        }
    }

    @OnClick(R.id.btn_continue)
    public void continueBooking() {
        Log.d("ttt","continueBooking");

        booking = new Booking();

        if (mPlayground.isIndividuals) {

            booking = mPlayground.booking;
            if (isParticipated) {
//                cancelIndividualBooking();    /* To be handled in MyBookings */
                return;
            }

            if (!chk_young.isChecked() && !chk_middle.isChecked() && !chk_old.isChecked()) {
                showMessage(getString(R.string.msg_age_select));
                return;
            }

        } else {
            if (mCalendarDay == null) {
                showMessage(getString(R.string.msg_no_day_select));
                return;
            }

            if (mCalendarTime == null) {
                showMessage(getString(R.string.msg_no_time_select));
                return;
            }

            booking.datetimeCreated = DateTimeUtils.getCurrentDatetime();

            booking.timeStart = mCalendarTime.timeStart;
            booking.timeEnd = mCalendarTime.timeEnd;

            booking.duration = durationListItem.value;
            booking.size = sizeListItem.value;
            booking.price = playgroundSchedule.price;
            booking.paymentMethod = PaymentMethod.CASH;

            booking.isIndividuals = playgroundSchedule.isIndividuals;
        }

//        booking.day = mCalendarDay.dayName;
//        booking.datetimeCreated = mCalendarDay.dayName + ", " + mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year;
//        booking.datetimeCreated = mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year;

        summary_txt_date.setText(DateTimeUtils.changeDateFormat(new Date(booking.timeStart), DateTimeUtils.PATTERN_DATE_3));
        summary_txt_timeStart.setText(String.valueOf(DateTimeUtils.getTime12Hour(booking.timeStart)));
        summary_txt_timeEnd.setText(String.valueOf(DateTimeUtils.getTime12Hour(booking.timeEnd)));

        if (mPlayground.isIndividuals) {

            summary_lbl_ageRange.setVisibility(View.VISIBLE);
            summary_txt_ageRange.setVisibility(View.VISIBLE);
            summary_lbl_playersNumber.setVisibility(View.VISIBLE);
            summary_txt_playersNumber.setVisibility(View.VISIBLE);

            summary_txt_type.setText(getString(R.string.individual));
            summary_txt_ageRange.setText(ageCategory.name);
            summary_txt_playersNumber.setText(String.valueOf(invitees));

            summary_txt_price.setText(String.format(Locale.ENGLISH, getString(R.string.price), (booking.priceIndividual / booking.size) + (invitees * (booking.priceIndividual / booking.size)), ""));

        } else {

            summary_lbl_ageRange.setVisibility(View.GONE);
            summary_txt_ageRange.setVisibility(View.GONE);
            summary_lbl_playersNumber.setVisibility(View.GONE);
            summary_txt_playersNumber.setVisibility(View.GONE);

            summary_txt_type.setText(getString(R.string.full));

//            summary_txt_price.setText(String.format(Locale.ENGLISH, getString()(R.string.price), booking.price, "/h"));
            summary_txt_price.setText(String.format(Locale.ENGLISH, getString(R.string.price), booking.price, ""));
        }

        if (booking.paymentMethod.equalsIgnoreCase(PaymentMethod.CASH)) {
            summary_txt_paymentMethod.setText(getString(R.string.payment_cash));
        }

        YoYo.with(Techniques.RotateInDownLeft)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsc_summary.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        nsc_content.setVisibility(View.GONE);
                        btn_continue.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(nsc_summary);
    }

    @OnClick(R.id.summary_btn_bookNow)
    public void bookNow() {
Log.d("ttt","bookNow");
        summary_btn_bookNow.setAlpha(.3f);
        summary_btn_bookNow.setEnabled(false);

        makeBooking();
    }

    @OnClick(R.id.result_btn_back)
    public void back() {
        hide();
//        ActivityUtils.goTo(mActivity, HomeActivity.class, true);
    }


    public void initPlaygroundDetails() {

        txt_name.setText(mPlayground.name);

        if (ListUtils.isEmpty(mPlayground.images)) {
            mPlayground.images = new ArrayList<>();
            mPlayground.images.add(" ");
        }

        PlaygroundImagesAdapter playgroundImagesAdapter = new PlaygroundImagesAdapter(mActivity, mPlayground.images);
        vp_images.setAdapter(playgroundImagesAdapter);

        mPageIndicatorView.setViewPager(vp_images);

        if (mPlayground.amenity != null) {
            if (mPlayground.amenity.hasShower) {
                ln_amenitiesShower.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasWC) {
                ln_amenitiesWC.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasGrass) {
                ln_amenitiesGrass.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasWater) {
                ln_amenitiesWater.setVisibility(View.VISIBLE);
            }

            if (mPlayground.amenity.hasBall) {
                ln_amenitiesBall.setVisibility(View.VISIBLE);
            }
        }

//        Calendar startDate = Calendar.getInstance();
////        String monthName = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
////        txt_month.setText(monthName);
//
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.DAY_OF_MONTH, 14);
//
//        daysAdapter = new DaysAdapter(getDates(startDate.getTime(), endDate.getTime()));
//        rv_days.setAdapter(daysAdapter);
//        daysAdapter.setIsSelectedByUSer(0, true);
//
//
//        Calendar startTime = Calendar.getInstance();
//        Calendar endTime = Calendar.getInstance();
//        endTime.add(Calendar.HOUR, 10);
//
//        timesAdapter = new TimesAdapter(getTimes(startTime.getTime(), endTime.getTime(), 30));
//        rv_times.setAdapter(timesAdapter);
//
//        timesAdapter.setIsSelectedByUSer(0, true);
    }

    public void initPlaygroundIndividualsDetails() {

        pBar_loading.setVisibility(View.VISIBLE);

        btn_calendar.setVisibility(View.GONE);
        txt_datetime.setVisibility(View.GONE);
        rv_days.setVisibility(View.GONE);
        rv_times.setVisibility(View.GONE);

        txt_age.setVisibility(View.VISIBLE);
        ln_age.setVisibility(View.VISIBLE);
        ln_peopleNumber.setVisibility(View.VISIBLE);

//        selectMiddle();     /* To trigger the checkbox to get value of age start & end */

        int size = mPlayground.booking.size;
        int total = 0;
        boolean hasYoung = false, hasMiddle = false, hasOld = false;
        int youngPlayers = 0, middlePlayers = 0, oldPlayers = 0;

        if (!ListUtils.isEmpty(mPlayground.booking.ageCategories)) {
            for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
                if (category.ageRangeStart == 8) {
                    hasYoung = true;
                    if (!ListUtils.isEmpty(category.players)) {
                        youngPlayers = category.players.size();
                        for (BookingPlayer player : category.players) {
                            youngPlayers += player.invitees;
                        }
                        totalYnungplayer=youngPlayers;
                    }
                } else if (category.ageRangeStart == 13) {
                    hasMiddle = true;
                    if (!ListUtils.isEmpty(category.players)) {
                        middlePlayers = category.players.size();
                        for (BookingPlayer player : category.players) {
                            middlePlayers += player.invitees;
                        }
                        totalMiddleplayer=middlePlayers;
                    }
                } else if (category.ageRangeStart == 18) {
                    hasOld = true;
                    if (!ListUtils.isEmpty(category.players)) {
                        oldPlayers = category.players.size();
                        for (BookingPlayer player : category.players) {
                            oldPlayers += player.invitees;
                        }
                        totaloldplayer=oldPlayers;
                    }
                }
            }
        }
Log.d("ttt",isParticipated+"123456");
       // if()
        chk_young.setEnabled(mUser != null && !isParticipated);
        chk_middle.setEnabled(mUser != null && !isParticipated);
        chk_old.setEnabled(mUser != null && !isParticipated);

        if (mUser != null) {
            if (isParticipated) {
                ln_peopleNumber.setClickable(false);

//            if (DateTimeUtils.getDifferenceInHours(new Date(), new Date(mPlayground.booking.timeStart)) <= 2) {
//                btn_continue.setClickable(true);
//                btn_continue.setText(getString(R.string.msg_user_same_booking_cancel));
//            } else {
                if (mPlayground.booking.status == BookingStatus.ADMIN_APPROVED) {
                    btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.orange));
                    btn_continue.setText(getString(R.string.msg_individual_booking_closed_confirmed));
                }else {
                btn_continue.setClickable(false);
            btn_continue.setAlpha(0.7f);
                btn_continue.setText(getString(R.string.msg_user_same_booking));}

//            }

                btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.orange));
                btn_continue.setVisibility(View.VISIBLE);

            } else {

                ln_peopleNumber.setClickable(true);
                btn_continue.setClickable(true);
                btn_continue.setAlpha(1);
                btn_continue.setTextSize(16);
                btn_continue.setText(getString(R.string.join));
                btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                btn_continue.setVisibility(View.VISIBLE);
                chk_young.setTextColor(mActivity.getResources().getColor(R.color.black));
                chk_young.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));
                chk_middle.setTextColor(mActivity.getResources().getColor(R.color.black));
                chk_middle.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));
                chk_old.setTextColor(mActivity.getResources().getColor(R.color.black));
                chk_old.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.black)));
                if (youngPlayers >= size) {
                    chk_young.setEnabled(false);
                    chk_young.setTextColor(mActivity.getResources().getColor(R.color.dark_gray));
                    chk_young.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.dark_gray)));
                }
                if (middlePlayers >= size) {
                    chk_middle.setEnabled(false);
                    chk_middle.setTextColor(mActivity.getResources().getColor(R.color.dark_gray));
                    chk_middle.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.dark_gray)));
                }
                if (oldPlayers >= size) {
                    chk_old.setEnabled(false);
                    chk_middle.setTextColor(mActivity.getResources().getColor(R.color.dark_gray));
                    chk_middle.setButtonTintList(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.dark_gray)));
                }
                if (mPlayground.booking.status == BookingStatus.ADMIN_APPROVED) {
                    btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.orange));
                    btn_continue.setText(getString(R.string.msg_individual_booking_closed_confirmed));
                } else {
                    btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.light_green));
                    btn_continue.setText(getString(R.string.join));
                }
                if (mPlayground.booking.status == BookingStatus.ADMIN_APPROVED || (youngPlayers >= size && middlePlayers >= size && oldPlayers >= size)) {
                    ln_peopleNumber.setClickable(false);
                    btn_continue.setClickable(false);
                    btn_continue.setAlpha(.3f);
                    btn_continue.setTextSize(16);
                    btn_continue.setVisibility(View.VISIBLE);


                }
            }
        }
if(isParticipated==false){

}
        txt_price.setText(String.format(Locale.ENGLISH, getString(R.string.price), mPlayground.booking.priceIndividual / size, ""));

        int animationDuration = 2000; // 2500ms = 2,5s

        if (hasYoung) {
            total_young.setText(String.format(Locale.ENGLISH, "%d/%d", youngPlayers, size));
            cpb_young.setProgressWithAnimation(((float) youngPlayers / (float) size) * 100, animationDuration);
            ln_young.setVisibility(View.VISIBLE);
            if(youngPlayers==size){
                zoomIn(ln_young);

                total_young.setTextColor(mActivity.getColor(R.color.colorAccent));
            }else {
                zoomOut(ln_young);
                total_young.setTextColor(mActivity.getColor(R.color.black_effective));

            }
            if (mPlayground.booking!=null){
            total_young.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(totalYnungplayer>=1){
                        mActivity.startActivity(new Intent(mActivity, NoOfPlayers.class).putExtra("bookingUId",mPlayground.booking.bookingUId)
                   .putExtra("ageCategories","0"));

                }}
            });
            }

        }

        if (hasMiddle) {
            total_middle.setText(String.format(Locale.ENGLISH, "%d/%d", middlePlayers, size));
            cpb_middle.setProgressWithAnimation(((float) middlePlayers / (float) size) * 100, animationDuration);
            Log.d("ttt",middlePlayers+"");
            if(middlePlayers==size){
zoomIn(ln_middle);
                total_middle.setTextColor(mActivity.getColor(R.color.colorAccent));
            }else {
                zoomOut(ln_middle);
                total_middle.setTextColor(mActivity.getColor(R.color.black_effective));

            }

            ln_middle.setVisibility(View.VISIBLE);
            if (mPlayground.booking!=null){

                total_middle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (totalMiddleplayer >= 1) {
                        mActivity.startActivity(new Intent(mActivity, NoOfPlayers.class).putExtra("bookingUId", mPlayground.booking.bookingUId)
                                .putExtra("ageCategories", "1"));

                    }
                }
            });}
        }

        if (hasOld) {

            total_old.setText(String.format(Locale.ENGLISH, "%d/%d", oldPlayers, size));
            cpb_old.setProgressWithAnimation(((float) oldPlayers / (float) size) * 100, animationDuration);
            ln_old.setVisibility(View.VISIBLE);
            if(oldPlayers==size){
                zoomIn(ln_old);
                total_old.setTextColor(mActivity.getColor(R.color.colorAccent));
            }else {
                zoomOut(ln_old);
                total_old.setTextColor(mActivity.getColor(R.color.black_effective));

            }
            if (mPlayground.booking!=null){
            total_old.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (totaloldplayer >= 1) {

                        mActivity.startActivity(new Intent(mActivity, NoOfPlayers.class).putExtra("bookingUId", mPlayground.booking.bookingUId)
                                .putExtra("ageCategories", "2"));

                    }
                }
            });}
        }

        txt_size.setText(String.format(Locale.ENGLISH, "%s x %s", (size / 2), (size / 2)));
        txt_duration.setText(String.format(Locale.ENGLISH, "%s %s", mPlayground.booking.duration, "mins"));

        List<GenericListItem> invitees = new ArrayList<>();
        invitees.add(new GenericListItem(getString(R.string.no_player), "0"));
        invitees.add(new GenericListItem(getString(R.string.one_player), "1"));
        invitees.add(new GenericListItem(getString(R.string.two_player), "2"));
//        invitees.add(new GenericListItem(getString(R.string.three_player), "3"));
//        invitees.add(new GenericListItem(getString(R.string.four_player), "4"));
//        invitees.add(new GenericListItem(getString(R.string.five_player), "5"));
        mDialogList.addItems(invitees);

        txt_dateIndividual.setText(DateTimeUtils.getDatetime(mPlayground.booking.timeStart, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));
        txt_timeIndividual.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(mPlayground.booking.timeStart), DateTimeUtils.getTime12Hour(mPlayground.booking.timeEnd)));

if(mPlayground.booking.status==BookingStatus.OWNER_RECEIVED){
    if (mPlayground.booking.isPast==false) {
        txt_Owner_info.setVisibility(View.VISIBLE);
        txt_name_guard.setVisibility(View.VISIBLE);
        txt_guard_info.setVisibility(View.VISIBLE);
        txt_name_Owner.setVisibility(View.VISIBLE);
        txt_name_Owner.setText(getString(R.string.owner) + ": " + mPlayground.nameOwner + "\n" + getString(R.string.mobile)
                + ": " + mPlayground.mobileOwner);

        txt_name_guard.setText(getString(R.string.guard) + ": " + mPlayground.nameguard + "\n" + getString(R.string.mobile)
                + ": " + mPlayground.mobileguard);
    }
}
        btn_calendarIndividual.setVisibility(View.VISIBLE);

        txt_dateIndividual.setVisibility(View.VISIBLE);
        btn_timeIndividual.setVisibility(View.VISIBLE);
        txt_timeIndividual.setVisibility(View.VISIBLE);

        pBar_loading.setVisibility(View.GONE);

        nsc_content.setVisibility(View.VISIBLE);
    }

    private void initCategories(Booking booking) {
        Log.d("ttt","initCategories");
        txt_age.setVisibility(View.VISIBLE);
        ln_age.setVisibility(View.VISIBLE);
this.booking=booking;

        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
        mDatabaseBookings.child(booking.bookingUId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Booking booking = dataSnapshot.getValue(Booking.class);
                            if (booking != null) {
                                int size = booking.size;
                                boolean hasYoung = false, hasMiddle = false, hasOld = false;
                                int youngPlayers = 0, middlePlayers = 0, oldPlayers = 0;

                                if (!ListUtils.isEmpty(booking.ageCategories)) {
                                    for (BookingAgeCategory category : booking.ageCategories) {
                                        if (category.ageRangeStart == 8) {
                                            hasYoung = true;
                                            if (!ListUtils.isEmpty(category.players)) {
                                                youngPlayers = category.players.size();
                                                for (BookingPlayer player : category.players) {
                                                    youngPlayers += player.invitees;
                                                }
                                                totalYnungplayer=youngPlayers;
                                            }
                                        } else if (category.ageRangeStart == 13) {
                                            hasMiddle = true;
                                            Log.d("ttt","true");
                                            if (!ListUtils.isEmpty(category.players)) {
                                                middlePlayers = category.players.size();
                                                for (BookingPlayer player : category.players) {
                                                    middlePlayers += player.invitees;
                                                }

                                                totalMiddleplayer=middlePlayers;
                                                Log.d("ttt",totalMiddleplayer+"");
                                            }
                                        } else if (category.ageRangeStart == 18) {
                                            hasOld = true;
                                            if (!ListUtils.isEmpty(category.players)) {
                                                oldPlayers = category.players.size();
                                                for (BookingPlayer player : category.players) {
                                                    oldPlayers += player.invitees;
                                                }
                                                totaloldplayer=oldPlayers;

                                            }
                                        }
                                    }
                                }

                                chk_young.setEnabled(!isParticipated);
                                chk_middle.setEnabled(!isParticipated);
                                chk_old.setEnabled(!isParticipated);

                                int animationDuration = 2000; // 2500ms = 2,5s

                                if (hasYoung) {
                                    total_young.setText(String.format(Locale.ENGLISH, "%d/%d", youngPlayers, size));
                                    cpb_young.setProgressWithAnimation(((float) youngPlayers / (float) size) * 100, animationDuration);
                                    if(youngPlayers==size){
                                        zoomIn(ln_young);
                                        total_young.setTextColor(mActivity.getColor(R.color.colorAccent));
                                    }else {
                                        zoomOut(ln_young);
                                        total_young.setTextColor(mActivity.getColor(R.color.black_effective));
                                    }
                                        total_young.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(totalYnungplayer>=1){
                                                    mActivity.startActivity(new Intent(mActivity, NoOfPlayers.class).putExtra("bookingUId",mPlayground.booking.bookingUId)
                                                            .putExtra("ageCategories","0"));

                                                }}
                                        });

                                    ln_young.setVisibility(View.VISIBLE);
                                }

                                if (hasMiddle) {
                                    total_middle.setText(String.format(Locale.ENGLISH, "%d/%d", middlePlayers, size));
                                    cpb_middle.setProgressWithAnimation(((float) middlePlayers / (float) size) * 100, animationDuration);
                                    if(middlePlayers==size){
                                        zoomIn(ln_middle);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            total_middle.setTextColor(mActivity.getColor(R.color.colorAccent));
                                        }
                                    }else {
                                        zoomOut(ln_middle);
                                        total_middle.setTextColor(mActivity.getColor(R.color.black_effective));
                                    }
                                    ln_middle.setVisibility(View.VISIBLE);

                                    if (mPlayground.booking!=null){
                                        total_middle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (totalMiddleplayer >= 1) {
                                                    mActivity.startActivity(new Intent(mActivity, NoOfPlayers.class).putExtra("bookingUId", mPlayground.booking.bookingUId)
                                                            .putExtra("ageCategories", "1"));

                                                }
                                            }
                                        });}
                                }

                                if (hasOld) {
                                    total_old.setText(String.format(Locale.ENGLISH, "%d/%d", oldPlayers, size));
                                    cpb_old.setProgressWithAnimation(((float) oldPlayers / (float) size) * 100, animationDuration);
                                    if(oldPlayers==size){
                                        zoomIn(ln_old);

                                        total_old.setTextColor(mActivity.getColor(R.color.colorAccent));
                                    }else {

                                            zoomOut(ln_old);
                                            total_old.setTextColor(mActivity.getColor(R.color.black_effective));

                                    }
                                    ln_old.setVisibility(View.VISIBLE);
                                    if (mPlayground.booking!=null){

                                        total_middle.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (totaloldplayer >= 1) {
                                                    mActivity.startActivity(new Intent(mActivity, NoOfPlayers.class).putExtra("bookingUId", mPlayground.booking.bookingUId)
                                                            .putExtra("ageCategories", "2"));

                                                }
                                            }
                                        });}
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pBar_times.setVisibility(View.GONE);
                        AppLogger.e("GetBookings() Error -> " + databaseError.toException());
                    }
                });
    }

    public void initUserBookingInfo() {
        Log.d("ttt","initUserBookingInfo");
        if (mPlayground.booking.isIndividuals) {
            txt_price.setText(String.format(Locale.ENGLISH, getString(R.string.price), mPlayground.booking.priceIndividual / mPlayground.booking.size, ""));

            ln_peopleNumber.setVisibility(View.VISIBLE);
            ln_peopleNumber.setEnabled(false);
            txt_peopleNumber.setText(getInvitees(mPlayground.booking.invitees));

            initCategories(mPlayground.booking);

        } else {
            txt_price.setText(String.format(Locale.ENGLISH, getString(R.string.price), mPlayground.booking.price, ""));
            ln_peopleNumber.setVisibility(View.GONE);
        }

        ln_duration.setEnabled(false);
        ln_size.setEnabled(false);

        btn_calendar.setVisibility(View.GONE);
        txt_datetime.setVisibility(View.GONE);
        rv_days.setVisibility(View.GONE);
        rv_times.setVisibility(View.GONE);


        btn_calendarIndividual.setVisibility(View.VISIBLE);
        txt_dateIndividual.setVisibility(View.VISIBLE);
        btn_timeIndividual.setVisibility(View.VISIBLE);
        txt_timeIndividual.setVisibility(View.VISIBLE);

        initPlaygroundDetails();
        if(mPlayground.booking.status==BookingStatus.OWNER_RECEIVED){
            if (mPlayground.booking.isPast==false) {
            txt_Owner_info.setVisibility(View.VISIBLE);
            txt_name_guard.setVisibility(View.VISIBLE);
            txt_guard_info.setVisibility(View.VISIBLE);
            txt_name_Owner.setVisibility(View.VISIBLE);
            txt_name_Owner.setText(getString(R.string.owner)+": "+mPlayground.nameOwner + "\n"+getString(R.string.mobile)
                    +": "+mPlayground.mobileOwner);

            txt_name_guard.setText(getString(R.string.guard)+": "+mPlayground.nameguard + "\n"+getString(R.string.mobile)
                    +": "+mPlayground.mobileguard);

        }}


        txt_size.setText(String.format(Locale.ENGLISH, "%s x %s", (mPlayground.booking.size / 2), (mPlayground.booking.size / 2)));
        txt_duration.setText(String.format(Locale.ENGLISH, "%s %s", mPlayground.booking.duration, "mins"));

        txt_dateIndividual.setText(DateTimeUtils.getDatetime(mPlayground.booking.timeStart, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));
        txt_timeIndividual.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(mPlayground.booking.timeStart), DateTimeUtils.getTime12Hour(mPlayground.booking.timeEnd)));

        pBar_loading.setVisibility(View.GONE);
        nsc_content.setVisibility(View.VISIBLE);
    }


    private void getPlaygroundSchedules() {
        if (mPlayground == null) {
            return;
        }

        pBar_loading.setVisibility(View.VISIBLE);

        mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_TABLE).child(mPlayground.playgroundId);

        /* To load the list once only*/
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isCollapsed) {
                    return;
                }

                PlaygroundSchedule playgroundSchedule;
                playgroundSchedules = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    playgroundSchedule = child.getValue(PlaygroundSchedule.class);
                    if (playgroundSchedule != null ){
                        if (playgroundSchedule.isActive){
                            playgroundSchedules.add(playgroundSchedule);
                        }
                        else{
                            playgroundSchedules.remove(playgroundSchedule);
                        }
                    }
                }

                initPlaygroundDetails();

                populateDays();

                pBar_loading.setVisibility(View.GONE);
                nsc_content.setVisibility(View.VISIBLE);
                btn_continue.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppLogger.e(" Error -> " + error.toException());
//
//                if (isCollapsed) {
//                    return;
//                }
            }
        };

        mDatabasePlaygrounds.addValueEventListener(mValueEventListener);
    }


    private void populateDays() {
        Calendar startDate = Calendar.getInstance();
//        startDate.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 0, 0, 0);

        Calendar endDate = Calendar.getInstance();
//        endDate.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 0, 0, 0);
        endDate.add(Calendar.DAY_OF_MONTH, 14);

        daysAdapter = new DaysAdapter(getDates(startDate.getTime(), endDate.getTime()));
        rv_days.setAdapter(daysAdapter);

        btn_calendar.setVisibility(View.VISIBLE);
        txt_datetime.setVisibility(View.VISIBLE);
        rv_days.setVisibility(View.VISIBLE);

        daysAdapter.setIsSelectedByUSer(0, true);
        mCalendarDay = daysAdapter.getItem(0);

//        if (isShowUserBooking) {
//            Calendar cal = new GregorianCalendar();
//            cal.setTime(new Date(mPlayground.booking.timeStart));
//
//            CalendarDay day = new CalendarDay(cal, true);
//            setDaySelected(day, 1);
//
//        } else {
        String dayFullName = startDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        getTimesOfDay(dayFullName);
//        }
    }

    private void getTimesOfDay(String dayFullName) {
//        if (LocaleHelper.getLanguage(mActivity).equalsIgnoreCase(Constants.LANGUAGE_ARABIC_CODE)) {
//            dayFullName = DateTimeUtils.getEnglishDayName(dayFullName);
//        }

        rv_times.setAdapter(null);
        rv_times.setVisibility(View.GONE);
        pBar_times.setVisibility(View.VISIBLE);

        boolean isTimesFound = false;

        for (PlaygroundSchedule schedule : playgroundSchedules) {

            if (dayFullName.toLowerCase().equals(schedule.day.toLowerCase())) {
                playgroundSchedule = schedule;

                txt_price.setText(String.format(Locale.ENGLISH, getString(R.string.price), schedule.price, ""));

                if (!ListUtils.isEmpty(schedule.size)) {
                    mDialogList.addSizes(schedule.size);
                    sizeListItem = schedule.size.get(0);
                    txt_size.setText(String.format(Locale.ENGLISH, "%s", sizeListItem.name));
                }

                if (!ListUtils.isEmpty(schedule.duration)) {
                    mDialogList.addDurations(schedule.duration);
                    durationListItem = schedule.duration.get(0);
                    txt_duration.setText(String.format(Locale.ENGLISH, "%s", durationListItem.name));

                    populateTimes(schedule.timeStart, schedule.timeEnd, schedule.duration.get(0).value, schedule.disabledTimes);
                }

                isTimesFound = true;
                break;
            }
        }

        if (!isTimesFound) {
            mDialogList.addDurations(new ArrayList<DurationListItem>());
            mDialogList.addSizes(new ArrayList<SizeListItem>());

            txt_duration.setText("");
            txt_size.setText("");

            pBar_times.setVisibility(View.GONE);

            mCalendarTime = null;

            btn_continue.setAlpha(.3f);
            btn_continue.setClickable(false);
        }
    }

    private void populateTimes(String timeStart, String timeEnd, int duration, List<CalendarTime> disabledTimes) {
        String[] startTime = timeStart.split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMin = Integer.parseInt(startTime[1]);

        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.setTime(mCalendarDay.date.getTime());
        startTimeCal.set(Calendar.HOUR_OF_DAY, startHour);
        startTimeCal.set(Calendar.MINUTE, startMin);

        String[] endTime = timeEnd.split(":");
        int endHour = Integer.parseInt(endTime[0]);
        int endMin = Integer.parseInt(endTime[1]);

        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.setTime(mCalendarDay.date.getTime());
        endTimeCal.set(Calendar.HOUR_OF_DAY, endHour);
        endTimeCal.set(Calendar.MINUTE, endMin);

        if (endTimeCal.before(startTimeCal)) {
            endTimeCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        timesAdapter = new TimesAdapter(getTimes(startTimeCal.getTime(), endTimeCal.getTime(), duration, disabledTimes));
        rv_times.setAdapter(timesAdapter);

        if (timesAdapter.getItemCount() > 0) {
            timesAdapter.setIsSelectedByUSer(0, true);
            mCalendarTime = timesAdapter.getItem(0);
        }

        btn_continue.setAlpha(mUser == null ? .3f : 1);
        btn_continue.setClickable(mUser != null);

//        getBookings(mPlayground.playgroundId, mCalendarDay.dayName + " " + mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year);
        getBookings(mPlayground.playgroundId, DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH));
    }


    public void setDaySelected(CalendarDay calendarDay, int pos) {
        mCalendarDay = calendarDay;
        daysAdapter.setIsSelectedByUSer(pos, true);

        String dayFullName = mCalendarDay.date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        getTimesOfDay(dayFullName);

        ViewUtils.scrollToBottom(nsc_content);
    }

    public void setTimeSelected(CalendarTime calendarTime, int pos) {
        mCalendarTime = calendarTime;
        timesAdapter.setIsSelectedByUSer(pos, true);

        if (calendarTime.isAvailable) {
            btn_continue.setAlpha(mUser == null ? .3f : 1);
            btn_continue.setClickable(mUser != null);

        } else {
            btn_continue.setAlpha(.3f);
            btn_continue.setClickable(false);
        }
    }


    private static List<CalendarDay> getDates(Date fromDate, Date toDate) {

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(fromDate);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate);

        int startDayNo;
        String dayName, monthName;
        int year;

        CalendarDay calendarDay;
        List<CalendarDay> days = new ArrayList<>();

        while (!startDate.after(endDate)) {
            dayName = startDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            monthName = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
            startDayNo = startDate.get(Calendar.DAY_OF_MONTH);
            year = startDate.get(Calendar.YEAR);

            Calendar tempDate = Calendar.getInstance();
            tempDate.setTime(startDate.getTime());

//            calendarDay = new CalendarDay(monthName, startDayNo, dayName, year, false);
            calendarDay = new CalendarDay(tempDate, false);
            days.add(calendarDay);

            startDate.add(Calendar.DATE, 1);
        }

        return days;
    }

    private static List<CalendarTime> getTimes(Date fromDate, Date toDate, int duration, List<CalendarTime> disabledTimes) {

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(fromDate);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate);

        String timeStart, timeEnd;

        CalendarTime calendarTime;
        List<CalendarTime> times = new ArrayList<>();

        while (startDate.before(endDate)) {

            /* To show upcoming hours only */
            if (startDate.getTime().after(Calendar.getInstance().getTime())) {

                timeStart = DateTimeUtils.changeDateFormat(startDate.getTime(), DateTimeUtils.PATTERN_TIME);

                Calendar tempTime = Calendar.getInstance();
                tempTime.setTime(startDate.getTime());

                Calendar endDateTemp = Calendar.getInstance();
                endDateTemp.setTime(tempTime.getTime());
                endDateTemp.add(Calendar.MINUTE, duration);

                timeEnd = DateTimeUtils.changeDateFormat(endDateTemp.getTime(), DateTimeUtils.PATTERN_TIME);

                calendarTime = new CalendarTime(tempTime.getTimeInMillis(), endDateTemp.getTimeInMillis(), duration, R.drawable.icon_football_field_new, false, true);
                if (!ListUtils.isEmpty(disabledTimes)) {
                    for (CalendarTime time : disabledTimes) {
                        if (DateTimeUtils.getHour(time.timeStart) == DateTimeUtils.getHour(calendarTime.timeStart)) {
                            calendarTime.isAvailable = time.isAvailable;
                        }
                    }
                }

                times.add(calendarTime);
            }

            startDate.add(Calendar.MINUTE, duration);
        }

        return times;
    }


    private void getBookings(final String playgroundId, final String date) {
        mDatabaseBookings = FirebaseDatabase.getInstance().
                getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);

        String criteria = playgroundId + "_" + date;
        mDatabaseBookings.orderByChild("playgroundId_date").equalTo(criteria)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Booking booking;
                            List<Booking> bookingList = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null) {
                                    bookingList.add(booking);
                                }
                            }

                            if (bookingList.size() > 0) {
                                List<CalendarTime> calendarTimes;

                                if (timesAdapter != null && timesAdapter.getItemCount() > 0) {
                                    calendarTimes = timesAdapter.getItems();

                                    for (Booking book : bookingList) {
                                        CalendarTime calendarTime = new CalendarTime(book.timeStart, book.timeEnd, book.duration, R.drawable.icon_football_field_new, false, false);

                                        for (CalendarTime time : calendarTimes) {
                                            if (DateTimeUtils.getHour(time.timeStart) == DateTimeUtils.getHour(calendarTime.timeStart)) {

                                                int pos = calendarTimes.indexOf(time);
                                                time = calendarTimes.get(pos);

                                                if (book.status == BookingStatus.PENDING ||
                                                        book.status == BookingStatus.ADMIN_APPROVED ||
                                                        book.status == BookingStatus.OWNER_RECEIVED) {

                                                    time.isAvailable = false;
                                                }

                                                timesAdapter.setItem(time, pos);
                                            }
                                        }

//                                        if (calendarTimes.contains(calendarTime)) {
//                                            int pos = calendarTimes.indexOf(calendarTime);
//                                            calendarTime = calendarTimes.get(pos);
//                                            calendarTime.isAvailable = false;
//
//                                            timesAdapter.updateItem(calendarTime, pos, true);
//                                        }
                                    }

                                    if (timesAdapter.getSelectedItem().isAvailable) {
                                        btn_continue.setAlpha(mUser == null ? .3f : 1);
                                        btn_continue.setClickable(mUser != null);
                                    } else {
                                        btn_continue.setAlpha(.3f);
                                        btn_continue.setClickable(false);
                                    }
                                }
                            }
                        }

                        getIndividualsBookings(playgroundId, date);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pBar_times.setVisibility(View.GONE);
                        AppLogger.e("GetBookings() Error -> " + databaseError.toException());
                    }
                });
    }

    private void getIndividualsBookings(String playgroundId, String date) {
        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);

        String criteria = playgroundId + "_" + date;
        mDatabaseBookings.orderByChild("playgroundId_date").equalTo(criteria)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Booking booking;
                            List<Booking> bookingList = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null && booking.isActive) {
                                    bookingList.add(booking);
                                }
                            }

                            if (bookingList.size() > 0) {
                                List<CalendarTime> calendarTimes;

                                if (timesAdapter != null && timesAdapter.getItemCount() > 0) {
                                    calendarTimes = timesAdapter.getItems();

                                    for (Booking book : bookingList) {
                                        CalendarTime calendarTime = new CalendarTime(book.timeStart, book.timeEnd, book.duration, R.drawable.icon_football_field_new, false, false);

                                        for (CalendarTime time : calendarTimes) {
                                            if (DateTimeUtils.getHour(time.timeStart) == DateTimeUtils.getHour(calendarTime.timeStart)) {
                                                int pos = calendarTimes.indexOf(time);
                                                time = calendarTimes.get(pos);
                                                time.isAvailable = false;

                                                timesAdapter.setItem(time, pos);
                                            }
                                        }

//                                        if (calendarTimes.contains(calendarTime)) {
//                                            int pos = calendarTimes.indexOf(calendarTime);
//                                            calendarTime = calendarTimes.get(pos);
//                                            calendarTime.isAvailable = false;
//
//                                            timesAdapter.updateItem(calendarTime, pos, true);
//                                        }
                                    }

                                    if (timesAdapter.getSelectedItem().isAvailable) {
                                        btn_continue.setAlpha(mUser == null ? .3f : 1);
                                        btn_continue.setClickable(mUser != null);
                                    } else {
                                        btn_continue.setAlpha(.3f);
                                        btn_continue.setClickable(false);
                                    }
                                }
                            }
                        }

                        pBar_times.setVisibility(View.GONE);

                        rv_times.setVisibility(View.VISIBLE);
                        ViewUtils.scrollToBottom(nsc_content);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pBar_times.setVisibility(View.GONE);
                        AppLogger.e("GetBookings() Error -> " + databaseError.toException());
                    }
                });
    }


    private void makeBooking() {

        if (mPlayground.isIndividuals) {



            if (!chk_young.isChecked() && !chk_middle.isChecked() && !chk_old.isChecked()) {
                showMessage(getString(R.string.msg_age_select));

                summary_btn_bookNow.setAlpha(1f);
                summary_btn_bookNow.setEnabled(true);

                return;
            }

            BookingPlayer user = new BookingPlayer();
            user.uId = mUser.uId;
            user.appUserId = mUser.appUserId;
            user.name = mUser.getUserFullName();
            user.email = mUser.email;
            user.mobileNo = mUser.mobileNo;
            user.profileImageUrl = mUser.profileImageUrl;
            user.invitees = invitees;

            user.price = (mPlayground.booking.priceIndividual / mPlayground.booking.size) + (invitees * (mPlayground.booking.priceIndividual / mPlayground.booking.size));

            if (chk_young.isChecked()) {
                for (BookingAgeCategory category : booking.ageCategories) {
                    if (category.ageRangeStart == 8) {
                        if (ListUtils.isEmpty(category.players)) {
                            category.players = new ArrayList<>();
                        }
                        booking.ageCategories.get(booking.ageCategories.indexOf(category)).players.add(user);

                        totalInvitees = category.players.size();
                        for (BookingPlayer player : category.players) {
                            totalInvitees += player.invitees;
                        }
                        break;
                    }
                }

            } else if (chk_middle.isChecked()) {
                for (BookingAgeCategory category : booking.ageCategories) {
                    if (category.ageRangeStart == 13) {
                        if (ListUtils.isEmpty(category.players)) {
                            category.players = new ArrayList<>();
                        }
                        booking.ageCategories.get(booking.ageCategories.indexOf(category)).players.add(user);

                        totalInvitees = category.players.size();
                        for (BookingPlayer player : category.players) {
                            totalInvitees += player.invitees;
                        }
                        break;
                    }
                }

            } else if (chk_old.isChecked()) {
                for (BookingAgeCategory category : booking.ageCategories) {
                    if (category.ageRangeStart == 18) {
                        if (ListUtils.isEmpty(category.players)) {
                            category.players = new ArrayList<>();
                        }
                        booking.ageCategories.get(booking.ageCategories.indexOf(category)).players.add(user);

                        totalInvitees = category.players.size();
                        for (BookingPlayer player : category.players) {
                            totalInvitees += player.invitees;
                        }
                        break;
                    }
                }
            }

            mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE).child(booking.bookingUId);
            mDatabaseBookings.child("ageCategories")
                    .setValue(booking.ageCategories)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AppLogger.w(" onSuccess");

                            addIndividualBookingToUser(mUser.uId, booking);

                            EventBus.getDefault().post(new OnEventRefresh());

                            img_status.setImageResource(R.drawable.icon_success);
if(booking.isIndividuals==true){
    txt_status.setText(getString(R.string.msg_booking_success_individual));

}else{
    txt_status.setText(getString(R.string.msg_booking_success));

}

//                             NotificationChannel mChannel = null;
//                             NotificationManager notifManager = null;
//                            if (notifManager == null) {
//                                notifManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
//                            }
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                NotificationCompat.Builder builder;
//                                Intent intent = new Intent(mActivity, HomeActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                                PendingIntent pendingIntent;
//
//                                int importance = NotificationManager.IMPORTANCE_HIGH;
//                                if (mChannel == null) {
//                                    mChannel = new NotificationChannel("0", getString()(R.string.app_name), importance);
//                                    mChannel.setDescription(getString()(R.string.msg_booking_success));
//                                    mChannel.enableVibration(true);
//                                    notifManager.createNotificationChannel(mChannel);
//                                }
//                                builder = new NotificationCompat.Builder(mActivity, "0");
//
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                                pendingIntent = PendingIntent.getActivity(mActivity, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
//                                builder.setContentTitle(getString()(R.string.app_name))
//                                        .setSmallIcon(R.mipmap.ic_launcher) // required
//                                        .setContentText(getString()(R.string.msg_booking_success))  // required
//                                        .setDefaults(Notification.DEFAULT_ALL)
//                                        .setAutoCancel(true)
//                                        .setLargeIcon(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher))
//                                        .setBadgeIconType(R.mipmap.ic_launcher)
//                                        .setContentIntent(pendingIntent)
//                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//                                Notification notification = builder.build();
//                                notifManager.notify(0, notification);
//                            }

                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(Constants.INTENT_KEY, Constants.PUSH_NOTIFICATION);

                            NotificationUtils notificationUtils = new NotificationUtils(mActivity);
                            notificationUtils.showNotificationMessage(NotificationType.BOOKING_INDIVIDUAL_NEW, getString(R.string.app_name), getString(R.string.msg_booking_individual_success), DateTimeUtils.getStandardCurrentDatetime(), intent);

//                            String title = getString()(R.string.notification_new_booking_title);
//                            String message = mUser.getUserFullName() + " has just make a booking on " + booking.playground.name + " at " + DateTimeUtils.getDatetime();
                            FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_INDIVIDUAL_NEW, "", "", mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);

                            if (totalInvitees == booking.size) {
                                FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_INDIVIDUAL_COMPLETED, "", String.format(getString(R.string.msg_individual_booking_completed), ageCategory.name, mPlayground.name), mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);
                            }

                            YoYo.with(Techniques.RotateInDownRight)
                                    .duration(300)
                                    .withListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            ln_result.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            nsc_summary.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    }).playOn(ln_result);
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AppLogger.w(" onComplete");

                            summary_btn_bookNow.setAlpha(1f);
                            summary_btn_bookNow.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AppLogger.e(" Error -> " + e.getLocalizedMessage());

                            img_status.setImageResource(R.drawable.icon_failed);
                            txt_status.setText("Sorry, an error has occurred\n please try again!");

                            YoYo.with(Techniques.RotateInDownRight)
                                    .duration(300)
                                    .withListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            ln_result.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            nsc_summary.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    }).playOn(ln_result);
                        }
                    });

        } else {
            booking.playground = new BookingPlayground();
            booking.playground.playgroundId = mPlayground.playgroundId;
            booking.playground.ownerId = mPlayground.ownerId;
            booking.playground.name = mPlayground.name;
            booking.playground.address_city = mPlayground.address_city;
            booking.playground.address_direction = mPlayground.address_direction;
            booking.playground.address_region = mPlayground.address_region;
            booking.playground.images = mPlayground.images;

            booking.ownerId = mPlayground.ownerId;
            booking.playgroundId = mPlayground.playgroundId;

            booking.userId = mUser.uId;

            booking.user = new BookingPlayer();
            booking.user.uId = mUser.uId;
            booking.user.appUserId = mUser.appUserId;
            booking.user.name = mUser.getUserFullName();
            booking.user.email = mUser.email;
            booking.user.mobileNo = mUser.mobileNo;
            booking.user.profileImageUrl = mUser.profileImageUrl;

            booking.status = BookingStatus.PENDING;

            booking.isIndividuals = false;

            booking.ownerId_status = mPlayground.ownerId + "_" + BookingStatus.PENDING;
            booking.playgroundId_date = mPlayground.playgroundId + "_" + DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH);
            booking.playgroundId_datetime_timeStart = mPlayground.playgroundId + "_" + booking.timeStart;

            mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
            mDatabaseBookings.orderByChild("playgroundId_datetime_timeStart").equalTo(booking.playgroundId_datetime_timeStart)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                showMessage("Another booking has been made on same time!");

                                summary_btn_bookNow.setAlpha(1f);
                                summary_btn_bookNow.setEnabled(true);

                            } else {
                                booking.bookingUId = mDatabaseBookings.child(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE).push().getKey();

                                mDatabaseBookings.child(booking.bookingUId)
                                        .setValue(booking)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                AppLogger.w(" onSuccess");

//                                            getBookings(mPlayground.playgroundId, mCalendarDay.dayName + " " + mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year);
//                                            getBookings(mPlayground.playgroundId, mCalendarDay.day + " " + mCalendarDay.monthName + " " + mCalendarDay.year);
                                                getBookings(mPlayground.playgroundId, DateTimeUtils.getDatetime(mCalendarDay.date.getTimeInMillis(), DateTimeUtils.PATTERN_DATE, Locale.ENGLISH));

                                                img_status.setImageResource(R.drawable.icon_success);
                                                txt_status.setText(getString(R.string.msg_booking_success));

                                                Intent intent = new Intent(mActivity, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra(Constants.INTENT_KEY, Constants.PUSH_NOTIFICATION);

                                                NotificationUtils notificationUtils = new NotificationUtils(mActivity);
                                                notificationUtils.showNotificationMessage(NotificationType.BOOKING_FULL_NEW, getString(R.string.app_name), getString(R.string.msg_booking_success), DateTimeUtils.getStandardCurrentDatetime(), intent);

                                                FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_FULL_NEW, "", "", mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);

                                                YoYo.with(Techniques.RotateInDownRight)
                                                        .duration(300)
                                                        .withListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animation) {
                                                                ln_result.setVisibility(View.VISIBLE);
                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                nsc_summary.setVisibility(View.GONE);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animation) {

                                                            }
                                                        }).playOn(ln_result);
                                            }
                                        })
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                AppLogger.w(" onComplete");

                                                summary_btn_bookNow.setAlpha(1f);
                                                summary_btn_bookNow.setEnabled(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                AppLogger.e(" Error -> " + e.getLocalizedMessage());

                                                img_status.setImageResource(R.drawable.icon_failed);
                                                txt_status.setText("Sorry, an error has occurred\n please try again!");

                                                YoYo.with(Techniques.RotateInDownRight)
                                                        .duration(300)
                                                        .withListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animation) {
                                                                ln_result.setVisibility(View.VISIBLE);
                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                nsc_summary.setVisibility(View.GONE);
                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animation) {

                                                            }
                                                        }).playOn(ln_result);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            AppLogger.e("MakeBooking() Error -> " + databaseError.toException());

                            summary_btn_bookNow.setAlpha(1f);
                            summary_btn_bookNow.setEnabled(true);
                        }
                    });
        }
    }

    private void addIndividualBookingToUser(String userUid, Booking booking) {
        UserBooking userBooking = new UserBooking();
        userBooking.bookingUId = booking.bookingUId;
        userBooking.userId = userUid;

        userBooking.playgroundId = mPlayground.playgroundId;
        userBooking.playground = new BookingPlayground();
        userBooking.playground.playgroundId = mPlayground.playgroundId;
        userBooking.playground.ownerId = mPlayground.ownerId;
        userBooking.playground.name = mPlayground.name;
        userBooking.playground.address_city = mPlayground.address_city;
        userBooking.playground.address_direction = mPlayground.address_direction;
        userBooking.playground.address_region = mPlayground.address_region;
        userBooking.playground.images = mPlayground.images;

        userBooking.datetimeCreated = booking.datetimeCreated;
        userBooking.timeStart = booking.timeStart;
        userBooking.timeEnd = booking.timeEnd;

        userBooking.size = booking.size;
        userBooking.duration = booking.duration;
        userBooking.price = booking.price;

        userBooking.isIndividuals = booking.isIndividuals;
        userBooking.priceIndividual = booking.priceIndividual;

        userBooking.invitees = invitees;
        userBooking.ageCategories = booking.ageCategories;

        userBooking.status = booking.status;

        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE);
        mDatabaseBookings.child(userUid)
                .child(booking.bookingUId)
                .setValue(userBooking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e(" Error -> " + e.getLocalizedMessage());
                    }
                });
    }

    private void cancelIndividualBooking() {
        boolean isFound = false;

        if (mPlayground.booking != null) {
            if (!ListUtils.isEmpty(mPlayground.booking.ageCategories)) {
                for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
                    if (!ListUtils.isEmpty(category.players)) {
                        for (BookingPlayer player : category.players) {
                            if (player.uId.equals(mUser.uId)) {
                                category.players.remove(player);
                                isFound = true;
                                break;
                            }
                        }
                    }

                    if (isFound) {
                        break;
                    }
                }
            }
        }

        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE).child(booking.bookingUId);
        mDatabaseBookings.child("ageCategories")
                .setValue(booking.ageCategories)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");

                        cancelUserBooking(booking.bookingUId);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e(" Error -> " + e.getLocalizedMessage());
                    }
                });
    }

    private void cancelUserBooking(String bookingUId) {
        booking.status = BookingStatus.USER_CANCELLED;

        mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE).child(mUser.uId);
        mDatabaseBookings
                .child(bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");

                        EventBus.getDefault().post(new OnEventRefresh());

                        btn_continue.setClickable(true);
                        btn_continue.setAlpha(1);
                        btn_continue.setTextSize(16);
                        btn_continue.setText(getString(R.string.join));
                        btn_continue.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                        btn_continue.setVisibility(View.VISIBLE);

                        hide();

                        Intent intent = new Intent(mActivity, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(Constants.INTENT_KEY, Constants.PUSH_NOTIFICATION);

                        NotificationUtils notificationUtils = new NotificationUtils(mActivity);
                        if(booking.isIndividuals){
                            FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_USER_CANCELLED_individual, "", "", mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);
                            notificationUtils.showNotificationMessage(NotificationType.BOOKING_USER_CANCELLED_individual, getString(R.string.app_name), getString(R.string.msg_booking_individual_cancel_success), DateTimeUtils.getStandardCurrentDatetime(), intent);

                        }else {
                            FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_USER_CANCELLED_individual, "", "", mUser.uId, mUser.getUserFullName(), mUser.profileImageUrl);
                            notificationUtils.showNotificationMessage(NotificationType.BOOKING_USER_CANCELLED_individual, getString(R.string.app_name), getString(R.string.msg_booking_individual_cancel_success), DateTimeUtils.getStandardCurrentDatetime(), intent);

                        }

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w(" onComplete");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e(" Error -> " + e.getLocalizedMessage());
                    }
                });
    }
    private void zoomIn(LinearLayout linearLayout) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(linearLayout,
                "scaleX", 1.15f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(linearLayout,
                "scaleY", 1.15f);
        scaleDownX.setDuration(1000);
        scaleDownY.setDuration(1000);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.start();
    }
    private void zoomOut(LinearLayout linearLayout) {
        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                linearLayout, "scaleX", 1f);
        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                linearLayout, "scaleY", 1f);
        scaleDownX2.setDuration(1000);
        scaleDownY2.setDuration(1000);
        AnimatorSet scaleDown2 = new AnimatorSet();
        scaleDown2.play(scaleDownX2).with(scaleDownY2);
        scaleDown2.start();
    }

    public void setFavourite(boolean isFavourite) {
        mPlayground.isFavourite = isFavourite;

        if (isFavourite) {
            btn_favourite.setImageResource(R.drawable.icon_star_filled);
        } else {
            btn_favourite.setImageResource(R.drawable.icon_star_empty);
        }
    }

    private String getInvitees(int player) {
        switch (player) {
            case 0:
                return getString(R.string.no_player);

            case 1:
                return getString(R.string.one_player);

            case 2:
                return getString(R.string.two_player);

            case 3:
                return getString(R.string.three_player);

            case 4:
                return getString(R.string.four_player);

            case 5:
                return getString(R.string.five_player);

            default:
                return getString(R.string.no_player);
        }
    }


    private void disableContinueButton() {
        btn_continue.setClickable(false);
        btn_continue.setAlpha(.3f);
        btn_continue.setText(getString(R.string.msg_user_need_login));
        btn_continue.setTextSize(16);
        btn_continue.setVisibility(View.VISIBLE);
    }

    private void disableFavouriteButton() {
        btn_favourite.setVisibility(View.GONE);
        txt_favourite.setVisibility(View.GONE);
    }


    public void showUserBookingInfo(final Booking booking, @NonNull final User user) {
        isShowUserBooking = true;

        mUser = user;

        DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabasePlaygrounds
                .child(booking.playgroundId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                            showMessage(R.string.error_no_connection);
                            return;
                        }

                        if (dataSnapshot != null && dataSnapshot.exists()) {

                            Playground playground = dataSnapshot.getValue(Playground.class);
                            if (playground != null) {
                                playground.isIndividuals = booking.isIndividuals;
                                playground.booking = booking;

                                show(playground, user);

                            } else {
                                showMessage(R.string.error_no_data);
                            }
                        } else {
                            showMessage(R.string.error_no_data);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());
                        showMessage(R.string.error_no_data);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void show(Playground playground, User user) {
        mPlayground = playground;
        mUser = user;

//        if (isShowUserBooking) {
//            show();
//            return;
//        }

        if (mUser == null) {
            disableContinueButton();
            disableFavouriteButton();

        } else {
            if (mPlayground.booking != null) {
                if (!ListUtils.isEmpty(mPlayground.booking.ageCategories)) {
                    for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
                        if (!ListUtils.isEmpty(category.players)) {
                            for (BookingPlayer player : category.players) {
                                if (player.uId.equals(mUser.uId)) {
                                    isParticipated = true;

                                    if (category.ageRangeStart == 8) {
                                        selectYoung();

                                    } else if (category.ageRangeStart == 13) {
                                        selectMiddle();

                                    } else if (category.ageRangeStart == 18) {
                                        selectOld();

                                    }

                                    txt_peopleNumber.setText(getInvitees(player.invitees));
                                    break;
                                }
                            }
                        }

                        if (isParticipated) {
                            break;
                        }
                    }
                }
            }
        }

        show();
    }


    private boolean isShown() {
        return mBottomSheetBehavior != null && mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    public void show() {
        if (!isShown()) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void hide() {
//        if (isShown()) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
    }


    private void hideSummary() {
        YoYo.with(Techniques.RotateOutUpLeft)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsc_content.setVisibility(View.VISIBLE);
                        btn_continue.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        nsc_summary.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(nsc_summary);
    }

    private void hideResult() {
        YoYo.with(Techniques.RotateOutUpRight)
                .duration(300)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsc_content.setVisibility(View.VISIBLE);
                        btn_continue.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ln_result.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(ln_result);
    }


    public void showMessage(@StringRes int resId) {
        Toast.makeText(mActivity, mActivity.getString(resId), Toast.LENGTH_LONG).show();
    }

    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        }
    }

    public String getString(@StringRes int resId) {
        return mActivity.getString(resId);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity().getClass() != mActivity.getClass()) {
            return;
        }

        if (event.getItem() instanceof SizeListItem) {
            sizeListItem = (SizeListItem) event.getItem();
            if (event.getAction() == ItemAction.PICK) {
                txt_size.setText(String.format(Locale.ENGLISH, "%s", sizeListItem.name));
            }

        } else if (event.getItem() instanceof DurationListItem) {
            durationListItem = (DurationListItem) event.getItem();
            if (event.getAction() == ItemAction.PICK) {
                txt_duration.setText(String.format(Locale.ENGLISH, "%s", durationListItem.name));
            }

        } else if (event.getItem() instanceof GenericListItem) {

            if (mPlayground.isIndividuals) {
Log.d("ttt","mPlayground.isIndividual");
                invitees = Integer.valueOf(((GenericListItem) event.getItem()).value);
                txt_peopleNumber.setText((getString(R.string.players)));

                int youngPlayers = 0, middlePlayers = 0, oldPlayers = 0;

                if (!ListUtils.isEmpty(mPlayground.booking.ageCategories)) {

                    if (chk_young.isChecked()) {

                        for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
                            if (category.ageRangeStart == 8) {
                                if (!ListUtils.isEmpty(category.players)) {
                                    youngPlayers = category.players.size();
                                    for (BookingPlayer player : category.players) {
                                        youngPlayers += player.invitees;
                                    }

                                    if ((1 + invitees) > (mPlayground.booking.size - youngPlayers)) {
                                        invitees = 0;
                                        showMessage(String.format(getString(R.string.msg_players_number_exceed), mPlayground.booking.size - youngPlayers));
                                    }
                                }
                            }
                        }
                    }

                    if (chk_middle.isChecked()) {
                        for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
                            if (category.ageRangeStart == 13) {
                                if (!ListUtils.isEmpty(category.players)) {
                                    middlePlayers = category.players.size();
                                    for (BookingPlayer player : category.players) {
                                        middlePlayers += player.invitees;
                                    }

                                    if ((1 + invitees) > (mPlayground.booking.size - middlePlayers)) {
                                        invitees = 0;
                                        showMessage(String.format(getString(R.string.msg_players_number_exceed), mPlayground.booking.size - middlePlayers));
                                    }
                                }
                            }
                        }
                    }

                    if (chk_old.isChecked()) {
                        for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
                            if (category.ageRangeStart == 18) {
                                if (!ListUtils.isEmpty(category.players)) {
                                    oldPlayers = category.players.size();
                                    for (BookingPlayer player : category.players) {
                                        oldPlayers += player.invitees;
                                    }

                                    if ((1 + invitees) > (mPlayground.booking.size - oldPlayers)) {
                                        invitees = 0;
                                        showMessage(String.format(getString(R.string.msg_players_number_exceed), mPlayground.booking.size - oldPlayers));
                                    }
                                }
                            }
                        }
                    }

//                    for (BookingAgeCategory category : mPlayground.booking.ageCategories) {
//                        if (category.ageRangeStart == 8) {
//                            if (!ListUtils.isEmpty(category.players)) {
//                                youngPlayers = category.players.size();
//                                for (BookingPlayer player : category.players) {
//                                    youngPlayers += player.invitees;
//                                }
//
//                                if ((1 + invitees) > (mPlayground.booking.size - youngPlayers)) {
//                                    invitees = 0;
//                                    showMessage(String.format(getString(R.string.msg_players_number_exceed), mPlayground.booking.size - youngPlayers));
//                                }
//                            }
//                        } else if (category.ageRangeStart == 13) {
//                            if (!ListUtils.isEmpty(category.players)) {
//                                middlePlayers = category.players.size();
//                                for (BookingPlayer player : category.players) {
//                                    middlePlayers += player.invitees;
//                                }
//
//                                if ((1 + invitees) > (mPlayground.booking.size - middlePlayers)) {
//                                    invitees = 0;
//                                    showMessage(String.format(getString(R.string.msg_players_number_exceed), mPlayground.booking.size - middlePlayers));
//                                }
//                            }
//                        } else if (category.ageRangeStart == 18) {
//                            if (!ListUtils.isEmpty(category.players)) {
//                                oldPlayers = category.players.size();
//                                for (BookingPlayer player : category.players) {
//                                    oldPlayers += player.invitees;
//                                }
//
//                                if ((1 + invitees) > (mPlayground.booking.size - oldPlayers)) {
//                                    invitees = 0;
//                                    showMessage(String.format(getString(R.string.msg_players_number_exceed), mPlayground.booking.size - oldPlayers));
//                                }
//                            }
//                        }
//                    }
                }

                if (invitees > 0) {
                    txt_peopleNumber.setText(((GenericListItem) event.getItem()).name);
                }
            }

        }

        mDialogList.dismiss();
    }


    public boolean onBackPressed() {
        if (ln_result.getVisibility() == View.VISIBLE) {
            hideResult();
            return true;
        }

        if (nsc_summary.getVisibility() == View.VISIBLE) {
            hideSummary();
            return true;
        }

        if (isShown()) {
            hide();
            return true;
        }

        return false;
    }

    public void onDetach() {
        EventBus.getDefault().unregister(this);
        mBottomSheetListener = null;
        mActivity = null;
    }


    class PlaygroundImagesAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<String> mImagesUrls;


        PlaygroundImagesAdapter(Context context, List<String> urls) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImagesUrls = urls;
        }


        public void clear() {
            mImagesUrls.clear();
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mImagesUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.playground_image, container, false);

            final ImageView img_View = itemView.findViewById(R.id.img_image);

            Glide.with(mContext).load(mImagesUrls.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.img_logo_white)
                    .into(img_View);

            img_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ProductImageBanner imageBanner = getItem(position);
//                    imageBanner.imageView = img_banner;
//
//                    EventBus.getDefault().post(new OnEventItemClicked<>(imageBanner, ItemAction.DETAILS, position));
                }
            });

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public class CubeInRotationTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            page.setCameraDistance(20000);


            if (position < -1) {     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            } else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(90 * Math.abs(position));

            } else if (position <= 1) {    // (0,1]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(-90 * Math.abs(position));

            } else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }
        }
    }

}
