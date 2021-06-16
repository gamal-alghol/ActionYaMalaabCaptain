package com.malaab.ya.action.actionyamalaab.custom;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BSheetLogin extends BottomSheetBehavior {

    @BindView(R.id.btn_close)
    AppCompatImageButton btn_close;

    @BindView(R.id.bsheet_edt_email)
    EditText edt_email;
    @BindView(R.id.bsheet_edt_password)
    EditText edt_password;
    @BindView(R.id.bsheet_btn_login)
    Button btn_login;

    private Activity mActivity;
    private BottomSheetBehavior mBottomSheetBehavior;
    private OnBottomSheetListener mBottomSheetListener;

    private Validator mValidator;
    private boolean isLoginTriggered;


    public void attachAndInit(final Activity activity) {
        if (activity != null) {
            mActivity = activity;

            View bottomSheet = mActivity.findViewById(R.id.bSheet_login);
            ButterKnife.bind(this, bottomSheet);

            mValidator = new Validator(activity);
            mValidator.addField(edt_email)
                    .addField(edt_password);

            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setPeekHeight(0);

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (mBottomSheetListener == null)
                        return;

                    if (newState == STATE_EXPANDED) {
//                        if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
//                            ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
//                        }

                        mBottomSheetListener.onBottomSheetExpanded();

                    } else if (newState == STATE_COLLAPSED) {
                        if (isLoginTriggered) {
                            isLoginTriggered = false;

                            if (StringUtils.isEmpty(edt_email.getText().toString()) || StringUtils.isEmpty(edt_password.getText().toString()))
                                return;

                            mBottomSheetListener.onBottomSheetCollapsed(edt_email.getText().toString(), edt_password.getText().toString());
                        }
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if (mBottomSheetListener == null)
                        return;

                    mBottomSheetListener.onSlide(slideOffset);
                }
            });
        }
    }


    @OnClick(R.id.btn_close)
    void close() {
        hide();
    }

    @OnClick(R.id.bsheet_btn_login)
    void login() {
        if (mValidator.isValid()) {
            isLoginTriggered = true;
            hide();
        }
    }


    public void resetCredentials() {
        edt_email.setText("");
        edt_password.setText("");
    }


    public void setOnBottomSheetListener(OnBottomSheetListener listener) {
        mBottomSheetListener = listener;
    }


    private boolean isShown() {
        return mBottomSheetBehavior != null && mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    public void show() {
        if (!isShown()) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void hide() {
//        if (isShown()) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
    }


    public boolean onBackPressed() {
        if (isShown()) {
            hide();
            return true;
        }

        return false;
    }

    public void onDetach() {
        mBottomSheetListener = null;
        mActivity = null;

        if (mValidator != null) {
            mValidator.onDestroy();
        }
    }
}
