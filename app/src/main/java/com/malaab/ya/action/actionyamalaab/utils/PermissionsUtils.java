package com.malaab.ya.action.actionyamalaab.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.PermissionsCodes;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;


public final class PermissionsUtils {

    public static void requestPermission(@NonNull final Activity activity, @PermissionsCodes int code, String[]... permissionsArray) {
        AndPermission.with(activity)
                .requestCode(code)
                .permission(permissionsArray)
                .callback(activity)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(activity, rationale).show();
                    }
                })
                .start();
    }

    public static void requestPermission(@NonNull final Fragment fragment, @PermissionsCodes int code, String[]... permissionsArray) {
        if (fragment.getActivity() == null){
            return;
        }

        AndPermission.with(fragment)
                .requestCode(code)
                .permission(permissionsArray)
                .callback(fragment)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(fragment.getActivity(), rationale).show();
                    }
                })
                .start();
    }

//    public static void showAlwaysDeniedDefaultDialog() {
//            // Default Prompt
//            AndPermission.defaultSettingDialog(this, PermissionsCodes.REQUEST_CODE_SETTING).show();
//            Utils.openAppPermissionSettings(getActivity().getApplicationContext());
//    }

    public static void showAlwaysDeniedCustomDialog(Activity activity) {
        // Custom Prompt
        AndPermission.defaultSettingDialog(activity, PermissionsCodes.REQUEST_CODE_SETTING)
                .setTitle(activity.getString(R.string.permissions_dialog_title))
                .setMessage(String.format(activity.getString(R.string.permissions_dialog_message), activity.getString(R.string.app_name)))
                .setPositiveButton(activity.getString(R.string.permissions_dialog_button_positive))
                .show();

//            // Custom Dialog Style.
//            SettingService settingService = AndPermission.defineSettingDialog(this, PermissionsCodes.REQUEST_CODE_SETTING);
//            // dialog click OK to call:
//            settingService.execute();
//
//            // dialog click Cancel to call:
//            settingService.cancel();
    }
}