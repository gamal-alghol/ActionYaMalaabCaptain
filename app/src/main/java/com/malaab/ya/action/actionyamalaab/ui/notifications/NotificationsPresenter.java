package com.malaab.ya.action.actionyamalaab.ui.notifications;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class NotificationsPresenter<V extends NotificationsMvpView>
        extends BasePresenter<V>
        implements NotificationsMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public NotificationsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Notifications Screen");
    }


    @Override
    public void getNotifications() {
        getMvpView().showLoading();

        getDataManager().resetNotificationsCounters();
        Log.d("ttt",getDataManager().getCurrentUser().uId);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_NOTIFICATIONS_TABLE);
        mDatabase.orderByChild("toUserUid")
                .equalTo(getDataManager().getCurrentUser().uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (!NetworkUtils.isNetworkConnected(mActivity.getApplicationContext())) {
                            getMvpView().onNoInternetConnection();
                            return;
                        }

                        if (dataSnapshot != null && dataSnapshot.exists()) {
                            Notification notification;
                            List<Notification> list = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                notification = child.getValue(Notification.class);
                                if (notification != null) {
                                    notification.uid = child.getKey();
                                    list.add(notification);
                                }
                            }

                            Collections.reverse(list);

                            getMvpView().onGetNotifications(list);

                        } else {
                            getMvpView().onError(R.string.error_no_data);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.e(" Error -> " + databaseError.toException().getLocalizedMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                    }
                });
    }

    @Override
    public void resetMessagesCounters() {
        getDataManager().resetNotificationsCounters();
    }
}
