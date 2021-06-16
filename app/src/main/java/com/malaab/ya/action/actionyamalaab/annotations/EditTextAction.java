package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({EditTextAction.IME_ACTION_SEARCH, EditTextAction.IME_ACTION_GO, EditTextAction.IME_ACTION_DONE})
@Retention(RetentionPolicy.SOURCE)
public @interface EditTextAction {

    int IME_ACTION_SEARCH = 0;
    int IME_ACTION_GO = 1;
    int IME_ACTION_DONE = 2;
}
