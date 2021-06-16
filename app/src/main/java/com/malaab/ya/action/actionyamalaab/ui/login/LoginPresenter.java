package com.malaab.ya.action.actionyamalaab.ui.login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class LoginPresenter<V extends LoginMvpView>
        extends BasePresenter<V>
        implements LoginMvpPresenter<V> {

    @Inject
    public AppCompatActivity mActivity;

    private FirebaseAuth mAuth;


    @Inject
    public LoginPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Login Screen");

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void updateLanguage(String languageCode) {
        if (getDataManager().getAppSettings() != null && getDataManager().getAppSettings().appLanguage.equalsIgnoreCase(languageCode)) {
            return;
        }

        getDataManager().updateAppSettings(languageCode, false, false, false);
        getMvpView().onLanguageUpdatedSuccess();
    }


    @Override
    public void doServerLogin(final String email, final String password) {
        getMvpView().showLoading();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (task.isSuccessful()) {
                            Logger.i("Authentication Success.");
                            getMvpView().onServerLoginSuccess(task.getResult().getUser().getUid());

                        } else {
                            getMvpView().hideLoading();

                            if (task.getException() != null) {
                                Logger.e("Authentication Failed => " + task.getException().getMessage());
                                getMvpView().showMessage("Authentication Failed \n" + task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void isUserExistInDB(String userUId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                user.uId = dataSnapshot.getKey();

                                if (!user.isActive) {
                                    getMvpView().showMessage(R.string.error_user_inactive);
                                    return;
                                }

                                if (user.role.equals(UserRole.ROLE_CAPTAIN)) {
                                    getDataManager().setCurrentUser(user);
                                    getMvpView().onUserExistInDB(user);

                                } else {
                                    getMvpView().showMessage(R.string.error_user_owner);
                                }
                            }

//                            for (DataSnapshot userDetails : dataSnapshot.getChildren()) {
//                                Log.d("valueName:", userDetails.child("Name").getValue());
//                                Log.d("valueEmail:", userDetails.child("Email").getValue());
//                                Log.d("valueuserid:", userDetails.child("userid").getValue());
//                            }

                        } else {
                            getMvpView().showMessage(R.string.error_user_not_exist);
                            getMvpView().onUserNotExistInDB();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        AppLogger.d("isUserExistInDB onCancelled = " + databaseError.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().showMessage(R.string.error);
                    }
                });
    }


    @Override
    public void forgotPassword(String username, String email) {
//        getMvpView().showLoading();
//
//        getCompositeDisposable().add(getDataManager()
//                .forgotPassword(username, email, getUserLanguage())
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(new Consumer<ForgotPasswordResponse>() {
//                    @Override
//                    public void accept(ForgotPasswordResponse response) throws Exception {
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        if (response != null && response.result != null) {
//                            if (response.result.status == 0) {
//                                getMvpView().onResetPassword(context.getString(R.string.error_forgot_password));
//
//                            } else {
//                                getMvpView().onResetPassword(context.getString(R.string.msg_reset_password_success));
//                            }
//                        } else {
//                            getMvpView().onError(R.string.error);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        AppLogger.e(throwable.getMessage());
//
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        getMvpView().hideLoading();
//
//                        handleApiError(throwable);
//                    }
//                }));
    }

}
