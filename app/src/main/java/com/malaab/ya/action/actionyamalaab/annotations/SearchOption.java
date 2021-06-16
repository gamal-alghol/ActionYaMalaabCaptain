package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({SearchOption.PRICE, SearchOption.REGION, SearchOption.AGE, SearchOption.SIZE})
@Retention(RetentionPolicy.SOURCE)
public @interface SearchOption {
    int PRICE = 1;
    int REGION = 2;
    int AGE = 3;
    int SIZE = 4;
}
