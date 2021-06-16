package com.malaab.ya.action.actionyamalaab.ui.home.full;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class    FullPresenter<V extends FullMvpView>
        extends BasePresenter<V>
        implements FullMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;

    //    private ChildEventListener mChildEventListener;


    @Inject
    public FullPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Home -> Full Bookings Tab");
    }


    @Override
    public void getCurrentUser() {
        if (!isViewAttached()) {
            return;
        }

        if (getUserDetails() == null) {
            getMvpView().onError(R.string.error_user_guest);
            getMvpView().onUserAsGuest();
            return;
        }

        getMvpView().onGetCurrentUser(getUserDetails());
    }

    @Override
    public void getPlaygroundsForGuest() {
        getMvpView().showLoading();

        DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabasePlaygrounds
                .orderByChild("isActive")
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                            getMvpView().onNoInternetConnection();
                            return;
                        }

                        if (dataSnapshot.exists()) {
                            Playground playground;
                            List<Playground> playgrounds = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                playground = child.getValue(Playground.class);
                                Log.d("ttt",playground.isHide+"");
                                if (playground != null && playground.isActive&&!playground.isHide) {
                                    playgrounds.add(playground);
                                }
                            }

                            if (playgrounds.size() == 0) {
                                getMvpView().onGetPlayground(playgrounds);
                                return;
                            }

                            getMvpView().onGetPlayground(playgrounds);

                        } else {
                            getMvpView().onError(R.string.error_no_data);
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
    public void getPlaygrounds(String userCity, double latitude, double longitude) {
        getMvpView().showLoading();

        final android.location.Location userLocation = new android.location.Location("User Current location");
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);

        DatabaseReference mDatabasePlaygrounds = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabasePlaygrounds
                .orderByChild("address_city")
              //  .equalTo(userCity)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                            getMvpView().onNoInternetConnection();
                            return;
                        }

                        if (dataSnapshot.exists()) {
                            Playground playground;
                            ArrayList<Playground> playgrounds = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                playground = child.getValue(Playground.class);
                                if (playground != null && playground.isActive&&!playground.isHide) {
                                    playgrounds.add(playground);
                                }
                            }

                            if (playgrounds.size() == 0) {
                                getMvpView().onGetPlayground(playgrounds);
                                return;
                            }

                            List<Playground> list = ListUtils.sortPlaygroundsByNearest(playgrounds, userLocation);

                            getMvpView().onGetPlayground(list);

                        } else {
                            getMvpView().onError(R.string.error_no_data);
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
