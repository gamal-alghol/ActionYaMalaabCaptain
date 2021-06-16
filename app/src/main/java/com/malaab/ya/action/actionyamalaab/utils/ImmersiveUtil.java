package com.malaab.ya.action.actionyamalaab.utils;

import android.annotation.TargetApi;
import android.view.View;


public class ImmersiveUtil {

    public static void enter(View decor) {
        ImmersiveUtil19.enter(decor);
    }

    public static void exit(View decor) {
        ImmersiveUtil19.exit(decor);
    }

    @TargetApi(19)
    private static class ImmersiveUtil19 {

        private static final int FLAG_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        static void enter(View decor) {
            SystemUiVisibilityUtil.addFlags(decor, FLAG_IMMERSIVE);
        }

        static void exit(View decor) {
            SystemUiVisibilityUtil.clearFlags(decor, FLAG_IMMERSIVE);
        }
    }
}