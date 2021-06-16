package com.malaab.ya.action.actionyamalaab.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.malaab.ya.action.actionyamalaab.R;


public final class AppUtils {

    public static void openPlayStoreForApp(Context context) {
        final String appPackageName = context.getPackageName();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.app_market_link) + appPackageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);

        } catch (android.content.ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources().getString(R.string.app_google_play_store_link) + appPackageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
    }
}
