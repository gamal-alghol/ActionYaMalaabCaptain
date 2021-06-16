package com.malaab.ya.action.actionyamalaab.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;

import com.malaab.ya.action.actionyamalaab.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;


public final class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    public static File saveJpgImageToGallery(Activity activity, Bitmap bitmap) {
        File photo = null;
        try {

            String mImageName = activity.getString(R.string.app_name) + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            photo = new File(getAlbumStorageDir(activity), mImageName);
            Bitmap compressedBitmap = ImageUtils.compressByScale(bitmap, 400, 300, true);

            saveBitmapToJPG(compressedBitmap, photo);

            scanMediaFile(activity, photo);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return photo;
    }

    private static File getAlbumStorageDir(Activity activity) {
        // Get the directory for the user's public pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

        File file = new File(Environment.getExternalStorageDirectory(), activity.getString(R.string.app_name));
        if (!createOrExistsDir(file)) {
            AppLogger.e("Directory not created");
        }
        return file;
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);

        canvas.drawBitmap(bitmap, 0, 0, null);
//        canvas.drawBitmap(getResizedBitmap(bitmap, 400, 300), 0, 0, null);

        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

    private static void scanMediaFile(Activity activity, File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    // Check image supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());

        if (Constants.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }


    public static Bitmap compressByScale(final Bitmap src, final int newWidth, final int newHeight, final boolean recycle) {
        return scale(src, newWidth, newHeight, recycle);
    }

    public static Bitmap scale(final Bitmap src, final int newWidth, final int newHeight, final boolean recycle) {
        if (isEmptyBitmap(src))
            return null;

        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled())
            src.recycle();

        return ret;
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

}