package com.malaab.ya.action.actionyamalaab.ui.account.accountdetails;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.FileUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.io.File;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class AccountDetailsPresenter<V extends AccountDetailsMvpView>
        extends BasePresenter<V>
        implements AccountDetailsMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;

    private StorageReference mStorage;


    @Inject
    public AccountDetailsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Account Details Screen");
try {
    mStorage = FirebaseStorage.getInstance().getReference();

}catch (NoClassDefFoundError E){
    Log.d("ttt",  E.getMessage());
}
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
    public void updateCurrentUser(final File image, final String fName, final String lName, final String phone, final String email, final String age, final String city) {
Log.d("ttt","updateCurrentUser");
        if (getDataManager().getCurrentUser() == null) {
            getMvpView().onError(R.string.msg_user_not_login);
            return;
        }

        getMvpView().showLoading();

        final User user = getDataManager().getCurrentUser();
        user.fName = fName;
        user.lName = lName;
        user.mobileNo = phone;
        user.email = email;
        user.age = Integer.valueOf(age);
        user.address_city = city;
        user.isActive = true;

        Log.d("ttt",image+"");

        if (image == null) {
            Log.d("ttt","image == null");
            update(user);
            return;
        }

        String name = email + "_" + String.valueOf(System.currentTimeMillis()) + "." + FileUtils.getFileExtension(mActivity,new File(String.valueOf(image)));
Log.d("ttt",name);
        final StorageReference fileToUpload = mStorage.child(Constants.FIREBASE_DB_USER_IMAGE_FOLDER).child(name);
        fileToUpload.putFile(Uri.fromFile(new File(String.valueOf(image))))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("ttt","updateCurrentUser");
fileToUpload.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
    @Override
    public void onComplete(@NonNull Task<Uri> task) {
        user.profileImageUrl = String.valueOf(task.getResult());
        update(user);
    }
});



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ttt",e.getMessage()+"2");

            }
        });;
    }


    private void update(final User user) {
        Log.d("ttt","update");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(user.uId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.i(" onSuccess");
                        Log.d("ttt","onSuccessupdate");

                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().setCurrentUser(user);

//                        getDataManager().updateCurrentUserInfo(user.uId, user.appUserId,
//                                user.email, user.password, user.fName, user.lName, user.dob, user.age, user.mobileNo,
//                                user.modifyDate, user.createdDate,
//                                user.address1, user.address2, user.postcode, user.city, user.state, user.country,
//                                user.profileImageUrl,
//                                user.referral_code, user.referred_by,
//                                user.role,
//                                user.fcmToken,
//                                user.latitude, user.longitude,
//                                user.loggedInMode, user.isActive);

                        getMvpView().hideLoading();
                        getMvpView().onUpdateCurrentUserSuccess();
                        getMvpView().showMessage(R.string.msg_success);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.i(" onComplete");

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
                        Log.d("ttt",e.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onError(e.getLocalizedMessage());
                    }
                });
    }
}
