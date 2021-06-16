package com.malaab.ya.action.actionyamalaab.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class NotificationUtils {

    private Context mContext;


    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }


    public void showNotificationMessage(String type, String title, String message, String timeStamp, Intent intent) {
        showNotificationMessage(type, title, message, timeStamp, null, intent);
    }

    public void showNotificationMessage(String type, final String title, final String message, final String timeStamp, String imageUrl, Intent intent) {
//        if (TextUtils.isEmpty(message))
//            return;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, "channel_01");

        final int icon = R.drawable.img_profile_default;
//        final int icon = R.mipmap.ic_launcher;
//        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/notification");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                Bitmap bitmap = getBitmapFromURL(imageUrl);
                if (bitmap != null) {
                    showSmallNotification(mBuilder, type, icon, title, message, timeStamp, bitmap, resultPendingIntent, alarmSound);
//                    showBigNotification(mBuilder, title, message, timeStamp, icon, bitmap, resultPendingIntent, alarmSound);
                } else {
                    showSmallNotification(mBuilder, type, icon, title, message, timeStamp, null, resultPendingIntent, alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, type, icon, title, message, timeStamp, null, resultPendingIntent, alarmSound);
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, String type, int icon, String title, String message, String timeStamp, Bitmap senderImage, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(senderImage == null ? BitmapFactory.decodeResource(mContext.getResources(), icon) : getCircleBitmap(senderImage))
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                .setContentText(message)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(Objects.requireNonNull(getNotificationChannel(type)));
            }
            notificationManager.notify(Constants.NOTIFICATION_ID, notification);

            AppLogger.d("*** Notified ***");
        }

//        playNotificationSound();
    }

    private void showBigNotification(NotificationCompat.Builder mBuilder, String type, int icon, String title, String message, String timeStamp, Bitmap senderImage, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(senderImage);
        bigPictureStyle.bigLargeIcon(getCircleBitmap(senderImage));

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
//                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                .setLargeIcon(getCircleBitmap(senderImage))
                .setContentText(message)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(Objects.requireNonNull(getNotificationChannel(type)));
            }
            notificationManager.notify(Constants.NOTIFICATION_ID_BIG_IMAGE, notification);
        }

//        playNotificationSound();
    }


    private NotificationChannel getNotificationChannel(String type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "channel_04";   // The id of the channel.
            CharSequence name = "Other Notifications";     // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            switch (type) {
                case NotificationType.BOOKING_FULL_NEW:
                case NotificationType.BOOKING_INDIVIDUAL_NEW:
                case NotificationType.BOOKING_ADMIN_APPROVED:
                case NotificationType.BOOKING_ADMIN_REJECTED:
                case NotificationType.BOOKING_USER_CANCELLED:
                case NotificationType.BOOKING_OWNER_RECEIVED:
                case NotificationType.BOOKING_INDIVIDUAL_COMPLETED:
                case NotificationType.BOOKING_INDIVIDUAL_NOT_COMPLETED:
                    CHANNEL_ID = "channel_01";
                    name = "Bookings Notifications";
                    break;

                case NotificationType.MESSAGE_CONTACT_US:
                    CHANNEL_ID = "channel_02";
                    name = "Messages Notifications";
                    break;

                case NotificationType.FINE_ISSUED:
                    CHANNEL_ID = "channel_03";
                    name = "Fines Notifications";
                    break;

                case NotificationType.CAPTAIN_NEW:
                case NotificationType.OWNER_NEW:
                case NotificationType.OWNER_PLAYGROUND_NEW:
                case NotificationType.USER_ADMIN_APPROVED:
                    CHANNEL_ID = "channel_04";
                    name = "Other Notifications";
                    break;
            }

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(name.toString());
            mChannel.enableVibration(true);

            return mChannel;
        }

        return null;
    }


    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap temp = bitmap.copy(bitmap.getConfig(), true);

        final Bitmap output = Bitmap.createBitmap(temp.getWidth(), temp.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, temp.getWidth(), temp.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(temp, rect, rect, paint);

        temp.recycle();

        return output;
    }


    private void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    private static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}