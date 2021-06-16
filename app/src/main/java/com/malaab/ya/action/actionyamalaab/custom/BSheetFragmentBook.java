package com.malaab.ya.action.actionyamalaab.custom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.ui.adapter.DaysAdapter;
import com.malaab.ya.action.actionyamalaab.ui.adapter.TimesAdapter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class  BSheetFragmentBook extends BottomSheetDialogFragment {

    @BindView(R.id.vp_images)
    ViewPager vp_images;
    @BindView(R.id.mPageIndicatorView)
    PageIndicatorView mPageIndicatorView;

    //    @BindView(R.id.txt_month)
//    TextView txt_month;
    @BindView(R.id.rv_days)
    RecyclerView rv_days;
    @BindView(R.id.rv_times)
    RecyclerView rv_times;

    DaysAdapter daysAdapter;
    TimesAdapter timesAdapter;

    BottomSheetBehavior mBehavior;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private Playground mPlayground;


    public BSheetFragmentBook() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bsheet_book, container, false);
        ButterKnife.bind(this, v);

        vp_images.setPageTransformer(true, new CubeInRotationTransformation());

        rv_days.setHasFixedSize(true);
        rv_days.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        rv_times.setHasFixedSize(true);
        rv_times.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        final View view = getView();
        if (view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    View parent = (View) view.getParent();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                    CoordinatorLayout.Behavior behavior = params.getBehavior();
                    BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                    if (bottomSheetBehavior != null) {
                        bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
                    }

                    parent.setBackgroundColor(Color.TRANSPARENT);
                }
            });
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResume() {
        super.onResume();

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long response) {
                        setupPlaygroundData(mPlayground);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        AppLogger.e(throwable.getMessage());
                    }
                });
    }


    public void setPlayground(Playground playground) {
        mPlayground = playground;
    }

    public void setupPlaygroundData(Playground playground) {
        if (playground == null) {
            return;
        }

        if (!ListUtils.isEmpty(playground.images)) {
            PlaygroundImagesAdapter playgroundImagesAdapter = new PlaygroundImagesAdapter(getActivity(), playground.images);
            vp_images.setAdapter(playgroundImagesAdapter);

            mPageIndicatorView.setViewPager(vp_images);
        }

        Calendar startDate = Calendar.getInstance();
        String monthName = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
//        txt_month.setText(monthName);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 14);

        daysAdapter = new DaysAdapter(getDates(startDate.getTime(), endDate.getTime()));
        rv_days.setAdapter(daysAdapter);
        daysAdapter.setIsSelectedByUSer(0, true);


        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.HOUR, 10);

        timesAdapter = new TimesAdapter(getTimes(startTime.getTime(), endTime.getTime(), 30));
        rv_times.setAdapter(timesAdapter);

        timesAdapter.setIsSelectedByUSer(0, true);
    }

    public void setDaySelected(int pos) {
        daysAdapter.setIsSelectedByUSer(pos, true);
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
            dayName = startDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            monthName = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            startDayNo = startDate.get(Calendar.DAY_OF_MONTH);
            year = startDate.get(Calendar.YEAR);

//            calendarDay = new CalendarDay(monthName, startDayNo, dayName, year, false);
            calendarDay = new CalendarDay(startDate, false);
            days.add(calendarDay);

            startDate.add(Calendar.DATE, 1);
        }

        return days;
    }

    private static List<CalendarTime> getTimes(Date fromDate, Date toDate, int diff) {

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(fromDate);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate);

        Calendar diffDate = startDate;

        String timeName;
        int year;

        CalendarTime calendarTime;
        List<CalendarTime> times = new ArrayList<>();

        while (!startDate.after(endDate)) {

            timeName = DateTimeUtils.changeDateFormat(startDate.getTime(), DateTimeUtils.PATTERN_TIME);

//            calendarTime = new CalendarTime(timeName, R.drawable.icon_ball, false, true);
//            times.add(calendarTime);

            startDate.add(Calendar.MINUTE, diff);
        }

        return times;
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
