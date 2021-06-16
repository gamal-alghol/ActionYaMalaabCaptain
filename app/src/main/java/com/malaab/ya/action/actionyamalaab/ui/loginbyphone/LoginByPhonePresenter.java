package com.malaab.ya.action.actionyamalaab.ui.loginbyphone;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.ActivityContext;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;
import com.malaab.ya.action.actionyamalaab.utils.FileUtils;
import com.malaab.ya.action.actionyamalaab.utils.FirebaseUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class LoginByPhonePresenter<V extends LoginByPhoneMvpView>
        extends BasePresenter<V>
        implements LoginByPhoneMvpPresenter<V> {

    @Inject
    @ActivityContext
    public Context mContext;

    @Inject
    public AppCompatActivity mActivity;

    private FirebaseAuth mAuth;


    @Inject
    public LoginByPhonePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Register Screen");

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode(Constants.LANGUAGE_ARABIC_CODE);
    }


    @Override
    public void getVerificationCode(String countryCode, String phoneNumber) {
        getMvpView().showLoading();

        String phone = countryCode.trim() + phoneNumber.trim();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String phoneVerificationId, PhoneAuthProvider.ForceResendingToken token) {
                if (!isViewAttached()) {
                    return;
                }

                getDataManager().updateFirebaseSettings(phoneVerificationId, token);

                getMvpView().hideLoading();
                getMvpView().onCodeSent(phoneVerificationId, token);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                AppLogger.d("onVerificationCompleted - Provider:" + credential.getProvider());
                AppLogger.d("onVerificationCompleted - SignInMethod:" + credential.getSignInMethod());
                AppLogger.d("onVerificationCompleted - SmsCode:" + credential.getSmsCode());

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().onVerificationCompleted(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e != null) {
                    AppLogger.w("onVerificationFailed -> " + e.getLocalizedMessage());
                }

                if (!isViewAttached()) {
                    return;
                }

                getMvpView().hideLoading();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
//                    getMvpView().showMessage("Invalid credential: " + e.getLocalizedMessage());
                    getMvpView().showMessage(R.string.error_phone_no_format);
                    getMvpView().onGetVerificationCodeFailed();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
//                    getMvpView().showMessage("SMS Quota exceeded");
                    getMvpView().showMessage(R.string.error_many_request);
                    getMvpView().onGetVerificationCodeFailed();
                }
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                mActivity,               // Activity (for callback binding)
                verificationCallbacks);
    }

    @Override
    public void verifyPhoneNumberWithCode(String verificationId, String code) {
        getMvpView().showLoading();

        if (StringUtils.isEmpty(verificationId)) {
            getMvpView().hideLoading();
            getMvpView().showMessage(R.string.error);
            return;
        }

        if (StringUtils.isEmpty(code)) {
            getMvpView().hideLoading();
            getMvpView().showMessage(R.string.error_verification_code);
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    @Override
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        if (StringUtils.isEmpty(phoneVerificationId)) {
//            getMvpView().hideLoading();
//            getMvpView().showMessage(R.string.error);
//            return;
//        }
//
//        if (StringUtils.isEmpty(code)) {
//            getMvpView().hideLoading();
//            getMvpView().showMessage(R.string.error_verification_code);
//            return;
//        }

//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);

        if (credential == null) {
            getMvpView().hideLoading();
            return;
        }

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (task.isSuccessful() && task.getResult() != null) {
                            // User has been added to firebase users as username is his phone number
                            FirebaseUser firebaseUser = task.getResult().getUser();

                            if (firebaseUser != null) {
//                                getDataManager().updateFirebaseSettings();
                                getMvpView().onUserSignedInSuccess(firebaseUser.getUid());

                            } else {
                                getMvpView().hideLoading();
                                getMvpView().showMessage(R.string.error);
                                getMvpView().onUserSignedInFailed();
                            }

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                getMvpView().hideLoading();
//                                getMvpView().showMessage("The verification code entered was invalid");
                                getMvpView().showMessage(R.string.error_verification_code);
                                getMvpView().onUserSignedInFailed();
                            }
                        }
                    }
                });
    }


    @Override
    public void isUserExistInDB(final String userUId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
        mDatabase.child(userUId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                user.uId = dataSnapshot.getKey();

                                if (user.role.equals(UserRole.ROLE_CAPTAIN)) {

                                    if (user.isActive) {
                                        getDataManager().setCurrentUser(user);
                                        getMvpView().onUserIsActive(user);
                                    } else {
                                        getMvpView().onUserNotActive(mActivity.getString(R.string.error_user_inactive));
//                                        getMvpView().showMessage(R.string.error_user_inactive);
                                    }

                                } else {
                                    getMvpView().showMessage(R.string.error_user_owner);
                                }

                            } else {
                                getMvpView().onCreateNewUser(userUId);
                            }

//                            for (DataSnapshot userDetails : dataSnapshot.getChildren()) {
//                                Log.d("valueName:", userDetails.child("Name").getValue());
//                                Log.d("valueEmail:", userDetails.child("Email").getValue());
//                                Log.d("valueuserid:", userDetails.child("userid").getValue());
//                            }

                        } else {
                            getMvpView().onCreateNewUser(userUId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.d("isUserExistInDB onCancelled = " + databaseError.getMessage());

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onError(R.string.error);
                    }
                });
    }

    @Override
    public void generateUserUniqueId(final String userUId) {
        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_META_DATA).child(Constants.FIREBASE_DB_META_DATA_USER_APPUSERID);
        mDatabase.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                // Analyse databaseError for any error during increment
//                AppLogger.d("onComplete - getMessage()" + databaseError.getMessage());

                if (!isViewAttached()) {
                    return;
                }

                if (dataSnapshot.exists()) {
                    Long appUserId = dataSnapshot.getValue(Long.class);
                    if (appUserId != null) {
                        getMvpView().onUserUniqueIdGenerated(userUId, appUserId);
                        return;
                    }
                }

                getMvpView().showMessage(R.string.error);
            }
        });

    }


    @Override
    public void addUserToFirebaseDatabase(final String userUId, final long appUserId, final String fName, final String lName, final String email,
                                          final String countryCode, final String phone,
                                          final String password,
                                          final String referredBy) {

        getMvpView().showLoading();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);

        final User user = new User(new User.UserBuilder(email, password, LoginMode.LOGGED_IN_MODE_SERVER)
                .withOptionalUID(userUId)
                .withOptionalAppUserId(appUserId)
                .withOptionalRole(UserRole.ROLE_CAPTAIN)
                .withOptionalFirstName(fName)
                .withOptionalLastName(lName)
                .withOptionalMobileNo(countryCode.trim() + phone.trim())
                .withOptionalCreatedDate(DateTimeUtils.getCurrentDatetime())
                .withOptionalReferredBy(referredBy)
                .withOptionalIsActive(true)
        );

        mDatabase.child(userUId)
                .setValue(user)  // pushing user to 'users' node using the userId
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (task.isSuccessful()) {
                            AppLogger.i("Adding User To Database -> Success.");

                            FirebaseUtils.sendNotificationToAdmin(NotificationType.CAPTAIN_NEW, "", "", userUId, user.getUserFullName(), user.profileImageUrl);

                            getDataManager().setCurrentUser(user);

                            getMvpView().onUserAddedToFirebaseSuccess(user);

                        } else {
                            AppLogger.i("Adding User To Database -> Failed.");
                            getMvpView().showMessage(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                        }
                    }
                });
    }


    @Override
    public void uploadImage(final User user, File profileImage) {
        if (profileImage == null) {
            getMvpView().onUploadProfileImageSuccess(user);
            return;
        }

        getMvpView().showLoading();

        String name = user.email + "_" + String.valueOf(System.currentTimeMillis()) + "." + FileUtils.getFileExtension(mActivity, profileImage);

        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference fileToUpload = mStorage.child(Constants.FIREBASE_DB_USER_IMAGE_FOLDER).child(name);
        fileToUpload.putFile(Uri.fromFile(profileImage))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        AppLogger.i("Uploading Image: onSuccess");

                        final Uri downloadUri = taskSnapshot.getUploadSessionUri();

                        if (downloadUri == null) {
                            user.profileImageUrl = "";

                            getMvpView().onUploadProfileImageSuccess(user);

                        } else {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
                            mDatabase.child(user.uId)
                                    .child("profileImageUrl")
                                    .setValue(downloadUri.toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (!isViewAttached()) {
                                                return;
                                            }

                                            getMvpView().hideLoading();

                                            if (task.isSuccessful()) {
                                                AppLogger.i("Adding User To Database -> Success.");

                                                user.profileImageUrl = downloadUri.toString();
                                                getDataManager().setCurrentUser(user);

                                            } else {
                                                AppLogger.i("Adding User To Database -> Failed.");
                                                getMvpView().showMessage(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                                            }

                                            getMvpView().onUploadProfileImageSuccess(user);
                                        }
                                    });
                        }

                        getDataManager().setCurrentUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        getMvpView().showMessage(exception.getLocalizedMessage());

                        getMvpView().onUploadProfileImageSuccess(user);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) {
                    }
                });
    }


//    @Override
//    public void signInWithPhoneAndDoServerRegister(String phoneVerificationId, String code,
//                                                   final String fName, final String lName, final String email, final String phone, final String password,
//                                                   final String referredBy) {
//        getMvpView().showLoading();
//        getMvpView().onHidePasswordStrength();
//
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!isViewAttached()) {
//                            return;
//                        }
//
//                        if (task.isSuccessful()) {
//                            // User has been added to firebase users as username is his phone number
//                            FirebaseUser firebaseUser = task.getResult().getUser();
//
//                            if (firebaseUser != null) {
//                                // creating user object
//                                User user = new User(new User.UserBuilder(email, password, LoginMode.LOGGED_IN_MODE_SERVER)
//                                        .withOptionalUID(firebaseUser.getUid())
//                                        .withOptionalRole(UserRole.ROLE_OWNER)
//                                        .withOptionalFirstName(fName)
//                                        .withOptionalLastName(lName)
//                                        .withOptionalMobileNo(phone)
//                                        .withOptionalReferredBy(referredBy)
//                                        .withOptionalIsActive(false)
//                                );
//
//                                getMvpView().onUserVerifiedSuccess(user);
//
//                            } else {
//                                getMvpView().hideLoading();
//                                getMvpView().onError(R.string.error);
//                            }
//
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                getMvpView().hideLoading();
//                                getMvpView().onError("The verification code entered was invalid");
//                            }
//                        }
//                    }
//                });
//    }

}

