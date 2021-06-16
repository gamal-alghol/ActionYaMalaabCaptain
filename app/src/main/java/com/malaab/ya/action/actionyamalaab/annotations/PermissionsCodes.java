package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({PermissionsCodes.CAMERA, PermissionsCodes.READ_CONTACTS, PermissionsCodes.WRITE_CONTACTS, PermissionsCodes.WRITE_EXTERNAL_STORAGE, PermissionsCodes.LOCATION, PermissionsCodes.REQUEST_CODE_SETTING})
@Retention(RetentionPolicy.SOURCE)
public @interface PermissionsCodes {

    int CAMERA = 1000;

    int READ_CONTACTS = 2000;
    int WRITE_CONTACTS = 3000;

    int WRITE_EXTERNAL_STORAGE = 4000;

    int LOCATION = 5000;

    int REQUEST_CODE_SETTING = 9000;
}
