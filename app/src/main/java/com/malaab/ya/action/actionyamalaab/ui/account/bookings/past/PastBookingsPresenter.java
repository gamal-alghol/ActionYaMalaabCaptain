package com.malaab.ya.action.actionyamalaab.ui.account.bookings.past;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.UserBooking;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class PastBookingsPresenter<V extends PastBookingsMvpView>
        extends BasePresenter<V>
        implements PastBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public PastBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - My Past Bookings Screen");
    }


    @Override
    public void getCurrentUserLocal() {
        if (!isViewAttached()) {
            return;
        }

        if (getUserDetails() != null && getUserDetails().loggedInMode != LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {
            getMvpView().onGetCurrentUser(getUserDetails());
            return;
        }

        getMvpView().showMessage(R.string.msg_user_not_login);
    }

    @Override
    public void getPastFullBookings(String userUid) {
        getMvpView().showLoading();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
        mDatabase.orderByChild("userId")
                .equalTo(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        if (dataSnapshot.exists()) {
                            Booking booking;
                            List<Booking> pastBookings = new ArrayList<>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);
                                if (booking != null) {
                                    if (!DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                        booking.isPast = true;
                                        pastBookings.add(booking);
                                    }
                                }
                            }
                             //Collections.reverse(pastBookings);
                            getMvpView().onGetPastFullBookings(pastBookings);
                        } else {
//                            getMvpView().onError(R.string.error_no_data);
                            getMvpView().onGetPastFullBookings(new ArrayList<Booking>());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onGetPastFullBookings(new ArrayList<Booking>());
                    }
                });
    }

    @Override
    public void getPastIndividualBookings(String userUid, final List<Booking> bookings) {
        Log.d("ttt","getPastIndividualBookings");

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE);
        mDatabase.child(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (dataSnapshot.exists()) {

                            UserBooking userBooking;
                            Booking booking;

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                userBooking = child.getValue(UserBooking.class);

                                if (userBooking != null) {
                                    if (!DateTimeUtils.isDateAfterCurrentDate(userBooking.timeStart)) {
                                        booking = new Booking();
                                        booking.bookingUId = userBooking.bookingUId;
                                        booking.userId = userBooking.userId;
                                        booking.playgroundId = userBooking.playgroundId;
                                        booking.playground = userBooking.playground;
                                        booking.datetimeCreated = userBooking.datetimeCreated;
                                        booking.timeStart = userBooking.timeStart;
                                        booking.timeEnd = userBooking.timeEnd;
                                        booking.size = userBooking.size;
                                        booking.duration = userBooking.duration;
                                        booking.price = userBooking.price;
                                        booking.isIndividuals = userBooking.isIndividuals;
                                        booking.priceIndividual = userBooking.priceIndividual;
                                        booking.status = userBooking.status;

                                        if (booking.isIndividuals) {
                                            booking.invitees = userBooking.invitees;
                                            booking.ageCategories = new ArrayList<>();
                                            if (ListUtils.isEmpty(userBooking.ageCategories)) {
                                                booking.ageCategories = userBooking.ageCategories;
                                            }
                                        }

                                        booking.isPast = true;

                                        bookings.add(booking);
                                    }
                                }
                            }
                        }

                        getMvpView().hideLoading();

                        if (ListUtils.isEmpty(bookings)) {
                            getMvpView().onError(R.string.error_no_data);
                        } else {
                            ListUtils.sortBookingsList(bookings);
                            getMvpView().onGetPastIndividualBookings(bookings);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                    }
                });
    }
}
