package com.malaab.ya.action.actionyamalaab.ui.account.bookings.upcoming;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.annotations.BookingType;
import com.malaab.ya.action.actionyamalaab.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.annotations.FineType;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingAgeCategory;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.data.network.model.UserBooking;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class UpcomingBookingsPresenter<V extends UpcomingBookingsMvpView>
        extends BasePresenter<V>
        implements UpcomingBookingsMvpPresenter<V> {

    private DatabaseReference mDatabase;
    //    private ValueEventListener mValueEventListener;


    @Inject
    public UpcomingBookingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - My Upcoming Bookings Screen");
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
    public void getUpcomingFullBookings(final String userUid) {
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
                            List<Booking> upcomingBookings = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null) {
                                    if (DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                        Log.d("ttt",booking.status+"");

                                        upcomingBookings.add(booking);
                                    }
                                }
                            }

                            Collections.reverse(upcomingBookings);

                            getMvpView().onGetUpcomingFullBookings(upcomingBookings);

                        } else {
//                            getMvpView().onError(R.string.error_no_data);
                            getMvpView().onGetUpcomingFullBookings(new ArrayList<Booking>());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onGetUpcomingFullBookings(new ArrayList<Booking>());
                    }
                });

//        mDatabase.child(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE)
//                .child(userUid)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//
//                            final Booking booking;
//                            final List<Booking> upcomingBookings = new ArrayList<>();
//                            List<Booking> pastBookings = new ArrayList<>();
//
//                            for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                String key = child.getKey();
//
//                                mDatabase.child(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE)
//                                        .child(key)
//                                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                if (dataSnapshot.exists()) {
//
//                                                    Booking booking = dataSnapshot.getValue(Booking.class);
//                                                    if (booking != null) {
//                                                        upcomingBookings.add(booking);
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


//        /* To load the list once only*/
//        mValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//
//
//                Booking booking;
//                List<Booking> upcomingBookings = new ArrayList<>();
//                List<Booking> pastBookings = new ArrayList<>();
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    booking = child.getValue(Booking.class);
//
//                    if (booking != null) {
//                        if (DateTimeUtils.isDateAfterCurrentDate(DateTimeUtils.changeDateFormat(booking.datetimeCreated, DateTimeUtils.PATTERN_DATE_1, DateTimeUtils.PATTERN_DATETIME_DEFAULT))) {
//                            upcomingBookings.add(booking);
//                        } else {
//                            pastBookings.add(booking);
//                        }
//                    }
//                }
//
//                if (!isViewAttached()) {
//                    return;
//                }
//
//                getMvpView().hideLoading();
//
//                getMvpView().onGetUpcomingBookings(upcomingBookings);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                AppLogger.e(" Error -> " + error.toException());
//
//                if (!isViewAttached()) {
//                    return;
//                }
//
//                getMvpView().hideLoading();
//            }
//        };
//
//        mDatabase.addListenerForSingleValueEvent(mValueEventListener);
    }

    @Override
    public void getUpcomingIndividualBookings(String userUid, final List<Booking> bookings) {
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
                                    if (DateTimeUtils.isDateAfterCurrentDate(userBooking.timeStart)) {
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
                                            if (!ListUtils.isEmpty(userBooking.ageCategories)) {
                                                booking.ageCategories = userBooking.ageCategories;
                                            }
                                        }

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
                            getMvpView().onGetUpcomingIndividualBookings(bookings);
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


    @Override
    public void cancelFullBooking(final Booking booking, final User user, boolean hasFine) {
        booking.hasFine = hasFine;
        booking.status = BookingStatus.USER_CANCELLED;

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_TABLE);
        mDatabase.child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
if(booking.isIndividuals){
    FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_USER_CANCELLED_individual, "", "", user.uId, user.getUserFullName(), user.profileImageUrl);

}else {
    FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_USER_CANCELLED, "", "", user.uId, user.getUserFullName(), user.profileImageUrl);

}

                        if (!isViewAttached()) {
                            return;
                        }

                        booking.status = BookingStatus.USER_CANCELLED;

                        getMvpView().onBookingCancelledSuccess(booking);
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

    @Override
    public void cancelIndividualBooking(final Booking booking, final User user) {
        boolean isFound = false;

        if (!ListUtils.isEmpty(booking.ageCategories)) {
            for (BookingAgeCategory category : booking.ageCategories) {
                if (!ListUtils.isEmpty(category.players)) {
                    for (BookingPlayer player : category.players) {
                        if (player.uId.equals(user.uId)) {
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

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE).child(booking.bookingUId);
        mDatabase
                .child("ageCategories")
                .setValue(booking.ageCategories)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");

                        cancelUserBooking(booking, user);
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

    @Override
    public void createFine(final Booking booking, User user,int x) {
        Fine fine = new Fine();
        fine.userId = user.uId;
        fine.bookingId = booking.bookingUId;
        fine.playgroundId = booking.playgroundId;
        fine.playground = booking.playground;
        fine.fineType = FineType.ATTENDANCE;
        fine.datetimeCreated = DateTimeUtils.getCurrentDatetime();
        fine.timeStart = booking.timeStart;
        fine.timeEnd = booking.timeEnd;
        fine.fineStatus = FineStatus.NOT_PAID;
        if(x==0) {
            fine.amount = 0;
        }
        if (booking.isIndividuals) {
            fine.bookType = BookingType.INDIVIDUAL;
        } else {
            fine.bookType = BookingType.FULL;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_FINES_TABLE).child(user.uId).push();
        mDatabase
                .setValue(fine)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w(" onSuccess");

                        getMvpView().onFineCreateSuccess(booking);
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


    private void cancelUserBooking(final Booking booking, final User user) {
        booking.status = BookingStatus.USER_CANCELLED;

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_BOOKINGS_TABLE).child(user.uId);
        mDatabase
                .child(booking.bookingUId)
                .setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(booking.isIndividuals){
                            FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_USER_CANCELLED_individual, "", "", user.uId, user.getUserFullName(), user.profileImageUrl);

                        }else {
                            FirebaseUtils.sendNotificationToAdmin(NotificationType.BOOKING_USER_CANCELLED, "", "", user.uId, user.getUserFullName(), user.profileImageUrl);

                        }
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().onBookingCancelledSuccess(booking);
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
}
