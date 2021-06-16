package com.malaab.ya.action.actionyamalaab.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public final class NetworkUtils {

    private NetworkUtils() {
        // This utility class is not publicly instantiable
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }

        return false;
    }
}
