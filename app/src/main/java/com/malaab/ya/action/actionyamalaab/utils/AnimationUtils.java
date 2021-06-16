package com.malaab.ya.action.actionyamalaab.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class AnimationUtils {

    public static void toggleViewHeight(View view, int minHeight) {
        if (view.getHeight() == minHeight) {
            expandView(view, ScreenUtils.getScreenHeight(view.getContext())); //'height' is the height of screen which we have measured already.
        } else {
            collapseView(view, minHeight);
        }
    }


    public static void slideForward(View toHide, final View toShow) {
        YoYo.with(Techniques.SlideOutLeft).duration(300).playOn(toHide);
        YoYo.with(Techniques.SlideInRight).duration(300).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(toShow);
    }

    public static void slideBackward(View toHide, final View toShow) {
        YoYo.with(Techniques.SlideOutRight).duration(300).playOn(toHide);
        YoYo.with(Techniques.SlideInLeft).duration(300).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(toShow);
    }


    public static void collapseView(final View view, int minHeight) {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeightAndState(), minHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        anim.setDuration(300);
        anim.start();
    }

    public static void expandView(final View view, int maxHeight) {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeightAndState(), maxHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.setDuration(300);
        anim.start();
    }

}
