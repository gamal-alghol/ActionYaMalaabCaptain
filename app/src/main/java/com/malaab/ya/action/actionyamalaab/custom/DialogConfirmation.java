package com.malaab.ya.action.actionyamalaab.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.malaab.ya.action.actionyamalaab.utils.StringUtils;


public class DialogConfirmation {

    private Activity mActivity;
    private AlertDialog.Builder mBuilder;
    private AlertDialog alert;

    private String mTitle = "", mMessage = "", mPositiveButton = "", mNegativeButton = "";

    private OnDialogConfirmationListener mListener;


    public DialogConfirmation with(Activity activity) {
        if (mBuilder == null) {
            mActivity = activity;
            mBuilder = new AlertDialog.Builder(mActivity);
        }

        return this;
    }

    public DialogConfirmation setOnDialogConfirmationListener(OnDialogConfirmationListener listener) {
        mListener = listener;

        return this;
    }


    public DialogConfirmation withTitle(String title) {
        mTitle = title;
        return this;
    }

    public DialogConfirmation withMessage(String message) {
        mMessage = message;
        return this;
    }

    public DialogConfirmation withPositiveButton(String positiveButton) {
        mPositiveButton = positiveButton;
        return this;
    }

    public DialogConfirmation withNegativeButton(String negativeButton) {
        mNegativeButton = negativeButton;
        return this;
    }


    public DialogConfirmation build() {

//        if (StringUtils.isEmpty(mTitle)) {
//            this.mTitle = mActivity.getString(R.string.dialog_title_confirm);
//        }

//        if (StringUtils.isEmpty(mMessage)) {
//            this.mMessage = mActivity.getString(R.string.dialog_confirmation_message);
//        }

        if (!StringUtils.isEmpty(mPositiveButton)) {
            mBuilder.setPositiveButton(mPositiveButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.hide();
                    if (mListener != null) {
                        mListener.onPositiveButtonClick();
                    }
                }
            });
        }

        if (!StringUtils.isEmpty(mNegativeButton)) {
            mBuilder.setNegativeButton(mNegativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mListener != null) {
                        mListener.onNegativeButtonClick();
                    }
                    alert.hide();
                }
            });
        }

        mBuilder.setTitle(mTitle);
        mBuilder.setMessage(mMessage);

        alert = mBuilder.create();

        return this;
    }


    private boolean isDialogShown() {
        return (alert != null && alert.isShowing());
    }

    public void show() {
        if (!isDialogShown()) {
            alert.show();
        }
    }

    public void hide() {
        if (isDialogShown())
            alert.hide();
    }


    public void dismiss() {
        if (isDialogShown()) {
            alert.dismiss();
        }
    }


    public void onDestroy() {
        if (alert != null) {
            dismiss();
        }

        mBuilder = null;
        alert = null;

        mListener = null;
        mActivity = null;
    }


    public interface OnDialogConfirmationListener {

        void onPositiveButtonClick();

        void onNegativeButtonClick();
    }
}
