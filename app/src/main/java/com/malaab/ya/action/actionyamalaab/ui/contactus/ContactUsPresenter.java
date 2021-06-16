package com.malaab.ya.action.actionyamalaab.ui.contactus;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class ContactUsPresenter<V extends ContactUsMvpView>
        extends BasePresenter<V>
        implements ContactUsMvpPresenter<V> {

    @Inject
    public ContactUsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - ContactUs Screen");
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

    public void sendMessage(final String fromUserUid, final String fromUserAppId, final String fromUsername, String fromUserEmail, String fromUserPhone, final String fromUserProfileImage, String message) {
        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_CONTACT_US);
        String uid = mDatabase.push().getKey();

        Message contactUs = new Message(uid,
                fromUserUid, fromUserAppId, fromUsername, fromUserEmail, fromUserPhone, fromUserProfileImage,
                message,
                DateTimeUtils.getCurrentDatetime(),
                false);

        mDatabase.child(uid)
                .setValue(contactUs)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        FirebaseUtils.sendNotificationToAdmin(NotificationType.MESSAGE_CONTACT_US, "", "", fromUserUid, fromUsername, fromUserProfileImage);

                        getMvpView().onSendMessageSuccess();
                        getMvpView().showMessage(R.string.msg_contactus_success);
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

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }

}
