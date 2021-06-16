package com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class    FinesPresenter<V extends FinesMvpView>
        extends BasePresenter<V>
        implements FinesMvpPresenter<V> {


    @Inject
    public FinesPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - My Fines Screen");
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
    public void getMyFines(String userUid) {
        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_FINES_TABLE).child(userUid);
        mDatabase
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot.exists()) {

                            Fine fine;
                            List<Fine> fines = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                fine = child.getValue(Fine.class);

                                if (fine != null) {
                                    fines.add(fine);
                                }
                            }

                            ListUtils.sortFinesList(fines);

                            getMvpView().onGetMyFines(fines);

                        }
                        else {
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
}
