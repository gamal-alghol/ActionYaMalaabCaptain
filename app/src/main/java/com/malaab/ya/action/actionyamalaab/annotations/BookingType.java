package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({BookingType.FULL, BookingType.INDIVIDUAL})
@Retention(RetentionPolicy.SOURCE)
public @interface BookingType {
    int FULL = 1;
    int INDIVIDUAL = 2;
}
