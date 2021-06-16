package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({FineType.PAYMENT, FineType.ATTENDANCE})
@Retention(RetentionPolicy.SOURCE)
public @interface FineType {
    int PAYMENT = 0;
    int ATTENDANCE = 1;
}
