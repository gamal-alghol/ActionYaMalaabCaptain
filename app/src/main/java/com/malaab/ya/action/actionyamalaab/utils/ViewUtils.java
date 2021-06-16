package com.malaab.ya.action.actionyamalaab.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.ScrollView;

import com.malaab.ya.action.actionyamalaab.R;


public final class ViewUtils {

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    public static void changeIconDrawableToGray(Context context, Drawable drawable) {
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP);
        }
    }


    public static void scrollToTop(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public static void scrollToBottom(final NestedScrollView nestedScrollView) {
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public static void scrollToPosition(final RecyclerView rv, final int position) {
        rv.postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.scrollToPosition(position);
            }
        }, 100);
    }
}
