package com.malaab.ya.action.actionyamalaab.ui.messages;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class MessagesPresenter<V extends MessagesMvpView>
        extends BasePresenter<V>
        implements MessagesMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public MessagesPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Messages Screen");
    }


    @Override
    public void getMessages() {
        getMvpView().showLoading();

        getDataManager().resetMessagesCounters();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_CONTACT_US);
        mDatabase
//                .orderByChild("toUserUid")
//                .equalTo(getUserDetails().uId)
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
                            Message message;
                            List<Message> list = new ArrayList<>();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                message = child.getValue(Message.class);

                                if (message != null &&
                                        ((!StringUtils.isEmpty(message.fromUserUid) && message.fromUserUid.equals(getUserDetails().uId))
                                                || (!StringUtils.isEmpty(message.toUserUid) && message.toUserUid.equals(getUserDetails().uId)))) {

                                    message.uid = child.getKey();

                                    if (message.reply != null) {
                                        list.add(message.reply);
                                    }

                                    list.add(message);
                                }
                            }

                            Collections.reverse(list);

                            getMvpView().onGetMessages(list);

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
    public void resetMessagesCounters() {
        getDataManager().resetMessagesCounters();
    }
}
