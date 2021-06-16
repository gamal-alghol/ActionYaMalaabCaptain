package com.malaab.ya.action.actionyamalaab.ui.search.search_region;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.City;
import com.malaab.ya.action.actionyamalaab.data.network.model.Region;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class SearchRegionPresenter<V extends SearchRegionMvpView>
        extends BasePresenter<V>
        implements SearchRegionMvpPresenter<V> {

    private DatabaseReference mDatabasePlaygrounds;
    private ValueEventListener mValueEventListener;


    @Inject
    public SearchRegionPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable,  iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android Search Region Screen");

        mDatabasePlaygrounds = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_REGIONS_TABLE);
    }


    @Override
    public void getRegions() {
        getMvpView().showLoading();

        /* To load the list once only*/
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot.exists()) {
                    Region region;
                    City city;
                    List<Region> regions = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        region = new Region();
                        region.uid = child.getKey();
                        region.cities = new ArrayList<>();

                        for (DataSnapshot snap : child.getChildren()) {
                            city = snap.getValue(City.class);
                            region.cities.add(city);
                        }

//                        if (region != null) {
//                            region.uid = child.getKey();
//                        }
//
                        regions.add(region);
                    }

                    if (!isViewAttached()) {
                        return;
                    }

                    getMvpView().hideLoading();
                    getMvpView().onGetRegionsSuccess(regions);

                } else {
                    if (!isViewAttached()) {
                        return;
                    }
                    getMvpView().hideLoading();
                    getMvpView().onGetRegionsFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AppLogger.e(" Error -> " + error.toException());

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();
                getMvpView().onGetRegionsFailed();
            }
        };

        mDatabasePlaygrounds.addListenerForSingleValueEvent(mValueEventListener);
    }
}
