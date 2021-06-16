package com.malaab.ya.action.actionyamalaab.ui.login;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.custom.BSheetLogin;
import com.malaab.ya.action.actionyamalaab.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.loginbyphone.LoginByPhoneActivity;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.KeyboardUtils;
import com.malaab.ya.action.actionyamalaab.utils.LocaleHelper;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.yayandroid.theactivitymanager.TheActivityManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements LoginMvpView, OnBottomSheetListener {

    @BindView(R.id.img_background)
    public ImageView img_background;

    @BindView(R.id.txt_message)
    public TextView txt_message;

    @BindView(R.id.btn_login)
    public Button btn_login;
    @BindView(R.id.txt_loginByPhoneNumber)
    public TextView txt_loginByPhoneNumber;
    @BindView(R.id.btn_register)
    public Button btn_register;

    @BindView(R.id.fr_bottom)
    public FrameLayout fr_bottom;
    @BindView(R.id.btn_skip)
    public Button btn_skip;
    @BindView(R.id.btn_arabic)
    public Button btn_arabic;

    private BSheetLogin mBSheetLogin;


    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        Glide.with(getApplicationContext())
                .load(R.drawable.img_background_new)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(img_background);
    }

    @Override
    protected void initUI() {
        mBSheetLogin = new BSheetLogin();
        mBSheetLogin.attachAndInit(this);
        mBSheetLogin.setOnBottomSheetListener(this);
    }


    @OnClick(R.id.btn_login)
    public void loginServer() {
        ActivityUtils.goTo(LoginActivity.this, LoginByPhoneActivity.class, false);
//        txt_message.setVisibility(View.GONE);
//        mBSheetLogin.show();
    }

    @OnClick(R.id.txt_loginByPhoneNumber)
    public void loginByPhone() {
        ActivityUtils.goTo(LoginActivity.this, LoginByPhoneActivity.class, false);
    }


//    @OnClick(R.id.txt_forgotPassword)
//    public void forgotPassword() {
//        txt_hint.setVisibility(View.GONE);
//        txt_message.setVisibility(View.GONE);
//
//        mBSheetForgotPassword.show();
//    }

//    @OnClick(R.id.txt_skip)
//    public void skip() {
//        if (extra != null) {
//            finish();
//        } else {
//            ActivityUtils.goTo(LoginActivity.this, HomeActivity.class, true);
//        }
//    }

    @OnClick(R.id.btn_register)
    public void register() {
        ActivityUtils.goTo(LoginActivity.this, LoginByPhoneActivity.class, false);
//        ActivityUtils.goTo(LoginActivity.this, RegisterActivity.class, false);
    }


    @OnClick(R.id.btn_skip)
    public void skip() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.INTENT_KEY_ISGUEST, true);

        ActivityUtils.goTo(LoginActivity.this, HomeActivity.class, true, bundle);
    }

    @OnClick(R.id.btn_arabic)
    public void arabic() {
        if (LocaleHelper.getLanguage(this).equals(Constants.LANGUAGE_ENGLISH_CODE)) {
            mPresenter.updateLanguage(Constants.LANGUAGE_ARABIC_CODE);
        } else {
            mPresenter.updateLanguage(Constants.LANGUAGE_ENGLISH_CODE);
        }
    }


    @Override
    public void onSlide(float slideOffset) {
        if (slideOffset == 0) {
            Glide.with(this)
                    .load(R.drawable.img_background_new)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(img_background);

            btn_login.setAlpha(1f);
            txt_loginByPhoneNumber.setAlpha(1f);
            btn_register.setAlpha(1f);
            fr_bottom.setAlpha(1f);

            return;
        }

//        float intensity = slideOffset * 100 / 25;
//        AppLogger.d("intensity = " + intensity);

        btn_login.setAlpha(1 - slideOffset);
        txt_loginByPhoneNumber.setAlpha(1 - slideOffset);
        btn_register.setAlpha(1 - slideOffset);
        fr_bottom.setAlpha(1 - slideOffset);

//        BlurImage.with(getApplicationContext())
//                .load(R.drawable.img_background_new)
//                .intensity(slideOffset * 100 / 25)
//                .Async(false)
//                .into(img_background);
    }

    @Override
    public void onBottomSheetExpanded() {

    }

    @Override
    public void onBottomSheetCollapsed(String... args) {
        String email = args[0];
        String password = args[1];

        mPresenter.doServerLogin(email, password);
    }


    @Override
    public void showLoading() {
        super.showLoading();

        KeyboardUtils.hideSoftInput(this);

        btn_login.setAlpha(0.5f);
        btn_register.setAlpha(0.5f);
        fr_bottom.setAlpha(0.5f);

        btn_login.setEnabled(false);
        btn_register.setEnabled(false);
        btn_skip.setEnabled(false);
        btn_arabic.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();

        btn_login.setAlpha(1f);
        btn_register.setAlpha(1f);
        fr_bottom.setAlpha(1f);

        btn_login.setEnabled(true);
        btn_register.setEnabled(true);
        btn_skip.setEnabled(true);
        btn_arabic.setEnabled(true);
    }


    @Override
    public void onLanguageUpdatedSuccess() {
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TheActivityManager.getInstance().getCurrentActivity().startActivityForResult(intent, 1);
    }


    @Override
    public void onServerLoginSuccess(String userUId) {
        mPresenter.isUserExistInDB(userUId);
    }

    @Override
    public void onUserExistInDB(User user) {
//        if (extra != null) {
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra(Constants.INTENT_KEY, extra);
//            setResult(Activity.RESULT_OK, returnIntent);
//
//            finish();
//
//        } else {
        if (user.latitude != 0 && user.longitude != 0) {
            ActivityUtils.goTo(LoginActivity.this, HomeActivity.class, true);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.INTENT_KEY_IS_FIRST_TIME, true);

            ActivityUtils.goTo(LoginActivity.this, MapActivity.class, true, bundle);
        }
//        }

    }

    @Override
    public void onUserNotExistInDB() {

    }


    @Override
    public void onResetPassword(String message) {
//        mDialogConfirmation.withTitle(getString(R.string.reset_password))
//                .withMessage(message)
//                .withPositiveButton("OK")
//                .build()
//                .show();
    }


    @Override
    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            txt_message.setText(message);
            YoYo.with(Techniques.FadeInUp)
                    .duration(300)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            txt_message.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(txt_message);

            btn_login.setAlpha(1f);
            btn_register.setAlpha(1f);
            fr_bottom.setAlpha(1f);

            btn_login.setEnabled(true);
            btn_register.setEnabled(true);
            btn_skip.setEnabled(true);
            btn_arabic.setEnabled(true);
        }
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
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
        if (!mBSheetLogin.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mBSheetLogin.onDetach();
        mPresenter.onDetach();
        super.onDestroy();
    }

}
