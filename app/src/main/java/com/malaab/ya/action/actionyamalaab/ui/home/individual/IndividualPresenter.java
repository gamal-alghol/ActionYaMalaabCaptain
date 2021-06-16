package com.malaab.ya.action.actionyamalaab.ui.home.individual;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class IndividualPresenter<V extends IndividualMvpView>
        extends BasePresenter<V>
        implements IndividualMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;

    //    private ChildEventListener mChildEventListener;
    private int count = 0;


    @Inject
    public IndividualPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Home ->  Individual Tab");
    }


    @Override
    public void getCurrentUser() {
        if (!isViewAttached()) {
            return;
        }

        if (getUserDetails() == null) {
            getMvpView().onUserAsGuest();
            return;
        }

        getMvpView().onGetCurrentUser(getUserDetails());
    }

    @Override
    public void getPlaygroundsForGuest() {
        count = 0;

        getMvpView().showLoading();

        final List<Playground> playgrounds = new ArrayList<>();

        DatabaseReference mDatabaseBookings = FirebaseDatabase.getInstance().
                getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
//        mDatabaseBookings.orderByChild("timeStart").startAt(DateTimeUtils.)
        mDatabaseBookings
                .orderByChild("isActive")
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (snapshot.exists()) {

                            for (DataSnapshot child : snapshot.getChildren()) {

                                final Booking booking = child.getValue(Booking.class);

                                if (booking != null) {
                                    booking.bookingUId = child.getKey();

                                    DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
                                    mDatabasePlaygrounds
                                            .child(booking.playgroundId)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    count++;

                                                    if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                                                        getMvpView().onNoInternetConnection();
                                                        return;
                                                    }

                                                    if (dataSnapshot.exists()) {
                                                        Playground playground = dataSnapshot.getValue(Playground.class);
                                                        if (playground != null) {
                                                            playground.isIndividuals = true;
                                                            playground.booking = booking;

//                                                if (!playgrounds.contains(playground)) {
                                                            if (DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                                                playgrounds.add(playground);
                                                            }
//                                                }
                                                        }

                                                        if (count >= snapshot.getChildrenCount()) {
                                                            if (playgrounds.size() == 0) {
                                                                getMvpView().onGetIndividualPlayground(playgrounds);
                                                                return;
                                                            }

                                                            getMvpView().onGetIndividualPlayground(playgrounds);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    count++;
                                                    AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                                                    if (!isViewAttached()) {
                                                        return;
                                                    }

                                                    getMvpView().hideLoading();
                                                }
                                            });
                                } else {
                                    count++;
                                }
                            }
                        } else {
                            getMvpView().onGetIndividualPlayground(playgrounds);
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
    public void getIndividualPlayground(final String userCity, double latitude, double longitude) {
        count = 0;

        getMvpView().showLoading();

        final android.location.Location userLocation = new android.location.Location("User Current location");
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);

        final ArrayList<Playground> playgrounds = new ArrayList<>();

        DatabaseReference mDatabaseBookings = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
//        mDatabaseBookings.orderByChild("timeStart").startAt(DateTimeUtils.)
        mDatabaseBookings
                .orderByChild("playground_city")
             //   .equalTo(userCity)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (snapshot.exists()) {

                            for (DataSnapshot child : snapshot.getChildren()) {

                                final Booking booking = child.getValue(Booking.class);

                                if (booking != null && booking.isActive) {
                                    booking.bookingUId = child.getKey();

                                    DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
                                    mDatabasePlaygrounds
                                            .child(booking.playgroundId)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    count++;

                                                    if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                                                        getMvpView().onNoInternetConnection();
                                                        return;
                                                    }

                                                    if (dataSnapshot.exists()) {

                                                        Playground playground = dataSnapshot.getValue(Playground.class);
                                                        if (playground != null) {
                                                            playground.isIndividuals = true;
                                                            playground.booking = booking;

//                                                if (!playgrounds.contains(playground)) {
                                                            if (DateTimeUtils.isDateAfterCurrentDate(booking.timeStart)) {
                                                                playgrounds.add(playground);
                                                            }
//                                                }
                                                        }

                                                        if (count >= snapshot.getChildrenCount()) {
                                                            if (playgrounds.size() == 0) {
                                                                getMvpView().onGetIndividualPlayground(playgrounds);
                                                                return;
                                                            }

                                                            List<Playground> list = ListUtils.sortPlaygroundsByNearestAndDatetime(playgrounds, userLocation);

                                                            getMvpView().onGetIndividualPlayground(list);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    count++;
                                                    AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                                                    if (!isViewAttached()) {
                                                        return;
                                                    }

                                                    getMvpView().hideLoading();
                                                }
                                            });
                                } else {
                                    count++;
                                }
                            }
                        } else {
                            getMvpView().onGetIndividualPlayground(playgrounds);
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

//    @Override
//    public void listenToFutsalCourtsUpdates() {
//        /* To listen to any changed in database*/
//        mChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                AppLogger.d("onChildAdded:" + dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                AppLogger.d("onChildChanged:" + dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                AppLogger.d("onChildRemoved:" + dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                AppLogger.d("onChildMoved:" + dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                AppLogger.d("onCancelled:" + databaseError.toException());
//            }
//        };
//
//        mDatabasePlaygrounds.addChildEventListener(mChildEventListener);
//    }

//    @Override
//    public void removeListeners() {
//        if (mDatabasePlaygrounds != null) {
//            if (mValueEventListener != null) {
//                mDatabasePlaygrounds.removeEventListener(mValueEventListener);
//            }
//            if (mChildEventListener != null) {
//                mDatabasePlaygrounds.removeEventListener(mChildEventListener);
//            }
//        }
//    }
}
