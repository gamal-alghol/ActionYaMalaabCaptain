package com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.custom.DialogConfirmation;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseFragment;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.malaab.ya.action.actionyamalaab.utils.Constants.BOOKING_CANCELLATION_ALLOWED_MINUTES;


public class UpcomingBookingsFragment extends BaseFragment implements UpcomingBookingsMvpView {

    @BindView(R.id.shimmer_view_container)
    public ShimmerFrameLayout pBar_loading;

    @BindView(R.id.swrl_full)
    public SwipeRefreshLayout swrl_full;

    @BindView(R.id.rv_individual)
    public RecyclerView rv_individual;

    @Inject
    DialogConfirmation mDialogConfirmation;

    @Inject
    UpcomingBookingsMvpPresenter<UpcomingBookingsMvpView> mPresenter;

    private BookingsAdapter adapter;

    private User mUser;
    private boolean mUserVisibleHint = true;


    public UpcomingBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        mUserVisibleHint = visible;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_individual, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, rootView));
            mPresenter.onAttach(this);
        }

        return rootView;
    }


    @Override
    protected void initUI() {
        swrl_full.setEnabled(false);

        rv_individual.setHasFixedSize(true);
        rv_individual.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
    public void onGetCurrentUser(User user) {
        mUser = user;
        mPresenter.getUpcomingFullBookings(mUser.uId);
    }


    @Override
    public void onGetUpcomingFullBookings(List<Booking> bookings) {
        mPresenter.getUpcomingIndividualBookings(mUser.uId, bookings);
    }

    @Override
    public void onGetUpcomingIndividualBookings(List<Booking> bookings) {
        adapter = new BookingsAdapter(bookings);
        rv_individual.setAdapter(adapter);

        rv_individual.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBookingCancelledSuccess(Booking booking) {
        if (adapter != null) {
            adapter.updateItem(booking, true);
        }

        EventBus.getDefault().post(new OnEventRefresh());
    }


    @Override
    public void onFineCreateSuccess(Booking booking) {

    }


    @Override
    public void onError(String message) {
        if (mUserVisibleHint) {
            super.onError(message);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(final OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof BookingsActivity) {

            final Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof Booking) {
                if (action == ItemAction.BOOKING_CANCEL) {

                    mDialogConfirmation
                            .withTitle(getString(R.string.dialog_booking_cancel_title))
                            .withMessage(getString(R.string.dialog_booking_cancel_msg))
                            .withPositiveButton(getString(R.string.yes))
                            .withNegativeButton(getString(R.string.no))
                            .setOnDialogConfirmationListener(new DialogConfirmation.OnDialogConfirmationListener() {
                                @Override
                                public void onPositiveButtonClick() {

                                    if (DateTimeUtils.getDifferenceInMinutes(new Date(), new Date(((Booking) event.getItem()).timeStart)) <= BOOKING_CANCELLATION_ALLOWED_MINUTES) {

                                        mDialogConfirmation
                                                .withTitle(getString(R.string.dialog_booking_fine_title))
                                                .withMessage(getString(R.string.dialog_booking_fine_msg))
                                                .withPositiveButton(getString(R.string.yes))
                                                .withNegativeButton(getString(R.string.no))
                                                .setOnDialogConfirmationListener(new DialogConfirmation.OnDialogConfirmationListener() {
                                                    @Override
                                                    public void onPositiveButtonClick() {
                                                        if (((Booking) item).isIndividuals) {
                                                            mPresenter.cancelIndividualBooking((Booking) item, mUser);
                                                        } else {
                                                            mPresenter.cancelFullBooking((Booking) item, mUser, true);
                                                        }

                                                        mPresenter.createFine((Booking) item, mUser,0);
                                                    }

                                                    @Override
                                                    public void onNegativeButtonClick() {
                                                    }
                                                })
                                                .build()
                                                .show();
                                    } else {
                                        if (((Booking) item).isIndividuals) {
                                            mPresenter.cancelIndividualBooking((Booking) item, mUser);
                                        } else {
                                            mPresenter.cancelFullBooking((Booking) item, mUser, false);
                                        }
                                    }
                                }

                                @Override
                                public void onNegativeButtonClick() {
                                }
                            })
                            .build()
                            .show();
                }
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideLoading();
        mPresenter.getCurrentUserLocal();
        hideLoading();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mPresenter.onDetach();
        super.onDetach();
    }
}