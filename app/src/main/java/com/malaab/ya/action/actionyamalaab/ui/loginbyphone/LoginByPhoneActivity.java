package com.malaab.ya.action.actionyamalaab.ui.loginbyphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.PermissionsCodes;
import com.malaab.ya.action.actionyamalaab.custom.EditTextWithEyeToggle;
import com.malaab.ya.action.actionyamalaab.custom.PhoneNumberEdit;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.PermissionsUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class LoginByPhoneActivity extends BaseActivity implements LoginByPhoneMvpView {

    @BindView(R.id.img_background)
    public ImageView img_background;

    @BindView(R.id.txt_message)
    public TextView txt_message;

    @BindView(R.id.edt_phone)
    public PhoneNumberEdit edt_phone;
    @BindView(R.id.edt_verificationCode)
    public EditTextWithEyeToggle edt_verificationCode;
    @BindView(R.id.btn_requestVerificationCode)
    public Button btn_requestVerificationCode;
    @BindView(R.id.btn_verify)
    public Button btn_verify;
    @BindView(R.id.pBar_verify)
    public ProgressBar pBar_verify;

    @BindView(R.id.img_profile)
    public CircleImageView img_profile;
    @BindView(R.id.edt_fName)
    public EditText edt_fName;
    @BindView(R.id.edt_lName)
    public EditText edt_lName;
    @BindView(R.id.edt_email)
    public EditText edt_email;
    @BindView(R.id.btn_register)
    public Button btn_register;

    @Inject
    LoginByPhoneMvpPresenter<LoginByPhoneMvpView> mPresenter;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mPhoneVerificationId;
    private String mUserUId;

    private File mProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        Glide.with(getApplicationContext())
                .load(R.drawable.img_background_new_blurred)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_background);
    }

    @Override
    protected void initUI() {
//        BlurImage.with(getApplicationContext())
//                .load(R.drawable.img_background_new)
//                .intensity(5)
//                .Async(false)
//                .into(img_background);

        edt_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mValidator
                .addField(edt_phone);
    }


    @OnClick(R.id.btn_requestVerificationCode)
    public void requestVerificationCode() {
        hideKeyboard();

        txt_message.setVisibility(View.GONE);

        if (mValidator.isValid()) {
            if (edt_phone.getText().toString().startsWith("0")) {
                edt_phone.setText(edt_phone.getText().toString().replaceFirst("0", ""));
            }

            btn_requestVerificationCode.setEnabled(false);

            mPresenter.getVerificationCode(edt_phone.getPrefix(), edt_phone.getText().toString());

//            new CountDownTimer(45000, 1000) {
//                @Override
//                public void onTick(long l) {
//                    timer.setText("0:" + l / 1000 + " s");
//                    resendCode.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onFinish() {
//                    timer.setText(0 + " s");
//                    resendCode.startAnimation(AnimationUtils.loadAnimation(PhoneActivity.this, R.anim.slide_from_right));
//                    resendCode.setVisibility(View.VISIBLE);
//                }
//            }.start();
        }
    }

    @OnClick(R.id.btn_verify)
    public void verifyCode() {
        txt_message.setVisibility(View.GONE);
        btn_verify.setEnabled(false);

        mValidator
                .addField(edt_verificationCode.getEditText());

        if (mValidator.isValid()) {
            mPresenter.verifyPhoneNumberWithCode(mPhoneVerificationId, edt_verificationCode.getText().trim());
        }
    }

    @OnClick(R.id.img_profile)
    public void selectProfileImage() {
        PermissionsUtils.requestPermission(LoginByPhoneActivity.this, PermissionsCodes.WRITE_EXTERNAL_STORAGE, Permission.STORAGE);
    }

    @OnClick(R.id.btn_register)
    public void registerServer() {
        hideKeyboard();

        txt_message.setVisibility(View.GONE);

        mValidator
                .addField(edt_fName)
                .addField(edt_lName)
                .addField(edt_email);

        if (mValidator.isValid()) {
            mPresenter.generateUserUniqueId(mUserUId);
        }
    }


    @PermissionYes(PermissionsCodes.WRITE_EXTERNAL_STORAGE)
    private void gotStoragePermission(@NonNull List<String> grantedPermissions) {
        EasyImage.openChooserWithGallery(this, getString(R.string.title_image_source), 0);
    }

    @PermissionNo(PermissionsCodes.WRITE_EXTERNAL_STORAGE)
    private void noStoragePermission(@NonNull List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            PermissionsUtils.showAlwaysDeniedCustomDialog(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                showMessage(e.getLocalizedMessage());
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (!ListUtils.isEmpty(imageFiles)) {

                    mProfileImage = imageFiles.get(0);

                    if (mProfileImage != null) {
                        Glide.with(getApplicationContext())
                                .load(mProfileImage)
                                .error(R.drawable.img_profile_default)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(img_profile);
                    }
                }
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                // Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(LoginByPhoneActivity.this);
                    if (photoFile != null)
                        photoFile.delete();
                }
            }
        });
    }


    @Override
    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            txt_message.setText(message);
            txt_message.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onCodeSent(String phoneVerificationId, PhoneAuthProvider.ForceResendingToken resendToken) {
        mPhoneVerificationId = phoneVerificationId;
        mResendToken = resendToken;

        btn_requestVerificationCode.setEnabled(true);
        btn_requestVerificationCode.setVisibility(View.GONE);

        edt_verificationCode.setVisibility(View.VISIBLE);
        btn_verify.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential credential) {
        btn_requestVerificationCode.setEnabled(true);
        btn_requestVerificationCode.setVisibility(View.GONE);

        btn_verify.setVisibility(View.GONE);
//        pBar_verify.setVisibility(View.VISIBLE);

        if (!StringUtils.isEmpty(credential.getSmsCode())){
            edt_verificationCode.setVisibility(View.VISIBLE);
            edt_verificationCode.setText(credential.getSmsCode());
        }

        String sms = credential.getSmsCode();

        mPresenter.signInWithPhoneAuthCredential(credential);
    }

    @Override
    public void onGetVerificationCodeFailed() {
        btn_requestVerificationCode.setEnabled(true);
        btn_requestVerificationCode.setVisibility(View.VISIBLE);
    }


    @Override
    public void onUserSignedInSuccess(String userUId) {
//        pBar_verify.setVisibility(View.GONE);

        mPresenter.isUserExistInDB(userUId);
    }

    @Override
    public void onUserSignedInFailed() {
//        pBar_verify.setVisibility(View.GONE);

        btn_verify.setEnabled(true);
        btn_verify.setVisibility(View.VISIBLE);
    }


    @Override
    public void onUserIsActive(User user) {
        if (user.latitude != 0 && user.longitude != 0) {
            ActivityUtils.goTo(LoginByPhoneActivity.this, HomeActivity.class, true);

        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.INTENT_KEY_IS_FIRST_TIME, true);
            ActivityUtils.goTo(LoginByPhoneActivity.this, MapActivity.class, true, bundle);
        }
    }

    @Override
    public void onUserNotActive(String message) {
        img_profile.setVisibility(View.GONE);
        edt_fName.setVisibility(View.GONE);
        edt_lName.setVisibility(View.GONE);
        edt_email.setVisibility(View.GONE);
        edt_phone.setVisibility(View.GONE);

        btn_requestVerificationCode.setEnabled(true);
        btn_requestVerificationCode.setVisibility(View.GONE);

        btn_verify.setVisibility(View.GONE);
        btn_register.setVisibility(View.GONE);

        txt_message.setVisibility(View.VISIBLE);
        txt_message.setTextSize(20);
        txt_message.setText(message);
    }

    @Override
    public void onCreateNewUser(String userUId) {
        mUserUId = userUId;

        edt_phone.setEnabled(false);
        edt_verificationCode.setVisibility(View.GONE);
        btn_requestVerificationCode.setEnabled(true);
        btn_requestVerificationCode.setVisibility(View.GONE);
        btn_verify.setVisibility(View.GONE);

        Glide.with(this)
                .load(R.drawable.img_profile_player_default)
                .error(R.drawable.img_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_profile);

        img_profile.setVisibility(View.VISIBLE);
        edt_fName.setVisibility(View.VISIBLE);
        edt_lName.setVisibility(View.VISIBLE);
        edt_email.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserUniqueIdGenerated(String userUId, long appUserId) {
        mPresenter.addUserToFirebaseDatabase(userUId, appUserId,
                edt_fName.getText().toString().trim(), edt_lName.getText().toString().trim(),
                edt_email.getText().toString().trim(),
                edt_phone.getPrefix(), edt_phone.getText().toString(),
                "", "");
    }

    @Override
    public void onUserAddedToFirebaseSuccess(User user) {
        mPresenter.uploadImage(user, mProfileImage);
    }

    @Override
    public void onUploadProfileImageSuccess(User user) {
        onUserIsActive(user);
    }


    @Override
    public void onNoInternetConnection() {
        txt_message.setText(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

}
