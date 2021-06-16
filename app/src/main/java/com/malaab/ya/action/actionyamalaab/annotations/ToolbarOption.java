package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({ToolbarOption.MENU, ToolbarOption.MAP, ToolbarOption.SEARCH})
@Retention(RetentionPolicy.SOURCE)
public @interface ToolbarOption {
    int MENU = 1;
    int MAP = 2;
    int SEARCH = 3;
}
