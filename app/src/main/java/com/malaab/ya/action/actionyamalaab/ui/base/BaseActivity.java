package com.malaab.ya.action.actionyamalaab.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.malaab.ya.action.actionyamalaab.AppController;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.di.component.DaggerActivityComponent;
import com.malaab.ya.action.actionyamalaab.di.module.ActivityModule;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.CommonUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.LocaleHelper;
import com.malaab.ya.action.actionyamalaab.utils.NetworkUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.Validator;
import com.yayandroid.theactivitymanager.TAMBaseActivity;

import javax.inject.Inject;

import butterknife.Unbinder;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends TAMBaseActivity implements MvpView, BaseFragment.Callback {

    protected abstract void initUI();

    @Inject
    public Validator mValidator;
    private ActivityComponent mActivityComponent;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((AppController) getApplication()).getComponent())
                .build();
    }

//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        super.setContentView(layoutResID);
//    }


    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initUI();

        AppCompatImageButton header_btn_back = findViewById(R.id.header_btn_back);
        if (header_btn_back != null) {
            if (LocaleHelper.getLanguage(this).equalsIgnoreCase(Constants.LANGUAGE_ARABIC_CODE)) {
                header_btn_back.setImageResource(R.drawable.icon_arrow_right);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));

        snackbar.show();
    }


    @Override
    public void showMessage(String message) {
        if (!StringUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
//        else {
//            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }


    @Override
    public void onError(String message) {
        if (!StringUtils.isEmpty(message)) {
            showSnackBar(message);
        }
//        else {
//            showSnackBar(getString(R.string.error));
//        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }


    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }


    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null)
            view = new View(this);

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onUserAuthenticationFailed(@StringRes int resId) {
        if (resId != 0) {
            showMessage(getString(resId));
        }
        ActivityUtils.goTo(BaseActivity.this, LoginActivity.class, true);
    }


    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            final View view = getCurrentFocus();
//
//            if (view != null) {
//                final boolean consumed = super.dispatchTouchEvent(ev);
//
//                final View viewTmp = getCurrentFocus();
//                final View viewNew = viewTmp != null ? viewTmp : view;
//
//                if (viewNew.equals(view)) {
//                    final Rect rect = new Rect();
//                    final int[] coordinates = new int[2];
//
//                    view.getLocationOnScreen(coordinates);
//
//                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());
//
//                    final int x = (int) ev.getX();
//                    final int y = (int) ev.getY();
//
//                    if (rect.contains(x, y)) {
//                        return consumed;
//                    }
//                } else if (viewNew instanceof EditText) {
//                    return consumed;
//                }
//
//                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);
//                viewNew.clearFocus();
//
//                return consumed;
//            }
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }

        if (mValidator != null) {
            mValidator.onDestroy();
        }

        super.onDestroy();


        if (isFinishedByTAM) {
            AppLogger.i("BaseActivity -> " + getClass().getSimpleName() + " finished by TheActivityManager.");
        }
    }
}
