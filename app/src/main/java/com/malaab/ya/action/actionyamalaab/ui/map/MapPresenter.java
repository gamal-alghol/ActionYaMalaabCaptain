package com.malaab.ya.action.actionyamalaab.ui.map;

import android.location.Location;
import android.support.annotation.NonNull;

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
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
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


public class MapPresenter<V extends MapMvpView>
        extends BasePresenter<V>
        implements MapMvpPresenter<V> {

    private DatabaseReference mDatabasePlaygrounds;
    private DatabaseReference mDatabaseExercises;

    private ValueEventListener mValueEventListener;
    private int count = 0;


    @Inject
    public MapPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Map Screen");

        mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabaseExercises = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);

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
    public void updateCurrentUserLocation(String city, String region, double latitude, double longitude) {
        if (!isViewAttached()) {
            return;
        }

        getMvpView().showLoading();

        final User user = getUserDetails();
        user.address_city = city;

        if (!region.contains("منطقة")) {
            user.address_region = "منطقة " + region;
        } else {
            user.address_region = region;
        }

        user.latitude = latitude;
        user.longitude = longitude;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(user.uId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().setCurrentUser(user);

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.msg_success);

                        getMvpView().onUpdateCurrentUserLocation();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.e(" Error -> " + e.getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void getPlaygrounds(final Location userCurrentLocation) {
        if (!isViewAttached()) {
            return;
        }

        getMvpView().showLoading();

        /* To load the list once only*/
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Playground playground;
                List<Playground> playgrounds = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    playground = child.getValue(Playground.class);
                    if (playground != null&&!playground.isHide) {
                        playground.playgroundId = child.getKey();
                        playgrounds.add(playground);
                      //  if (isPlaygroundInRangeOf2KM(userCurrentLocation, playground)) {

                     //   }
                    }
                }

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();
                getMvpView().onGetPlayground(playgrounds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppLogger.e(" Error -> " + error.toException());

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();
            }
        };

        mDatabasePlaygrounds.addListenerForSingleValueEvent(mValueEventListener);
    }

    @Override
    public void getExercises(final Location userCurrentLocation) {
        if (!isViewAttached()) {
            return;
        }
        count = 0;

        getMvpView().showLoading();


        final ArrayList<Playground> playgrounds = new ArrayList<>();

//        mDatabaseBookings.orderByChild("timeStart").startAt(DateTimeUtils.)
        mDatabaseExercises.orderByChild("playground_city")
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

                                                /*    if (!NetworkUtils.isNetworkConnected(this))) {
                                                        getMvpView().onNoInternetConnection();
                                                        return;
                                                    }*/

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
                                                                getMvpView().onGetPlayground(playgrounds);
                                                                return;
                                                            }

                                                            List<Playground> list = ListUtils.sortPlaygroundsByNearestAndDatetime(playgrounds,userCurrentLocation);

                                                            getMvpView().onGetPlayground(list);
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
                            getMvpView().onGetPlayground(playgrounds);
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
/*
        getMvpView().showLoading();

        // To load the list once only
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Booking booking;
                List<Booking> bookings = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    booking = child.getValue(Booking.class);
                    if (booking != null) {

                        booking.bookingUId = child.getKey();
                        bookings.add(booking);
                        //  if (isPlaygroundInRangeOf2KM(userCurrentLocation, playground)) {

                        //   }
                    }
                }

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();
                getMvpView().onGetExercises(bookings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppLogger.e(" Error -> " + error.toException());

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();
            }
        };

        mDatabaseExercises.addListenerForSingleValueEvent(mValueEventListener);
*/
    }

    @Override
    public void getNearestPlayground(List<Playground> playgrounds, final Location userCurrentLocation) {
        if (!isViewAttached()) {
            return;
        }

        if (playgrounds.size() == 0) {
            return;
        }

        List<Playground> list = ListUtils.sortPlaygroundsByNearest((ArrayList<Playground>) playgrounds, userCurrentLocation);
        if (list.size() > 0) {
            getMvpView().onGetNearestPlayground(list.get(0), userCurrentLocation);
        }
    }


    private boolean isPlaygroundInRangeOf2KM(Location userCurrentLocation, Playground playground) {
        float[] dist = new float[1];

        Location.distanceBetween(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude(), playground.latitude, playground.longitude, dist);

        AppLogger.d(playground.name + "\n" + "Distance = " + (dist[0] / 1000) + " KM");

        //here your code or alert box for outside 1Km radius area
        return dist[0] / 1000 <= 2;
    }


    @Override
    public void isPlaygroundInFavouriteList(Playground playground) {
        String userUid = getDataManager().getCurrentUser().uId;
        getMvpView().onPlaygroundInFavouriteList(getDataManager().isPlaygroundInFavouriteList(userUid, playground));
    }

    @Override
    public void addPlaygroundToFavouriteList(Playground playground) {
        String userUid = getDataManager().getCurrentUser().uId;
        getDataManager().addPlaygroundToFavouriteList(userUid, playground);

        getMvpView().onAddPlaygroundToFavouriteList(true);
    }

    @Override
    public void removePlaygroundFromFavouriteList(Playground playground) {
        String userUid = getDataManager().getCurrentUser().uId;
        getDataManager().removePlaygroundTFromFavouriteList(userUid, playground);

        getMvpView().onRemovePlaygroundFromFavouriteList(true);
    }
}
