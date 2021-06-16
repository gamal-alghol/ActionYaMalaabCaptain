package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({ItemAction.PICK, ItemAction.DETAILS, ItemAction.SHARE, ItemAction.LOCATION, ItemAction.FAVOURITE_ADD, ItemAction.FAVOURITE_REMOVE, ItemAction.PLAYGROUND_VIEW,
        ItemAction.BOOKING_CANCEL})
@Retention(RetentionPolicy.SOURCE)
public @interface ItemAction {
    int PICK = 1;
    int DETAILS = 2;
    int SHARE = 3;
    int LOCATION = 4;
    int FAVOURITE_ADD = 5;
    int FAVOURITE_REMOVE = 6;
    int PLAYGROUND_VIEW = 7;
    int BOOKING_CANCEL = 8;
}
