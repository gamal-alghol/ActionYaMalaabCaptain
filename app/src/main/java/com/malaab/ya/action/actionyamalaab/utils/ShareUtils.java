package com.malaab.ya.action.actionyamalaab.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.malaab.ya.action.actionyamalaab.R;


public final class ShareUtils {

    private static String DYNAMIC_LINK_SCHEME = "https";
    private static String DYNAMIC_LINK_DOMAIN = "actionyamalab.page.link";
    private static String DYNAMIC_LINK_PATH = "app";

    private static String KEY_PLAYGROUND_ID = "playgroundId";
    private static String KEY_BOOKING_ID = "bookingId";
    private static String KEY_PLAYGROUND_TYPE = "playgroundType";

    private static String PACKAGE_IOS = "com.malab.ActionYaMalaab";


    public static void sharePlayground(final Activity activity, String playgroundId, final String playgroundName, final boolean isIndividual, final String bookingId) {

        final Uri deepLink = buildDeepLink(playgroundId, isIndividual, bookingId);

        DynamicLink dynamicLink = buildDynamicLink(deepLink);
        Uri dynamicLinkUri = dynamicLink.getUri();

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLinkUri)
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(activity, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        Uri shortLink = task.getResult().getShortLink();
//                        Uri flowchartLink = task.getResult().getPreviewLink();

                        String msg = String.format(activity.getString(R.string.msg_share_playground_full), playgroundName, shortLink);
                        if (isIndividual) {
                            msg = String.format(activity.getString(R.string.msg_share_playground_individual), playgroundName, shortLink);
                        }

                        shareDeepLink(activity, msg);
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, activity.getString(R.string.error), Toast.LENGTH_LONG).show();
                    }
                });

//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse("https://actionyamalab.page.link/app"))
//                .setDynamicLinkDomain("actionyamalab.page.link")
//                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
//                .addOnCompleteListener(mActivity, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            Uri shortLink = task.getResult().getShortLink();
////                        Uri flowchartLink = task.getResult().getPreviewLink();
//
//                            String msg = "Hey, check out this: " + shortLink;
//
//                            shareDeepLink(msg);
//
//                        } else {
//                        }
//                    }
//                });
    }

    private static void shareDeepLink(Activity activity, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        activity.startActivity(intent);
    }

    private static Uri buildDeepLink(String playgroundId, boolean isIndividual, String BookingId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(DYNAMIC_LINK_SCHEME)
                .authority(DYNAMIC_LINK_DOMAIN)
                .appendPath(DYNAMIC_LINK_PATH)
                .appendQueryParameter(KEY_PLAYGROUND_ID, playgroundId)
                .appendQueryParameter(KEY_PLAYGROUND_TYPE, String.valueOf(isIndividual))
                .appendQueryParameter(KEY_BOOKING_ID, BookingId);

        return builder.build();
    }

    private static DynamicLink buildDynamicLink(Uri deepLink) {
        return FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
//                .setLink(Uri.parse("https://actionyamalab.page.link/app"))
                .setLink(deepLink)
                .setDynamicLinkDomain(DYNAMIC_LINK_DOMAIN)
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder(PACKAGE_IOS).build())
                .buildDynamicLink();
    }

}
