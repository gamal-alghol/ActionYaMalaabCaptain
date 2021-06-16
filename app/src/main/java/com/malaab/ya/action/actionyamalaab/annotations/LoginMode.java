package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({LoginMode.LOGGED_IN_MODE_SERVER, LoginMode.LOGGED_IN_MODE_FB, LoginMode.LOGGED_IN_MODE_GOOGLE, LoginMode.LOGGED_IN_MODE_LOGGED_OUT})
@Retention(RetentionPolicy.SOURCE)
public @interface LoginMode {

    int LOGGED_IN_MODE_SERVER = 1;
    int LOGGED_IN_MODE_FB = 2;
    int LOGGED_IN_MODE_GOOGLE = 3;
    int LOGGED_IN_MODE_LOGGED_OUT = 0;
}
