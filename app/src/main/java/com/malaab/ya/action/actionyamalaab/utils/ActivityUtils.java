package com.malaab.ya.action.actionyamalaab.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.yayandroid.theactivitymanager.TheActivityManager;


public class ActivityUtils {

    public static void setAsLanding(Activity activity) {
        TheActivityManager.getInstance().setAsLanding(activity);
    }

    public static void toLanding() {
        TheActivityManager.getInstance().toLanding();
    }


    public static void toInstanceOf(Class<?> to) {
        TheActivityManager.getInstance().toInstanceOf(to);
    }


    public static void finishInstance(Class<?> clazz) {
        TheActivityManager.getInstance().finishInstance(clazz);
    }

    public static void goTo(Context from, Class<?> to, int activiy) {
        startActivity(from, to, false,false,null,activiy);
    }
    public static void goTo(Context from, Class<?> to, boolean finishIt) {
        startActivity(from, to, finishIt, false, null,-1);
    }

    public static void goTo(Context from, Class<?> to, boolean finishIt, Bundle bundle) {
        startActivity(from, to, finishIt, false, bundle,-1);
    }

    public static void goTo(Context from, Class<?> to, boolean finishIt, boolean isMultipleTask) {
        startActivity(from, to, finishIt, isMultipleTask, null,-1);
    }

    public static void goToWithAnimation(Context from, Class<?> to, boolean finishIt, View view, String transitionName) {
        startActivity(from, to, finishIt, view, transitionName);
    }


//    public static void backTo(ActivityRoot from, Class<?> to, Bundle bundle) {
//        startActivity(from, to, bundle, true, true);
//    }

    private static void startActivity(Context from, Class<?> to, boolean finishIt, boolean isMultipleTask, Bundle bundle,int activity) {
        Intent intent = new Intent(from, to);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
if(activity!=-1){
    intent.putExtra("activity",activity);
}
        if (isMultipleTask) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        /* Since you say you want to start your activity if it's not started already, maybe you wouldn't mind restarting it.
         * I tested a ton of suggestions and flag combinations for the intent as well,
         * this will always bring the activity you need to the front,
         * though it won't keep any state previously associated with it.
         **/

//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //we send 1 to tell the new activity to receive result at runtime,
        // ex: when request login from another activities and we want to return data to original activity
        // ex: ProductDetailsActivity"
//        TheActivityManager.getInstance().getCurrentActivity().startActivityForResult(intent, 1);

        if (TheActivityManager.getInstance().getCurrentActivity() != null) {
            TheActivityManager.getInstance().getCurrentActivity().startActivity(intent);

            if (finishIt) {
                TheActivityManager.getInstance().finishInstance(from.getClass());
            }
        }
    }

    private static void startActivity(Context from, Class<?> to, boolean finishIt, View view, String transitionName) {
        Intent intent = new Intent(from, to);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(TheActivityManager.getInstance().getCurrentActivity(), view, transitionName);
            TheActivityManager.getInstance().getCurrentActivity().startActivityForResult(intent, 999, options.toBundle());
        } else {
            TheActivityManager.getInstance().getCurrentActivity().startActivityForResult(intent, 999);
        }

//        TheActivityManager.getInstance().getCurrentActivity().startActivity(intent);

        if (finishIt) {
            TheActivityManager.getInstance().finishInstance(from.getClass());
        }
    }

}
