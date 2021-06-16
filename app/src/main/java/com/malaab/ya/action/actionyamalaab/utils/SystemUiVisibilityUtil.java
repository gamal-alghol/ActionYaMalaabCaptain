package com.malaab.ya.action.actionyamalaab.utils;

import android.view.View;


public class SystemUiVisibilityUtil {

    static void addFlags(View view, int flags) {
        view.setSystemUiVisibility(view.getSystemUiVisibility() | flags);
    }

    static void clearFlags(View view, int flags) {
        view.setSystemUiVisibility(view.getSystemUiVisibility() & ~flags);
    }

    public static boolean hasFlags(View view, int flags) {
        return (view.getSystemUiVisibility() & flags) == flags;
    }
}