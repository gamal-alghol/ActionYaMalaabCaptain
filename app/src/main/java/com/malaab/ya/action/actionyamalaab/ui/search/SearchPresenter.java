package com.malaab.ya.action.actionyamalaab.ui.search;

import android.support.annotation.NonNull;
import android.widget.Toast;

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
import com.malaab.ya.action.actionyamalaab.data.network.model.PlaygroundSearch;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class SearchPresenter<V extends SearchMvpView>
        extends BasePresenter<V>
        implements SearchMvpPresenter<V> {

    private DatabaseReference mDatabase;


    @Inject
    public SearchPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Search Screen");
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
    public void searchByPrice(float priceStart, float priceEnd) {
        getMvpView().showLoading();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE);
        mDatabase.orderByChild("price")
                .startAt(priceStart)
                .endAt(priceEnd)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            PlaygroundSearch playground;
                            List<PlaygroundSearch> playgrounds = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                playground = child.getValue(PlaygroundSearch.class);
                                if (playground != null && playground.isActive) {
                                    playgrounds.remove(playground);     // prevent duplicate and get latest one
                                    playgrounds.add(playground);
                                }
                            }

                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsSuccess(playgrounds, false);

                        } else {
                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsFailed();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e("search() Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onGetPlaygroundsFailed();
                    }
                });
    }

    @Override
    public void searchByRegion(String region, String city, String direction) {
        if (StringUtils.isEmpty(region)) {
            getMvpView().showMessage(R.string.title_region_select);
            return;
        }

        if (StringUtils.isEmpty(city)) {
            getMvpView().showMessage(R.string.title_city_select);
            return;
        }

        getMvpView().showLoading();

        String criteria = "";
        String criteriaValue = "";

        if (!StringUtils.isEmpty(region)) {
            criteria = "address_region";
            criteriaValue = region;
        }

        if (!StringUtils.isEmpty(city)) {
            criteria = "address_city";
            criteriaValue = city;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE);
        mDatabase.orderByChild(criteria)
                .equalTo(criteriaValue)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            PlaygroundSearch playground;
                            List<PlaygroundSearch> playgrounds = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                playground = child.getValue(PlaygroundSearch.class);
                                if (playground != null && playground.isActive) {
                                    playgrounds.remove(playground);     // prevent duplicate and get latest one
                                    playgrounds.add(playground);
                                }
                            }

                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsSuccess(playgrounds, false);

                        } else {
                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsFailed();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e("search() Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onGetPlaygroundsFailed();
                    }
                });
    }

    @Override
    public void searchByAge(float ageStart, float ageEnd) {
        if (ageStart == 0 || ageEnd == 0) {
            getMvpView().showMessage(R.string.msg_age_select);
            return;
        }

        getMvpView().showLoading();

        String criteria = "hasAgeYoung";

        if (ageStart == 8) {
            criteria = "hasAgeYoung";
        } else if (ageStart == 13) {
            criteria = "hasAgeMiddle";
        } else if (ageStart == 18) {
            criteria = "hasAgeOld";
        }

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE);
        mDatabase.orderByChild(criteria)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            PlaygroundSearch playground;
                            List<PlaygroundSearch> playgrounds = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                playground = child.getValue(PlaygroundSearch.class);
                                if (playground != null && playground.isActive && DateTimeUtils.isDateAfterCurrentDate(playground.timeStart)) {
//                                    playgrounds.remove(playground);     // prevent duplicate and get latest one
                                    playgrounds.add(playground);
                                }
                            }

                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
//                            getIndividualBookings(playgrounds);
                            getMvpView().onGetPlaygroundsSuccess(playgrounds, true);

                        } else {
                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsFailed();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e("search() Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onGetPlaygroundsFailed();
                    }
                });
    }

    @Override
    public void searchBySize(int size) {
        if (size == 0) {
            getMvpView().showMessage(R.string.title_size_select);
            return;
        }

        getMvpView().showLoading();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SEARCH_TABLE);
        mDatabase.orderByChild("size")
                .equalTo(size)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            PlaygroundSearch playground;
                            List<PlaygroundSearch> playgrounds = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                playground = child.getValue(PlaygroundSearch.class);
                                if (playground != null && playground.isActive) {
                                    playgrounds.remove(playground);     // prevent duplicate and get latest one
                                    playgrounds.add(playground);
                                }
                            }

                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsSuccess(playgrounds, false);

                        } else {
                            if (!isViewAttached()) {
                                return;
                            }

                            getMvpView().hideLoading();
                            getMvpView().onGetPlaygroundsFailed();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.e("search() Error -> " + databaseError.toException());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onGetPlaygroundsFailed();
                    }
                });
    }


    @Override
    public void getFullBookingDetails(String playgroundId) {
        if (StringUtils.isEmpty(playgroundId)) {
            getMvpView().onError(R.string.error_no_data);
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabase.child(playgroundId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot.exists()) {
                            Playground playground = dataSnapshot.getValue(Playground.class);

                            if (playground != null) {
                                getMvpView().onGetPlaygroundDetails(playground);
                                return;
                            }
                        }

                        getMvpView().showMessage(R.string.error_no_data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.d("onCancelled = " + databaseError.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.error);
                    }
                });
    }

    @Override
    public void getIndividualBookingDetails(String playgroundId, final String bookingId) {
        if (StringUtils.isEmpty(playgroundId) || StringUtils.isEmpty(bookingId)) {
            getMvpView().onError(R.string.error_no_data);
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_TABLE);
        mDatabase.child(playgroundId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (dataSnapshot.exists()) {
                            final Playground playground = dataSnapshot.getValue(Playground.class);

                            if (playground != null) {

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE);
                                mDatabase.child(bookingId)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (!isViewAttached()) {
                                                    return;
                                                }

                                                getMvpView().hideLoading();

                                                if (dataSnapshot.exists()) {
                                                    final Booking booking = dataSnapshot.getValue(Booking.class);
                                                    if (booking != null) {
                                                        playground.isIndividuals = true;
                                                        playground.booking = booking;
                                                        getMvpView().onGetPlaygroundDetails(playground);
                                                        return;
                                                    }
                                                }

                                                getMvpView().showMessage(R.string.error_no_data);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                AppLogger.d("onCancelled = " + databaseError.getMessage());

                                                if (!isViewAttached()) {
                                                    return;
                                                }

                                                getMvpView().hideLoading();
                                                getMvpView().showMessage(R.string.error);
                                            }
                                        });
                                return;
                            }
                        }

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.error_no_data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.d("onCancelled = " + databaseError.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.error);
                    }
                });

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
