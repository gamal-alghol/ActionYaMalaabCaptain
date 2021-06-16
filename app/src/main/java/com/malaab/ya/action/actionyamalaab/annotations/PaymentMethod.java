package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@StringDef({PaymentMethod.CASH})
@Retention(RetentionPolicy.SOURCE)
public @interface PaymentMethod {

    String CASH = "cash";
}
