package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@StringDef({UserRole.ROLE_CAPTAIN, UserRole.ROLE_OWNER, UserRole.ROLE_ADMIN})
@Retention(RetentionPolicy.SOURCE)
public @interface UserRole {

    String ROLE_CAPTAIN = "captain";
    String ROLE_OWNER = "owner";
    String ROLE_ADMIN = "admin";
}
