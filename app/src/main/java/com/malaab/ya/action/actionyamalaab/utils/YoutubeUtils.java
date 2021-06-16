package com.malaab.ya.action.actionyamalaab.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.malaab.ya.action.actionyamalaab.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YoutubeUtils {

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        return "http://img.youtube.com/vi/" + getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg";
    }

    private static String getYoutubeVideoIdFromUrl(String inUrl) {
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    public static void openYoutube(Activity activity, String videoUrl) {
        if (!isAppInstalled(activity, "com.google.android.youtube")) {
            Toast.makeText(activity, "Youtube is not Installed!", Toast.LENGTH_LONG).show();
            openGooglePlayStore(activity, "com.google.android.youtube");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.youtube");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(videoUrl));

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, activity.getString(R.string.error_share_no_provider), Toast.LENGTH_LONG).show();
        }
    }


    private static boolean isAppInstalled(Activity activity, String uri) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static void openGooglePlayStore(Activity activity, String appName) {
        Uri uri = Uri.parse("market://details?id=" + appName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        if (goToMarket.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(goToMarket);
        } else {
            Toast.makeText(activity, "No Market apps were found!", Toast.LENGTH_LONG).show();
        }
    }

}
